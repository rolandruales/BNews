package com.example.bnews.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bnews.NewsActivity
import com.example.bnews.R
import com.example.bnews.adapter.NewsAdapter
import com.example.bnews.databinding.FragmentSavedNewsBinding
import com.example.bnews.databinding.FragmentSearchNewsBinding
import com.example.bnews.viewmodel.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    private val viewModel: NewsViewModel by activityViewModels<NewsViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentSavedNewsBinding? = null
    private val _binding get() = binding!!
    private val TAG = "savedNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        navigateToContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    private fun navigateToContent() {
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_contentNewsFragment,
                bundle
            )
        }
    }

    private fun setUpRecyclerview() {
        _binding.apply {
            latestNewsRecView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                newsAdapter = NewsAdapter()
                adapter = newsAdapter
            }
        }
    }

}