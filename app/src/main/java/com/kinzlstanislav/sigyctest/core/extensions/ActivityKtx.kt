package com.kinzlstanislav.sigyctest.core.extensions

import android.app.Activity

fun Activity.disableTouchGestures() {
    window.disableTouchGestures()
}

fun Activity.enableTouchGestures() {
    window.enableTouchGestures()
}