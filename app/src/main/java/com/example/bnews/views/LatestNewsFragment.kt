package com.example.bnews.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bnews.NewsActivity
import com.example.bnews.R
import com.example.bnews.adapter.NewsAdapter
import com.example.bnews.databinding.FragmentLatestNewsBinding
import com.example.bnews.util.Resource
import com.example.bnews.viewmodel.NewsViewModel

class LatestNewsFragment : Fragment(R.layout.fragment_latest_news) {

    private val viewModel: NewsViewModel by activityViewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentLatestNewsBinding? = null
    private val _binding get() = binding!!
    val TAG = "latestNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLatestNewsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerview()
        setupViewModel()
    }

    //setup recyclerview before displaying
    private fun setUpRecyclerview() {
        _binding.apply {
            latestNewsRecView.apply {
                layoutManager =LinearLayoutManager(requireContext())
                newsAdapter = NewsAdapter()
                adapter = newsAdapter
            }
        }
    }

    //setup viewmodel for displaying in recyclerview
    private fun setupViewModel() {
        viewModel.latestNews.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                else -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "Error occurred: $message")
                    }
                }
            }
        })
    }

    private fun showProgressBar() {
        _binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        _binding.progressBar.visibility = View.GONE
    }
    }
