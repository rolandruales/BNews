package com.example.bnews.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bnews.NewsActivity
import com.example.bnews.R
import com.example.bnews.adapter.NewsAdapter
import com.example.bnews.databinding.FragmentLatestNewsBinding
import com.example.bnews.util.Constants.Companion.QUERY_PAGE_SIZE
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
        navigateToContent()
    }

    //setup recyclerview before displaying
    private fun setUpRecyclerview() {
        _binding.apply {
            latestNewsRecView.apply {
                layoutManager =LinearLayoutManager(requireContext())
                newsAdapter = NewsAdapter()
                adapter = newsAdapter
                addOnScrollListener(this@LatestNewsFragment.scrollListener)
            }
        }
    }

    private fun navigateToContent() {
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_latestNewsFragment_to_contentNewsFragment,
                bundle
            )
        }
    }

    //setup viewmodel for displaying in recyclerview
    private fun setupViewModel() {
        viewModel.latestNews.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.latestNewsPage == totalPages
                        if (isLastPage) {
                            _binding.latestNewsRecView.setPadding(0, 0, 0, 0)
                        }
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
        isLoading = true
    }

    private fun hideProgressBar() {
        _binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    //handles pagination of recyclerview
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getLatestNews("us")
                isScrolling = false
            }
        }
    }
}
