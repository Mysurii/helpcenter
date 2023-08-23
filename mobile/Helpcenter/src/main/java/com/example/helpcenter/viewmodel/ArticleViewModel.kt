package com.example.helpcenter.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.helpcenter.models.ChatItem
import com.example.helpcenter.models.Content
import com.example.helpcenter.models.ListItem
import com.example.helpcenter.repositories.ArticlesRepository
import com.example.helpcenter.repositories.MessageRepository
import com.example.helpcenter.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ArticlesRepository()
    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val articles: LiveData<List<ListItem.Article>> = repository.articles
    val currentArticle: LiveData<ListItem.Article> = repository.currentArticle

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isLoading: LiveData<Boolean>
        get() = _isLoading


    fun fetchArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500)
            try {
                repository.fetchArticles()
            } catch (err: ArticlesRepository.MessageError) {
                _errorText.value = err.message
            }
            _isLoading.value = false
        }
    }


    //Get the Current Article
    fun fetchCurrentArticleContent(article: ListItem.Article) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500)
            try {
                repository.fetchCurrentArticleContent(article)
            } catch (err: ArticlesRepository.MessageError) {
                _errorText.value = err.message
            }
            _isLoading.value = false
        }
    }

    fun removeCurrentArticle() {
        repository.removeCurrentArticle()
    }
}