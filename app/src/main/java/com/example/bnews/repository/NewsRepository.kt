package com.example.bnews.repository

import com.example.bnews.api.RetrofitInstance
import com.example.bnews.database.ArticleDatabase
import retrofit2.http.Query

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getLatestNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getLatestNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)
}