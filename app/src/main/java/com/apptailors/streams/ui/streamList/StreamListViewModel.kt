package com.apptailors.streams.ui.streamList

import androidx.lifecycle.ViewModel
import com.apptailors.streams.data.repository.Repository

class StreamListViewModel(private val repository: Repository) : ViewModel() {

    fun getStreamList() = repository.getStreamList()
}