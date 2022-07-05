package com.varma.hemanshu.news.repository

import com.varma.hemanshu.news.api.RetrofitInstance
import com.varma.hemanshu.news.db.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)
}