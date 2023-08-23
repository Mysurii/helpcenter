package com.example.helpcenter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helpcenter.models.ChatItem
import com.example.helpcenter.repositories.MessageRepository
import androidx.lifecycle.viewModelScope
import com.example.helpcenter.models.Chat
import com.example.helpcenter.repositories.SocketHandler
import com.example.helpcenter.utils.Constants.loggedInUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MessageRepository()

    val activeChat: LiveData<Chat> = repository.activeChat
    val messages: LiveData<List<ChatItem.Message>> = repository.messages
    val conversations: LiveData<List<Chat>> = repository.conversations
    val employees: LiveData<List<String>> = repository.employees

    val lastMessage: LiveData<ChatItem.Message> = repository.lastMessage

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val errorText: LiveData<String>
        get() = _errorText

    fun addMessage(text: String, members: String) {
        viewModelScope.launch {
            try {
                repository.postMessage(text, members)
            } catch (err: MessageRepository.MessageError) {
                _errorText.value = err.message
            }
        }
    }


    fun createConversation(email: String = "support@vormats.nl") {
        viewModelScope.launch {
            try {
                repository.createConversation(email)
            } catch (err: MessageRepository.ConversationRefreshError) {
                _errorText.value = err.message
            }
        }
    }

    private fun fetchConversations() {
        viewModelScope.launch {
            try {
                repository.fetchConversations(loggedInUser.email)
            } catch (err: MessageRepository.ConversationRefreshError) {
                _errorText.value = err.message
            }
        }
    }

    fun fetchMessages(id: String) {
        viewModelScope.launch {
            try {
                repository.getMessages(id)
            } catch (err: MessageRepository.MessageError) {
                _errorText.value = err.message
            }
        }
    }

    fun fetchEmployees() {
        viewModelScope.launch {
            try {
                repository.fetchEmployees()
            } catch (err: MessageRepository.ConversationRefreshError) {
                _errorText.value = err.message
            }
        }
    }

    fun setActiveChat(conversationId: String) {
        repository.setActiveChat(conversationId)
    }

    fun connect() {
        SocketHandler.establishConnection()
        SocketHandler.emitAddUser(null)
        repository.listenToUsers()
        fetchConversations()
        repository.listenToAddMessage()
        repository.observeTyping()
    }

    fun removeActiveChat() {
        repository.removeActiveChat()
    }

    fun setTimeout(callback: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500)
            callback()
            _isLoading.value = false
        }
    }
}