package com.kinzlstanislav.sigyctest.core.nestednavigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.core.extensions.processLifecycleOwner
import com.kinzlstanislav.sigyctest.core.extensions.waitForAppToBeIdle
import com.kinzlstanislav.sigyctest.core.helpers.AndroidUtils.doWhenIdle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class NestedNavigationRouter {

    fun attachNavigationController(navController: NavController) {
        currentNavController = navController
    }

    private var currentNavController: NavController? = null
    private val currentDestinationId get() = currentNavController?.currentDestination?.id

    private val defaultNestedNavigationOptions
        get() =
            NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(currentDestinationId!!, inclusive = true)
                .setEnterAnim(R.anim.tc_fragment_fade_enter)
                .setExitAnim(R.anim.tc_fragment_fade_exit)
                .build()

    /**
     * @param useDefaultNavigationOptions specify as false
     * if you want to use navigation options from xml
     * */
    @OptIn(DelicateCoroutinesApi::class)
    fun nestedNavigate(
        directions: NavDirections,
        useDefaultNavigationOptions: Boolean = true
    ) {
        try {
            Timber.d("LOLO: Attempting navigation")
            doWhenIdle {
                val navController = requireNotNull(currentNavController)
                Timber.d("LOLO: Didnt idle")
                if (useDefaultNavigationOptions) {
                    navController.navigate(directions, defaultNestedNavigationOptions)
                } else {
                    navController.navigate(directions)
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
            throw CancellationException()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun nestedNavigate(
        @IdRes resId: Int,
        args: Bundle? = null,
        customOptions: NavOptions? = null,
        skipIfIsAlreadyInDestination: Boolean = true
    ) {
        try {
            doWhenIdle {
                val navController = requireNotNull(currentNavController)
                if (skipIfIsAlreadyInDestination) {
                    if (navController.currentDestination?.id == resId) return@doWhenIdle
                }
                navController.navigate(
                    resId,
                    args,
                    customOptions ?: defaultNestedNavigationOptions
                )
            }
        } catch (e: Exception) {
            // Could be something like... java.lang.IllegalArgumentException: Navigation action/destination
            // com.kt.tourleader.staging:id/homeFragmentTourListKT cannot be found from the current destination
            Timber.e(e)
            throw CancellationException()
        }
    }

    fun <N : NestedNavigation> registerNestedNavigationFlow(
        nestedGraphViewModel: BaseNestedGraphViewModel<N>,
        onCollected: NestedNavigationRouter.(N) -> Unit
    ) {
        processLifecycleOwner.lifecycleScope.launch {
            processLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                nestedGraphViewModel.nestedNavigationFlow.collectLatest {
                    onCollected.invoke(this@NestedNavigationRouter, it)
                }
                nestedGraphViewModel.onClearedFlow.collectLatest {
                    if (it) {
                        cancel("Navigation graph ended")
                    }
                }
            }
        }
    }
}