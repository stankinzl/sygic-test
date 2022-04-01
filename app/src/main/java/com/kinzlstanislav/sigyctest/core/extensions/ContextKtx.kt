package com.kinzlstanislav.sigyctest.core.extensions

import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import timber.log.Timber

@Suppress("unused")

fun Context.requireDrawable(@DrawableRes resId: Int): Drawable? =
    ContextCompat.getDrawable(this, resId)

fun Context.requireColor(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)
val Context.lifecycleOwner: LifecycleOwner?
    get() {
        var context: Context? = this

        while (context != null && context !is LifecycleOwner) {
            val baseContext = (context as? ContextWrapper?)?.baseContext
            context = if (baseContext == context) null else baseContext
        }

        return if (context is LifecycleOwner) context else null
    }
val processLifecycleOwner get() = ProcessLifecycleOwner.get()

@Deprecated("Use doOnApplyWindowInsets with WindowInsetsCompat.type.navigationBars().bottom to get the height")
fun Context.getStatusBarHeight(): Int {
    return try {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    } catch (e: Exception) {
        Timber.e(e)
        0
    }
}

@Deprecated("Use doOnApplyWindowInsets with WindowInsetsCompat.type.statusBars().bottom to get the height")
fun Context.getNavigationBarHeight(): Int {
    return try {
        val navigationBarId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (navigationBarId > 0) resources.getDimensionPixelSize(navigationBarId) else 0
    } catch (e: Exception) {
        Timber.e(e)
        0
    }
}