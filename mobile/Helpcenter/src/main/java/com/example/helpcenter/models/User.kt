package com.example.helpcenter.models

data class User(
    var email: String,
    var isAdmin: Boolean,
    var currentConversationId: String? = null
)