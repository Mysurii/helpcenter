package com.example.helpcenter.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helpcenter.api.Api
import com.example.helpcenter.api.services.ApiService
import com.example.helpcenter.models.ChatItem
import kotlinx.coroutines.withTimeout

class ResponseRepository() {
    private val apiService: ApiService = Api.createApi()

    private val _response: MutableLiveData<ChatItem.Response> = MutableLiveData()

    val response: LiveData<ChatItem.Response>
        get() = _response

    suspend fun getResponse(id: String) {
        try {
            val result = withTimeout(5_000) {
                apiService.fetchResponse(id)
            }
            _response.value = result
        } catch (err: Throwable) {
            throw ResponseError("Something went wrong with the chatbot. Please reload and try again", err)
        }
    }

    fun resetResponse() {
        _response.value = null
    }

    class ResponseError(message: String, cause: Throwable) : Throwable(message, cause)
}