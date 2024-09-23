package com.example.tavilyplayground.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tavilyplayground.R
import com.example.tavilyplayground.databinding.FragmentHomeBinding
import com.example.tavilyplayground.viewmodel.HomeViewModel
import com.example.tavilyplayground.viewmodel.QueryState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = HomeAdapter()
        binding.recyclerResponse.adapter = adapter

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        binding.recyclerResponse.layoutManager = layoutManager

        // スクロール時にレイアウトを再調整
        binding.recyclerResponse.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager.invalidateSpanAssignments()
            }
        })

        viewModel.response.observe(viewLifecycleOwner) {
            binding.textAnswer.text = it.answer
            adapter.submitList(it.images)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.textError.text = it
        }

        binding.buttonSubmitQuery.setOnClickListener {
            val queryText = binding.inputQuery.text.toString()
            viewModel.submitQuery(queryText)
            binding.inputQuery.setText("")
            hideKeyboard()
        }

        viewModel.queryState.observe(viewLifecycleOwner) {
            if(it == QueryState.LOADING) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        return binding.root
    }

    private fun hideKeyboard() {
        val activity = activity
        if (activity != null && activity.currentFocus != null) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }
}