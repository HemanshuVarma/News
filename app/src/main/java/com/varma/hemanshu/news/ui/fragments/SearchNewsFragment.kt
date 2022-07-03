package com.varma.hemanshu.news.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.varma.hemanshu.news.R
import com.varma.hemanshu.news.ui.NewsActivity
import com.varma.hemanshu.news.ui.NewsViewModel

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }

}