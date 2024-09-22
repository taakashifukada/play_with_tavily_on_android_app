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

        viewModel.response.observe(viewLifecycleOwner) {
            binding.textResponse.text = it.answer
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.textError.text = it
        }

        binding.buttonSubmitQuery.setOnClickListener {
            binding.textError.text = ""
            viewModel.submitQuery(binding.inputQuery.text.toString())
            binding.inputQuery.setText("")
            hideKeyboard()
        }

        viewModel.queryState.observe(viewLifecycleOwner) {
            if(it == QueryState.LOADING) {
                binding.textStatusQuery.text = it.text
                binding.textResponse.text = ""
            } else {
                binding.textStatusQuery.text = ""
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