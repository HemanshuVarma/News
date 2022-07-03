package com.varma.hemanshu.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varma.hemanshu.news.models.NewsResponse
import com.varma.hemanshu.news.repository.NewsRepository
import com.varma.hemanshu.news.util.Constants.Companion.COUNTRY_CODE
import com.varma.hemanshu.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {

    private val _breakingNews = MutableLiveData<Resource<NewsResponse>>()

    // backing variable for breaking news
    val breakingNews: LiveData<Resource<NewsResponse>> get() = _breakingNews

    var breakingNewsPage = 1

    init {
        getBreakingNews(COUNTRY_CODE)
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(data = resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}