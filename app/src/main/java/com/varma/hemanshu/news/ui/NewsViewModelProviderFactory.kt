package com.varma.hemanshu.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varma.hemanshu.news.repository.NewsRepository


class NewsViewModelProviderFactory(private val newsRepository: NewsRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository = newsRepository) as T
    }
}