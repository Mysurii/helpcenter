package com.example.helpcenter.api.services

import com.example.helpcenter.models.*
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    // CHAT ENDPOINT
    @GET("conversations/{email}")
    suspend fun fetchConversations(@Path("email") email: String) : List<Chat>

    @GET("messages/{conversationId}")
    suspend fun fetchMessages(@Path("conversationId") id: String) : List<ChatItem.Message>

    @Headers( "Content-Type: application/json; charset=utf-8")
    @POST("messages")
    suspend fun postMessage(@Body body: RequestBody)

    @POST("conversations")
    suspend fun createConversation(@Body body: RequestBody) : Chat

    @GET("users")
    suspend fun fetchEmployees() : List<String>


    // RESPONSES ENDPOINT
    @GET("responses/{intent}")
    suspend fun fetchResponse(@Path("intent") intent: String) : ChatItem.Response


    // ARTICLES ENDPOINT
    @GET("articles/{articleID}")
    suspend fun fetchCurrentArticleContent(@Path("articleID") articleID: String ) : ArrayList<Content>  // Create the list

    @GET("articles")
    suspend fun fetchArticles() :  List<ListItem.Article>

    @POST("contactform")
    suspend fun sendContactform(@Body body: RequestBody)


}