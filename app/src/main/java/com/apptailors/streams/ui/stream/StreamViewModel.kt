package com.apptailors.streams.ui.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptailors.streams.data.entity.BuffResult
import com.apptailors.streams.data.network.Result
import com.apptailors.streams.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreamViewModel(private val repository: Repository) : ViewModel() {

    private val _question: MutableLiveData<Result<BuffResult>> by lazy { MutableLiveData<Result<BuffResult>>() }
    val question: LiveData<Result<BuffResult>> = _question

    fun getQuestion(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getQuestion(id)
            withContext(Dispatchers.Main) {
                _question.value = result
            }
        }
    }
}