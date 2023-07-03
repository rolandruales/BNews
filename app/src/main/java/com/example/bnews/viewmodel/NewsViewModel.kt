package com.example.bnews.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bnews.model.Article
import com.example.bnews.model.NewsResponse
import com.example.bnews.repository.NewsRepository
import com.example.bnews.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    val latestNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var latestNewsPage = 1
    var latestNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getLatestNews("us")
    }

    fun getLatestNews(countryCode: String) = viewModelScope.launch {
        latestNews.postValue(Resource.Loading())
        val response = repository.getLatestNews(countryCode, latestNewsPage)
        latestNews.postValue(handleLatestNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleLatestNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                latestNewsPage++
                if(latestNewsResponse == null) {
                    latestNewsResponse = resultResponse
                } else {
                    val oldArticles = latestNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(latestNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                searchNewsPage++
                searchNewsResponse = if(searchNewsResponse == null) {
                    resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                    null
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews() = repository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun checkDuplicate(url: String): Boolean {
        return repository.checkDuplicate(url)
    }
}