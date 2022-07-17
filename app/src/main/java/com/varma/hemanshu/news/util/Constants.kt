package com.varma.hemanshu.news.util

class Constants {

    companion object {
        const val API_KEY = "5cf2d01607e44838b7d4203ee4ed736f"
        const val BASE_API = "https://newsapi.org"

        // See https://newsapi.org/docs/endpoints/top-headlines for country codes
        const val COUNTRY_CODE = "us"

        const val SEARCH_NEWS_DELAY = 500L

        const val QUERY_PAGE_SIZE = 20
    }
}