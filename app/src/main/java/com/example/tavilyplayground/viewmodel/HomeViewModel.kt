package com.example.tavilyplayground.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tavilyplayground.entity.ApiQuery
import com.example.tavilyplayground.entity.ApiResponse
import com.example.tavilyplayground.repository.MainRepository
import com.example.tavilyplayground.ui.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: MainRepository): ViewModel() {

    val response: LiveData<ApiResponse>
        get() = _response
    private val _response = MutableLiveData<ApiResponse>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val queryText = MutableLiveData<String?>()

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

    fun submitQuery() {
        queryText.value?.let {
            val queryObject = ApiQuery(query = it)
            searchJson(queryObject)
        }
        queryText.value = null
    }
}

enum class QueryState(val text: String) {
    LOADING("Loading"),
    SUCCESS("Success"),
    ERROR("Error"),
}