package com.apptailors.streams.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

inline fun CoroutineScope.startCoroutineTimer(delayMillis: Long = 0, repeatMillis: Long = 0, crossinline action: () -> Unit) = launch {
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

inline fun <reified T: Activity> Context.startActivity(block: (Intent.() -> Unit) = {}) {
    val intent = Intent(this, T::class.java).apply { block() }
    startActivity(intent)
}
