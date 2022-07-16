package com.varma.hemanshu.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varma.hemanshu.news.models.Article
import com.varma.hemanshu.news.models.NewsResponse
import com.varma.hemanshu.news.repository.NewsRepository
import com.varma.hemanshu.news.util.Constants.Companion.COUNTRY_CODE
import com.varma.hemanshu.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {

    //Data holder for Breaking News
    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()

    // backing variable for breaking news
    val breakingNews: LiveData<Resource<NewsResponse>> get() = _breakingNews

    //Data holder for search news
    private val _searchNews = MutableLiveData<Resource<NewsResponse>>()

    // backing variable for search news
    val searchNews: LiveData<Resource<NewsResponse>> get() = _searchNews

    private var breakingNewsPage = 1
    private var searchNewsPage = 1

    init {
        getBreakingNews(COUNTRY_CODE)
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        _searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(data = resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    // Will be supporting pagination soon
    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(data = resultResponse)
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

}