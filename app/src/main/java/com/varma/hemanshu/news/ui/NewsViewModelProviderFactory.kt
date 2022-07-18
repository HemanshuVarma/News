package com.varma.hemanshu.news.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varma.hemanshu.news.repository.NewsRepository

class NewsViewModelProviderFactory(
    private val app: Application,
    private val newsRepository: NewsRepository
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app = app, newsRepository = newsRepository) as T
    }
}