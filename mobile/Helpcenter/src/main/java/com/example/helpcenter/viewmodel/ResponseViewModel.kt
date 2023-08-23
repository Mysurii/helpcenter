package com.example.helpcenter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.helpcenter.models.ChatItem
import com.example.helpcenter.repositories.ResponseRepository
import kotlinx.coroutines.launch

class ResponseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ResponseRepository()
    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val messages: LiveData<ChatItem.Response> = repository.response
    val errorText: LiveData<String>
        get() = _errorText

    fun getResponse(intent: String) {
        viewModelScope.launch {
            try {
                repository.getResponse(intent)

            } catch (err: Throwable) {
                _errorText.value = "Couldn't load the response of the chatbot"
            }
        }
    }

    fun resetResponse() {
        repository.resetResponse()
    }
}