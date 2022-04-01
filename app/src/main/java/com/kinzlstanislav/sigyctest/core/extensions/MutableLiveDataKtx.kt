package com.kinzlstanislav.sigyctest.core.extensions

import androidx.lifecycle.MutableLiveData
import java.lang.IllegalStateException

fun <T> MutableLiveData<T>.emit(state: T) {
    try {
        value = state
    } catch (e: IllegalStateException) {
        postValue(state)
    }
}