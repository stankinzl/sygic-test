package com.kinzlstanislav.sigyctest.core.extensions

import android.os.Handler
import android.os.Looper

fun postDelayed(delay: Int, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ action() }, delay.toLong())
}