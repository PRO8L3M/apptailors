package com.apptailors.streams.ui.stream

import androidx.lifecycle.ViewModel
import com.apptailors.streams.data.repository.Repository

class StreamViewModel(private val repository: Repository) : ViewModel() {

    fun getText() = repository.text
}