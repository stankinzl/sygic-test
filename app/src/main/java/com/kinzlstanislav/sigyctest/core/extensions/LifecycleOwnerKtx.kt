package com.kinzlstanislav.sigyctest.core.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> Fragment.observe(liveData: LiveData<T>, onObserve: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) {
        it?.let {
            requireActivity().runOnUiThread {
                onObserve(it)
            }
        }
    }
}

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, onObserve: (T) -> Unit) {
    liveData.observe(this) {
        it?.let {
            onObserve(it)
        }
    }
}