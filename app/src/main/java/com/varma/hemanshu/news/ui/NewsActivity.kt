package com.varma.hemanshu.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.varma.hemanshu.news.R
import com.varma.hemanshu.news.db.ArticleDatabase
import com.varma.hemanshu.news.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newsRepository = NewsRepository(ArticleDatabase.invoke(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        //Inflating UI after viewModel is initialized/ready
        setContentView(R.layout.activity_news)

        //Coupling bottom navigation view with navigation graph(it's controller)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}