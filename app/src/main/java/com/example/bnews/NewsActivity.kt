package com.example.bnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bnews.database.ArticleDatabase
import com.example.bnews.databinding.ActivityMainBinding
import com.example.bnews.repository.NewsRepository
import com.example.bnews.viewmodel.NewsViewModel
import com.example.bnews.viewmodel.NewsViewModelProviderFactory


class NewsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)
//        val navController = findNavController(R.id.newsNavHostFragment)
//        binding.bottomNavView.setupWithNavController(navController)

        val repository = NewsRepository(ArticleDatabase.getDatabase(this))
        val viewModelFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]

        try {
            setContentView(binding.root)
            val newsRepository = NewsRepository(ArticleDatabase.getDatabase(this))
            val noteViewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
            viewModel = ViewModelProvider(
                this,
                noteViewModelProviderFactory
            )[NewsViewModel::class.java]
        } catch (_: java.lang.Exception) {
        }
    }
}