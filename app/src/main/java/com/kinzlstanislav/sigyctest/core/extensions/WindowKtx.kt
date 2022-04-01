package com.kinzlstanislav.sigyctest.core.extensions

import android.os.Build
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

fun Window.drawUiUnderSystemBars(artificiallyPadLayout: ViewGroup? = null) {
    setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
    // FOR Android 11 and higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setDecorFitsSystemWindows(false)
    }
    // for API < 27, not set from xml theme
    statusBarColor = context.requireColor(android.R.color.transparent)
    navigationBarColor = context.requireColor(android.R.color.transparent)

    artificiallyPadLayout?.setPadding(
        0,
        context.getStatusBarHeight(),
        0,
        context.getNavigationBarHeight()
    )
}

fun Window.disableTouchGestures() {
    setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun Window.enableTouchGestures() {
    clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}