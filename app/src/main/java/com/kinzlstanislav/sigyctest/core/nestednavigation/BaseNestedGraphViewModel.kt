package com.kinzlstanislav.sigyctest.core.nestednavigation

import androidx.lifecycle.ViewModel
import com.kinzlstanislav.sigyctest.core.extensions.coroutine
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Used for navigation controls withing a scoped ViewModel of navigation component nested graph
 * Has no one Fragment, as it controls the navigation within the nested graph, it requires
 * a current reference to navigation controller even after configuration change.
 * @warn WARNING! Use launchSingleTop on fragments inside nested graph only when you want to kill the VM.
 * */
open class BaseNestedGraphViewModel<T : NestedNavigation> : ViewModel() {
    protected val _nestedNavigationFlow = MutableSharedFlow<T>()
    val nestedNavigationFlow: SharedFlow<T> = _nestedNavigationFlow
    private val _onClearedFlow = MutableSharedFlow<Boolean>()
    val onClearedFlow: SharedFlow<Boolean> = _onClearedFlow
    override fun onCleared() {
        coroutine {
            _onClearedFlow.tryEmit(true)
        }
        super.onCleared()
    }
}