package com.example.helpcenter.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helpcenter.api.Api
import com.example.helpcenter.api.services.ApiService
import com.example.helpcenter.models.Chat
import com.example.helpcenter.models.ChatItem
import com.example.helpcenter.utils.Constants.loggedInUser
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class MessageRepository() {
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val chatPlaceHolder = Chat("", arrayListOf(), ChatItem.Message())
    private val apiService: ApiService = Api.createApi()
    private val _conversations: MutableLiveData<List<Chat>> = MutableLiveData()

    private val _messages: MutableLiveData<List<ChatItem.Message>> = MutableLiveData()
    private val _activeChat: MutableLiveData<Chat> = MutableLiveData()
    private val _lastMessage: MutableLiveData<ChatItem.Message> = MutableLiveData()
    private val _employees: MutableLiveData<List<String>> = MutableLiveData()

    val messages: LiveData<List<ChatItem.Message>>
        get() = _messages

    val conversations: LiveData<List<Chat>>
        get() = _conversations
    val activeChat: LiveData<Chat>
        get() = _activeChat
    val lastMessage: LiveData<ChatItem.Message>
        get() = _lastMessage
    val employees: LiveData<List<String>>
        get() = _employees

    init {
        _messages.value = listOf()
    }

    suspend fun fetchConversations(email: String) {
        try {
            val result = withTimeout(5_000) {
                apiService.fetchConversations(email)
            }
            _conversations.value = result
        } catch (err: Throwable) {
            throw ConversationRefreshError("Something went wrong with the conversation", err)
        }
    }

    suspend fun createConversation(employeeEmail: String) {
        try {
            val userEmail = loggedInUser.email
            val obj =
                JSONObject("""{members: [$userEmail, $employeeEmail], creator: $userEmail}""").toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            val result = withTimeout(5_000) {
                apiService.createConversation(obj)
            }

            getMessages(result.id)

            val allConversations = _conversations.value?.toMutableList()

            // Only add the conversation if its necessary
            if (!exists(result.id))
                allConversations?.add(result)

            mainScope.launch {
                _conversations.value = allConversations
                _activeChat.value = result
                _activeChat.value?.members?.find { x -> x != loggedInUser.email }?.let {
                    SocketHandler.emitNewConversation(result, it)
                }
            }
        } catch (err: Throwable) {
            throw ConversationRefreshError("Something went wrong with the conversation", err)
        }
    }

    suspend fun postMessage(text: String, members: String) {

        try {
            val messages = _messages.value?.toMutableList()
            val message = ChatItem.Message(text, members = members, from = loggedInUser.email)
            messages?.add(message)
            _messages.value = messages

            val email = loggedInUser.email
            val obj =
                JSONObject("""{"senderEmail": "$email", "text": "$text", "members": "$members"}""").toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            withTimeout(5_000) {
                apiService.postMessage(obj)
            }
            mainScope.launch {
                _lastMessage.value = message
                _activeChat.value?.lastMessage = message
            }
        } catch (err: Throwable) {
            throw MessageError("Failed to save the message", err)
        }
    }

    fun setActiveChat(conversationId: String) {
        if (conversations.value != null) {
            val foundChat = findChat(conversationId)
            if (foundChat != null) {
                foundChat.isRead = true
                _activeChat.value = foundChat
            }
        }
    }

    suspend fun getMessages(id: String) {
        try {
            val result = withTimeout(10_000) {
                apiService.fetchMessages(id)
            }
            _messages.value = result
        } catch (err: Throwable) {
            throw MessageError("Failed to receive the messages", err)
        }
    }

    suspend fun fetchEmployees() {
        try {
            val result = withTimeout(5_000) {
                apiService.fetchEmployees()
            }
            _employees.value = result
        } catch (err: Throwable) {
            throw ConversationRefreshError("Failed to receive the employees data", err)
        }
    }

    fun listenToAddMessage() {
        SocketHandler.getSocket().on("getMessage") { args ->
            if (args != null && args[0] != null) {
                val message = Gson().fromJson(args[0].toString(), ChatItem.Message::class.java)
                val allMessages = _messages.value?.toMutableList()

                allMessages?.add(message)

                val foundConversation = message?.conversationId?.let { findChat(it) }
                val index = _conversations.value?.indexOf(foundConversation)

                mainScope.launch {
                    if (foundConversation != null) {
                        if (foundConversation !== activeChat.value) foundConversation.isRead = false
                        val newConversations = _conversations.value?.toMutableList()
                        if (index != null && newConversations != null) {
                            foundConversation.lastMessage = message
                            newConversations[index] = foundConversation
                            _conversations.value = newConversations
                        }
                    }
                    _lastMessage.value = message
                    _messages.value = allMessages
                }
            }
        }
    }

    private fun updateConversation(conversation: Chat) {
        val foundConversation = findChat(conversation.id)
        if (foundConversation != null) {
            val newConversations = _conversations.value?.toMutableList()
            val index = _conversations.value?.indexOf(foundConversation)

            mainScope.launch {
                if (index != null) {
                    newConversations?.set(index, conversation)
                    _conversations.value = newConversations
                }
            }
        }

    }

    fun observeTyping() {
        SocketHandler.getSocket().on("isTyping") { args ->
            if (args != null) {
                val data = JSONObject(args[0].toString())
                val conversationId = data.getString("conversationId")
                val isTyping = data.getBoolean("isTyping")
                val foundChat = findChat(conversationId)

                mainScope.launch {
                    if (foundChat != null) {
                        if (foundChat.isTyping != isTyping) {
                            val newConversations = _conversations.value?.toMutableList()
                            val index = _conversations.value?.indexOf(foundChat)
                            if (index != null) {
                                foundChat.isTyping = isTyping
                                newConversations?.set(index, foundChat)
                                _conversations.value = newConversations
                            }
                        }
                    }
                }
            }
        }
    }

    fun listenToUsers() {
        SocketHandler.getSocket().on("users") { args ->
            if (args != null && args[0] != null) {
                val data = args[0] as JSONArray
                val emails = arrayListOf<String>()
                for (i in 0 until data.length()) {
                    val jsonObject = data.get(i) as JSONObject
                    val user = jsonObject.get("user") as JSONObject
                    val email = user.get("email") as String
                    if (email != loggedInUser.email)
                        emails.add(email)
                }

                mainScope.launch {
                    conversations.value?.forEach {
                        val foundChat = conversations.value?.find { chat -> chat.id === it.id }
                        if (foundChat != null) {
                            foundChat.isOnline = emails.any { email -> it.members.contains(email) }
                            updateConversation(foundChat)
                        }
                    }
                }
            }
        }
    }

    fun removeActiveChat() {
        this._activeChat.value = chatPlaceHolder
        this._messages.value = arrayListOf()
    }

    private fun findChat(id: String): Chat? {
        return this._conversations.value?.find { x -> x.id == id }
    }

    private fun exists(id: String): Boolean {
        val chat = conversations.value?.find { x -> x.id == id }
        return chat != null
    }

    class ConversationRefreshError(message: String, cause: Throwable) : Throwable(message, cause)
    class MessageError(message: String, cause: Throwable) : Throwable(message, cause)
}