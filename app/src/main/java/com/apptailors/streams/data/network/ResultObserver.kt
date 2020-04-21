package com.apptailors.streams.data.network

import androidx.lifecycle.Observer

class ResultObserver<T : Any>(
    private val onSuccess: (T) -> Unit,
    private val onFailure: (Exception) -> Unit
) : Observer<Result<T>> {

    override fun onChanged(result: Result<T>?) {
        when (result) {
            is Result.Success -> onSuccess(result.data)
            is Result.Failure -> onFailure(result.exception)
        }
    }
}