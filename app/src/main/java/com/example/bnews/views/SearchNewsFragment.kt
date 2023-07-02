package com.example.bnews.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bnews.NewsActivity
import com.example.bnews.R
import com.example.bnews.adapter.NewsAdapter
import com.example.bnews.databinding.FragmentLatestNewsBinding
import com.example.bnews.databinding.FragmentSearchNewsBinding
import com.example.bnews.util.Constants.Companion.SEARCH_NEWS_DELAY
import com.example.bnews.util.Resource
import com.example.bnews.viewmodel.NewsViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private val viewModel: NewsViewModel by activityViewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentSearchNewsBinding? = null
    private val _binding get() = binding!!
    private val TAG = "searchNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        setupViewModel()
        searchNews()
        navigateToContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    //search news function
    private fun searchNews() {
        var job: Job? = null
        _binding.searchNewsEditText.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                         viewModel.searchNews(editable.toString())
                        setupViewModel()
                        setupViewModel()
                    }
                }
            }
        }
    }

    private fun navigateToContent() {
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_contentNewsFragment,
                bundle
            )
        }
    }

    //setup recyclerview before displaying
    private fun setUpRecyclerview() {
        _binding.apply {
            latestNewsRecView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                newsAdapter = NewsAdapter()
                adapter = newsAdapter
            }
        }
    }

    //setup viewmodel for displaying in recyclerview
    private fun setupViewModel() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {response ->
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