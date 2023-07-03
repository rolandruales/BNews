package com.example.bnews.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bnews.model.Article

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT Count(*) FROM articles WHERE url = :url")
    fun checkDuplicate(url: String): Long

    @Query("DELETE FROM articles")
    suspend fun deleteAll()
}