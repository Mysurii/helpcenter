package com.example.helpcenter.models

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue

sealed class ListItem {
    data class Theme(
        val title: String
    ) : ListItem()

    data class Article(
        @SerializedName("_id") var articleID: String,
        var title: String = "",
        var description: String = "",

        var contents: @RawValue ArrayList<Content> = arrayListOf(),
        val theme: String = "",
    ) : ListItem()

    // Create a list
}

data class Header(
    var title: String,
    var description: String
)