package com.varma.hemanshu.news.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.varma.hemanshu.news.models.Article

@Dao
interface ArticleDao {

    //Insert article in DB or Update otherwise
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}