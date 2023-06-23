package com.example.bnews.repository

import com.example.bnews.api.RetrofitInstance
import com.example.bnews.database.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getLatestNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getLatestNews(countryCode, pageNumber)
}