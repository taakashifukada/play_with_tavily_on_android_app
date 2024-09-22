package com.example.tavilyplayground.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.tavilyplayground.entity.ApiQuery
import com.example.tavilyplayground.entity.ApiResponse
import com.example.tavilyplayground.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: MainRepository): ViewModel() {

    val response: LiveData<ApiResponse>
        get() = _response
    private val _response = MutableLiveData<ApiResponse>()
    val responseText: LiveData<String> = _response.map { response ->
        buildString {
            append("answer: \n${response.answer}\n\n")
            response.results.map {
                append("result: \n${it.content}\n\n")
            }
        }
    }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _queryState = MutableLiveData<QueryState>()
    val queryState: LiveData<QueryState>
        get() = _queryState

    private fun searchJson(query: ApiQuery) {
        viewModelScope.launch {
            try {
                _queryState.value = QueryState.LOADING
                _response.value = repository.searchJson(query)
                _queryState.value = QueryState.SUCCESS
            } catch (e: Exception) {
                _queryState.value = QueryState.ERROR
                _errorMessage.value = e.toString()
            }
        }
    }

    fun submitQuery(queryInput: String) {
        if(queryInput.isNullOrEmpty()) {
            return
        }

        val queryObject = ApiQuery(query = queryInput)
        searchJson(queryObject)
    }
}

enum class QueryState(val text: String) {
    LOADING("Loading"),
    SUCCESS("Success"),
    ERROR("Error"),
}