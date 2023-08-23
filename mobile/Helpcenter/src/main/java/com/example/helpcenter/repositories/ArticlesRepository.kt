package com.example.helpcenter.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helpcenter.api.Api
import com.example.helpcenter.api.services.ApiService
import com.example.helpcenter.models.ListItem
import kotlinx.coroutines.withTimeout


class ArticlesRepository {

    private val apiService: ApiService = Api.createApi()

    private val _articles: MutableLiveData<List<ListItem.Article>> = MutableLiveData()

    private val _currentArticle: MutableLiveData<ListItem.Article> = MutableLiveData()

    val articles: LiveData<List<ListItem.Article>>
        get() = _articles

    val currentArticle: LiveData<ListItem.Article>
        get() = _currentArticle

    suspend fun fetchArticles() {
        try {
            val result = withTimeout(5_000) {
                apiService.fetchArticles()
            }
            _articles.value = result
        } catch (err: Throwable) {
            Log.d("err", err.toString())
        }
    }

    suspend fun fetchCurrentArticleContent(article : ListItem.Article) {
        try {
            val result = withTimeout(5_000) {
                apiService.fetchCurrentArticleContent(article.articleID)
            }
            article.contents = result
            _currentArticle.value = article
        } catch (err: Throwable) {
            Log.d("err", err.toString())
        }
    }

    fun removeCurrentArticle() {
        this._currentArticle.value = ListItem.Article("", "", "")
    }

    class MessageError(message: String, cause: Throwable) : Throwable(message, cause)
}

