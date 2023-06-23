package com.example.bnews.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bnews.newsapi.NewsResponse
import com.example.bnews.repository.NewsRepository
import com.example.bnews.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val latestNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private val latestNewsPage = 1

    fun getLatestNews(countryCode: String) = viewModelScope.launch {
        latestNews.postValue(Resource.Loading())
        val response = repository.getLatestNews(countryCode, latestNewsPage)
        latestNews.postValue(handelLatestNewsResponse(response))
    }

    private fun handelLatestNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}