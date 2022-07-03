package com.varma.hemanshu.news

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)