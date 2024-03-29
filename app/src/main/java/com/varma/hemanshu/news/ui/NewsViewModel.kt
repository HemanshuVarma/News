package com.varma.hemanshu.news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.varma.hemanshu.news.NewsApplication
import com.varma.hemanshu.news.models.Article
import com.varma.hemanshu.news.models.NewsResponse
import com.varma.hemanshu.news.repository.NewsRepository
import com.varma.hemanshu.news.util.Constants.Companion.COUNTRY_CODE
import com.varma.hemanshu.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, val newsRepository: NewsRepository) : AndroidViewModel(app) {

    //Data holder for Breaking News
    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()

    // backing variable for breaking news
    val breakingNews: LiveData<Resource<NewsResponse>> get() = _breakingNews

    // Caching all the responses when using pagination for breaking news
    var breakingNewsResponse: NewsResponse? = null

    //Data holder for search news
    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()

    // backing variable for search news
    val searchNews: LiveData<Resource<NewsResponse>> get() = _searchNews

    // Caching all the responses when using pagination for search news
    var searchNewsResponse: NewsResponse? = null

    var breakingNewsPage = 1
    var searchNewsPage = 1

    init {
        getBreakingNews(COUNTRY_CODE)
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // incrementing page counter for pagination
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(data = breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                // incrementing page counter for pagination
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(data = searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getAllSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        _searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                _searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                _searchNews.postValue(Resource.Error("No internet available"))
            }
        } catch (t: Throwable) {
            // Handling error when response is dirty
            when (t) {
                is IOException -> {
                    // caused by retrofit
                    _searchNews.postValue(Resource.Error("Network failure"))
                }
                else -> {
                    // conversion error
                    _searchNews.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        _breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                _breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                _breakingNews.postValue(Resource.Error("No internet available"))
            }
        } catch (t: Throwable) {
            // Handling error when response is dirty
            when (t) {
                is IOException -> {
                    // caused by retrofit
                    _breakingNews.postValue(Resource.Error("Network failure"))
                }
                else -> {
                    // conversion error
                    _breakingNews.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}