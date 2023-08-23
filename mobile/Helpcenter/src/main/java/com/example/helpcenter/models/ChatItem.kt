package com.example.helpcenter.models

import com.example.helpcenter.utils.Constants.RECEIVED_ID
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

sealed class ChatItem() {

    data class Response(
        @SerializedName("_id") val id: String? = null,
        @SerializedName("title") val intent: String,
        @SerializedName("messages") val messages: List<String>,
        @SerializedName("options") val options: List<Option>? = null,
        @SerializedName("createdAt") val createdAt: Date

    ) : ChatItem()

    data class Message(

        @SerializedName("text") var message: String? = "",
        @SerializedName("createdAt") var createdAt: Date = Date(),
        @SerializedName("members") var members: Any? = null,
        @SerializedName("senderEmail") var from: String? = RECEIVED_ID,
        var options: ArrayList<Option>? = null,

        @SerializedName("conversationId") var conversationId: String? = null,
        @SerializedName("_id") var messageId: String? = null,
    ) : ChatItem()

    data class Option(
        var text: String? = "",
        var nextIntent: String? = "",
    ) : ChatItem()

    data class DateMessage(
        var createdAt: Date,
    ) : ChatItem()

    class TypingIndicator : ChatItem()
}