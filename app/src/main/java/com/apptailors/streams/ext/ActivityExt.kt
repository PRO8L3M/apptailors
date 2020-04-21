package com.apptailors.streams.ext

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