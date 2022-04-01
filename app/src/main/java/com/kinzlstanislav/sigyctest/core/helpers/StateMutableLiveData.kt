package com.kinzlstanislav.sigyctest.core.helpers

import androidx.lifecycle.MutableLiveData

// https://stackoverflow.com/questions/54639282/how-to-emit-distinct-values-from-mutablelivedata
/**
 * Only emits different values.
 * */
class StateMutableLiveData<T>(value: T? = null) : MutableLiveData<T>(value) {
    override fun setValue(value: T?) {
        if (value != this.value) {
            super.setValue(value)
        }
    }
}