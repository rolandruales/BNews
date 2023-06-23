package com.example.bnews.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.bnews.NewsActivity
import com.example.bnews.R
import com.example.bnews.viewmodel.NewsViewModel

class ContentNewsFragment : Fragment(R.layout.fragment_content_news) {

    lateinit var viewModel: NewsViewModel
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel = (activity as NewsActivity).viewModel
//    }
}