package com.example.bnews.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.bnews.NewsActivity
import com.example.bnews.R
import com.example.bnews.adapter.NewsAdapter
import com.example.bnews.databinding.FragmentContentNewsBinding
import com.example.bnews.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ContentNewsFragment : Fragment(R.layout.fragment_content_news) {

    private val viewModel: NewsViewModel by activityViewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentContentNewsBinding? = null
    private val _binding get() = binding!!
    private val args: ContentNewsFragmentArgs by navArgs()
    private val TAG = "contentNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadArticle()
        fab()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentNewsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    //load article to the webView
    private fun loadArticle() {
        val article = args.article
        _binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }
    }

    //save article button
    private fun fab() {
        val content = args.article

        _binding.fabSave.setOnClickListener {
            if (!viewModel.checkDuplicate(content.url!!)) {
                viewModel.saveArticle(content)
                Snackbar.make(view!!, "Saved to favorites", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(view!!, "Article already saved", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as NewsActivity).showBotNav(false)
    }

    override fun onPause() {
        super.onPause()
        (activity as NewsActivity).showBotNav(true)
    }
}