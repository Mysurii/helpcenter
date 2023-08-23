package com.example.helpcenter.repositories

import android.util.Log
import com.example.helpcenter.models.Chat
import com.example.helpcenter.utils.Constants.loggedInUser
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException


object SocketHandler {
    lateinit var mSocket: Socket
    private const val socketLink = "https://vormats-helpcenter.herokuapp.com"

    init {
        setSocket()
        establishConnection()
    }

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(socketLink)
        } catch (err: URISyntaxException) {
            Log.d("Urisyntax error:", err.message.toString())
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

    fun emitNewConversation(conversation: Chat, employeeEmail: String) {
        mSocket.emit(
            "addNewConversation",
            JSONObject("""{receiverEmail:$employeeEmail,conversation:{_id:${conversation.id},members:${JSONArray(conversation.members)},lastMessage:{_id:${conversation.lastMessage?.messageId},message:"${conversation.lastMessage?.message}",createdAt:"${conversation.lastMessage?.createdAt}",members:"${conversation.lastMessage?.members}"}}}""")
        )
    }

    @Synchronized
    fun emitAddUser(currentConversationId: String?) {
        val email = loggedInUser.email
        mSocket.emit(
            "addUser",
            JSONObject("""{"email": "$email", "currentConversationId": "$currentConversationId"}""")
        )
    }

    @Synchronized
    fun emitMessage(
        conversationId: String,
        senderEmail: String,
        receiverEmail: String,
        text: String
    ) {
        this.mSocket.emit(
            "sendMessage",
            JSONObject("""{"conversationId": "$conversationId", "senderEmail": "$senderEmail", "receiverEmail": "$receiverEmail", "text": "$text"}""")
        );
    }

    @Synchronized
    fun emitIsTyping(receiverEmail: String, conversationId: String, isTyping: Boolean) {
        mSocket.emit(
            "typing",
            JSONObject("""{receiverEmail: $receiverEmail, conversationId: $conversationId, isTyping: $isTyping}""")
        )
    }
}