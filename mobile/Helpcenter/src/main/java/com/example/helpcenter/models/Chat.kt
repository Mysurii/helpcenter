package com.example.helpcenter.models

import com.google.gson.annotations.SerializedName

data class Chat(
//    var email: String = "",
//    var lastMessage: ChatItem.Message? = null,
//    var amountOfUnreadMessages: Int = 0
//    var isEmployeeTyping: Boolean? = false,
//    var isUserTyping: Boolean? = false
    @SerializedName("_id") val id: String,
    @SerializedName("members") val members: List<String>,
    @SerializedName("lastMessage") var lastMessage: ChatItem.Message? = null,
    var isOnline: Boolean? = false,
    var isRead: Boolean? = true,
    var isTyping: Boolean? = false
)