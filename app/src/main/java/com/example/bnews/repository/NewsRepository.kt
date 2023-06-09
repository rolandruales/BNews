package com.example.bnews.repository

import com.example.bnews.api.RetrofitInstance
import com.example.bnews.database.ArticleDatabase
import com.example.bnews.model.Article
import retrofit2.http.Query

class NewsRepository(
    private val db: ArticleDatabase
) {
    suspend fun getLatestNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getLatestNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun checkDuplicate(url: String): Boolean {
        val count = db.getArticleDao().checkDuplicate(url)
        return count > 0
    }

    suspend fun deleteAll() = db.getArticleDao().deleteAll()
}