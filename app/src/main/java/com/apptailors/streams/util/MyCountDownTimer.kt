package com.apptailors.streams.util

import android.os.CountDownTimer

class MyCountDownTimer(repeat: Long, everySec: Long): CountDownTimer(repeat, everySec)  {

    var onFinishLambda:(() -> Unit)? = null
    var onTickLambda:((Long) -> Unit)? = null

    override fun onFinish() {
        onFinishLambda?.invoke()
    }

    override fun onTick(millisUntilFinished: Long) {
        onTickLambda?.invoke(millisUntilFinished)
    }
}