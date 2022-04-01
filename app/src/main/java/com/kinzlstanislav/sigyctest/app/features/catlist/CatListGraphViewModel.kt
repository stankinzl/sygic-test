package com.kinzlstanislav.sigyctest.app.features.catlist

import com.kinzlstanislav.sigyctest.core.nestednavigation.BaseNestedGraphViewModel
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigation
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigation.*
import com.kinzlstanislav.sigyctest.core.extensions.coroutine
import com.kinzlstanislav.sigyctest.core.extensions.repeatOnFlow
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class CatListGraphViewModel : BaseNestedGraphViewModel<NestedNavigation>() {

    init {
        coroutine {
            var navigatingIndex = 0
            repeatOnFlow(every = 1.seconds) {
                _nestedNavigationFlow.emit(when(navigatingIndex) {
                    0 -> GenericLoading
                    1 -> GenericTextContent("Network Error")
                    2 -> GenericTextContent("Generic Error")
                    3 -> GenericTextContent("Empty")
                    else -> GenericContent
                })
                navigatingIndex += 1
                if (navigatingIndex == 5) {
                    navigatingIndex = 0
                }
            }
        }
    }
}