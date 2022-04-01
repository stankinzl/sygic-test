@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.kinzlstanislav.sigyctest.core.helpers

import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import timber.log.Timber

object AndroidUtils {

    fun doWhenIdle(action: () -> Unit) {
        Looper.myQueue().addIdleHandler {
            try {
                action()
            } catch (e: Exception) {
                Timber.e(e)
            }
            false
        }
    }

    fun makeThoseVisible(vararg views: View?) {
        val nonNullViews = views.filterNotNull()
        setVisibility(true, *nonNullViews.toTypedArray())
    }

    fun makeThoseGone(vararg views: View?) {
        val nonNullViews = views.filterNotNull()
        setVisibility(false, *nonNullViews.toTypedArray())
    }

    fun setVisibility(visible: Boolean, vararg views: View) {
        views.forEach {
            it.isVisible = visible
        }
    }
}