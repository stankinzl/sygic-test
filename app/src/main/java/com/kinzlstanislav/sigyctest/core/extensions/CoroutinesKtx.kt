@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.kinzlstanislav.sigyctest.core.helpers.AndroidUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * It is essential that coroutines run on IO as running them on Main would most likely
 * result in lags. Exceptionally Main thread can be used if the need for more
 * coroutines arises.
 * */
fun ViewModel.coroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(dispatcher) {
    block()
    cancel()
}

fun ViewModel.uiCoroutine(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(dispatcher) {
    block()
    cancel()
}

suspend inline fun <T> suspendCancellableCoroutineWithTimeout(
    timeoutMs: Int,
    crossinline block: (CancellableContinuation<T>) -> Unit
) = withTimeout(timeoutMs.toLong()) {
    suspendCancellableCoroutine(block)
}

fun <T> CoroutineScope.catchingAsync(block: suspend CoroutineScope.() -> T?): Deferred<T?> =
    async(context = coroutineContext) {
        try {
            return@async block.invoke(this)
        } catch (e: Exception) {
            Timber.e(e)
            return@async null
        }
    }

suspend fun Job?.awaitCompletionIfActive() {
    if (this == null) return
    suspendCancellableCoroutine<Boolean> { continuation ->
        if (isActive) {
            invokeOnCompletion {
                continuation.resumeWith(Result.success(true))
            }
        } else {
            continuation.resumeWith(Result.success(true))
        }
    }
}


fun View.viewCoroutine(
    block: suspend CoroutineScope.() -> Unit
): Job? = context.lifecycleOwner?.lifecycleScope?.launch {
    block()
    cancel()
}

fun Fragment.fragmentCoroutine(
    block: suspend CoroutineScope.() -> Unit
): Job = viewLifecycleOwner.lifecycleScope.launch {
    block()
    cancel()
}

fun Activity.activityCoroutine(
    block: suspend CoroutineScope.() -> Unit
): Job? = lifecycleOwner?.lifecycleScope?.launch {
    block()
    cancel()
}

suspend fun RecyclerView.awaitScrollEnd() {
    // If a smooth scroll has just been started, it won't actually start until the next
    // animation frame, so we'll await that first
    awaitAnimationFrame()
    // Now we can check if we're actually idle. If so, return now
    if (scrollState == RecyclerView.SCROLL_STATE_IDLE) return

    suspendCancellableCoroutine<Boolean> { continuation ->
        val onScrollChangedListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Make sure we remove the listener so we don't leak the
                    // coroutine continuation
                    recyclerView.removeOnScrollListener(this)
                    // Finally, resume the coroutine
                    continuation.resumeWith(Result.success(true))
                }
            }
        }
        continuation.invokeOnCancellation {
            // If the coroutine is cancelled, remove the scroll listener
            removeOnScrollListener(onScrollChangedListener)
            // We could also stop the scroll here if desired
        }

        addOnScrollListener(onScrollChangedListener)
    }
}

suspend fun View.awaitAnimationFrame() = suspendCancellableCoroutine<Boolean> { continuation ->
    val runnable = Runnable {
        continuation.resumeWith(Result.success(true))
    }
    // If the coroutine is cancelled, remove the callback
    continuation.invokeOnCancellation { removeCallbacks(runnable) }
    // And finally post the runnable
    postOnAnimation(runnable)
}


suspend fun View.fadeOutSuspended(dur: Int = 300, fadeOutValue: Float = 0f) {
    // not using withEndAction as it causes crashes if user exitted the fragment meanwhile
    ObjectAnimator.ofFloat(this, View.ALPHA, alpha, fadeOutValue).run {
        // 1st run will last 150ms, 2nd: 300ms, 3rd: 450ms
        duration = dur.toLong()
        start()
        awaitEnd()
    }
}

suspend fun View.fadeInSuspended(dur: Int = 300, fadeInValue: Float = 1f) {
    ObjectAnimator.ofFloat(this, View.ALPHA, alpha, fadeInValue).run {
        // 1st run will last 150ms, 2nd: 300ms, 3rd: 450ms
        duration = dur.toLong()
        start()
        awaitEnd()
    }
}

suspend fun Animator.awaitEnd() = suspendCancellableCoroutine<Boolean> { cont ->
    // Add an invokeOnCancellation listener. If the coroutine is
    // cancelled, cancel the animation too that will notify
    // listener's onAnimationCancel() function
    cont.invokeOnCancellation { cancel() }

    addListener(object : AnimatorListenerAdapter() {
        private var endedSuccessfully = true

        override fun onAnimationCancel(animation: Animator) {
            // Animator has been cancelled, so flip the success flag
            endedSuccessfully = false
        }

        override fun onAnimationEnd(animation: Animator) {
            // Make sure we remove the listener so we don't keep
            // leak the coroutine continuation
            animation.removeListener(this)

            if (cont.isActive) {
                // If the coroutine is still active...
                if (endedSuccessfully) {
                    // ...and the Animator ended successfully, resume the coroutine
                    cont.resumeWith(Result.success(true))
                } else {
                    // ...and the Animator was cancelled, cancel the coroutine too
                    cont.cancel()
                }
            }
        }
    })
}


suspend fun waitForAppToBeIdle() {
    // Need to add timeout as some animations prevent main looper from going to idle state
    // ... TODO: Investigate how to prevent this
    suspendCancellableCoroutineWithTimeout<Boolean>(timeoutMs = 2000) { continuation ->
        AndroidUtils.doWhenIdle {
            continuation.resumeWith(Result.success(true))
        }
    }
}

suspend fun View.waitForViewToPreDraw() {
    suspendCancellableCoroutineWithTimeout<Boolean>(timeoutMs = 2000) { continuation ->
        doOnPreDraw {
            continuation.resumeWith(Result.success(true))
        }
    }
}

suspend fun View.waitForViewToLayout() {
    suspendCancellableCoroutineWithTimeout<Boolean>(timeoutMs = 2000) { continuation ->
        doOnLayout {
            continuation.resumeWith(Result.success(true))
        }
    }
}

suspend fun View.postSuspended() {
    suspendCancellableCoroutineWithTimeout<Boolean>(timeoutMs = 2000) { continuation ->
        post {
            continuation.resumeWith(Result.success(true))
        }
    }
}

// https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda
fun <F> collectFlow(flow: Flow<F>, lifecycleOwner: LifecycleOwner, onCollected: (F) -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest {
                onCollected.invoke(it)
            }
        }
    }
}

/**
 * Use inside a coroutine scope.
 * @sample
 * repeatOnFlow(every = 10.seconds) {
 *      // Do something...
 * }
 * */
@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(ExperimentalTime::class)
suspend fun CoroutineScope.repeatOnFlow(every: Duration, repeat: suspend CoroutineScope.() -> Unit) {
    flow {
        while(true) {
            delay(every)
            emit(true)
        }
    }.collect {
        coroutineScope { repeat() }
    }
}