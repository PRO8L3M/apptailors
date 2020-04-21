package com.apptailors.streams.ext

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

inline fun <reified T> RecyclerView.Adapter<*>.getAsyncListDiffer(noinline areItemsTheSame: ((oldItem: T, newItem:T) -> Boolean)): AsyncListDiffer<T> {
    val diffUtilCallback = object: DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) = areItemsTheSame.invoke(oldItem, newItem)

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    }
    return AsyncListDiffer(this, diffUtilCallback)
}


fun View.snackBar(text: String, duration: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(this, text, duration).show()