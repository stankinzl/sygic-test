package com.kinzlstanislav.sigyctest.core.extensions

import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun Window.drawUiUnderSystemBars(artificiallyPadLayout: ViewGroup? = null) {
    WindowCompat.setDecorFitsSystemWindows(this, false)
    artificiallyPadLayout?.doOnApplyWindowInsets { insetView, windowInsets, initialPadding, _ ->
        insetView.updatePadding(
            left = initialPadding.left + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
            top = initialPadding.top + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
            right = initialPadding.right + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
            bottom = initialPadding.bottom + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
        )
    }
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