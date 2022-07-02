package com.varma.hemanshu.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.varma.hemanshu.news.R
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //Coupling bottom navigation view with navigation graph(it's controller)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}