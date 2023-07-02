package com.example.bnews.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.bnews.NewsActivity
import com.example.bnews.R
import com.example.bnews.adapter.NewsAdapter
import com.example.bnews.databinding.FragmentContentNewsBinding
import com.example.bnews.databinding.FragmentSavedNewsBinding
import com.example.bnews.viewmodel.NewsViewModel

class ContentNewsFragment : Fragment(R.layout.fragment_content_news) {

    private val viewModel: NewsViewModel by activityViewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentContentNewsBinding? = null
    private val _binding get() = binding!!
    private val args: ContentNewsFragmentArgs by navArgs()
    private val TAG = "contentNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        _binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentNewsBinding.inflate(inflater, container, false)
        return _binding.root
    }
}