package com.example.bnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
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
        setContentView(binding.root)

        bottomNav()
        viewModelFactory()
        setTopLevelDestination()
    }

    //back button in action ba when navigating
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    //viewmodel factory to instantiate viewmodel class and return it
    private fun viewModelFactory() {
        val repository = NewsRepository(ArticleDatabase.getDatabase(this))
        val viewModelFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
    }

    //setup bottom navigation view
    private fun bottomNav() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    //set the top level destination to hide the back button of bot nav's fragments
    private fun setTopLevelDestination() {
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.latestNewsFragment, R.id.savedNewsFragment, R.id.searchNewsFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    //hide bot nav in some fragments
    fun showBotNav(show: Boolean) {
        if (show) {
            binding.bottomNavView.visibility = View.VISIBLE
        } else {
            binding.bottomNavView.visibility = View.GONE
        }
    }


}