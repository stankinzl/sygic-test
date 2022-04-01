package com.kinzlstanislav.sigyctest.core.extensions

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kinzlstanislav.sigyctest.App

fun App.registerActivityLifecycleCallbacksKtx(
    onActivityCreated: ((Activity, Bundle?) -> Unit)? = null,
    onActivityStarted: ((Activity) -> Unit)? = null,
    onActivityResumed: ((Activity) -> Unit)? = null,
    onActivityPaused: ((Activity) -> Unit)? = null,
    onActivityStopped: ((Activity) -> Unit)? = null,
    onActivitySaveInstanceState: ((Activity, Bundle) -> Unit)? = null,
    onActivityDestroyed: ((Activity) -> Unit)? = null
) {
    registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(a: Activity, p1: Bundle?) {
            onActivityCreated?.let { it(a, p1) }
        }

        override fun onActivityStarted(a: Activity) {
            onActivityStarted?.let { it(a) }
        }

        override fun onActivityResumed(a: Activity) {
            onActivityResumed?.let { it(a) }
        }

        override fun onActivityPaused(a: Activity) {
            onActivityPaused?.let { it(a) }
        }

        override fun onActivityStopped(a: Activity) {
            onActivityStopped?.let { it(a) }
        }

        override fun onActivitySaveInstanceState(a: Activity, p1: Bundle) {
            onActivitySaveInstanceState?.let { it(a, p1) }
        }

        override fun onActivityDestroyed(a: Activity) {
            onActivityDestroyed?.let { it(a) }
        }
    })
}