package com.apptailors.streams.ext

import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(requireContext(), text, duration).show()

fun Fragment.snackBar(text: String, duration: Int = Snackbar.LENGTH_SHORT) = Snackbar.make(requireView(), text, duration).show()

fun Fragment.navigateTo(@IdRes destination: Int) {
    findNavController().navigate(destination)
}

inline fun Fragment.startCoroutineTimer(delayMillis: Long = 0, repeatMillis: Long = 0, crossinline action: () -> Unit) = lifecycleScope.launch {
    delay(delayMillis)
    if (repeatMillis > 0) {
        while (true) {
            action()
            delay(repeatMillis)
        }
    } else {
        action()
    }
}