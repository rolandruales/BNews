package com.example.bnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bnews.database.ArticleDatabase
import com.example.bnews.databinding.ActivityMainBinding
import com.example.bnews.repository.NewsRepository
import com.example.bnews.viewmodel.NewsViewModel
import com.example.bnews.viewmodel.NewsViewModelProviderFactory


class NewsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.newsNavHostFragment)
        binding.bottomNavView.setupWithNavController(navController)

        val repository = NewsRepository(ArticleDatabase(this))
        val viewModelFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
    }
}