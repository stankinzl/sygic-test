@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import com.kinzlstanislav.sigyctest.core.base.BaseFragment
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import timber.log.Timber

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, length).show()
}

fun View.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, length).show()
}

fun Activity.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Fragment.hideKeyboard() {
    val inputManager =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
}

fun BaseFragment.navigate(directions: NavDirections) {
    try {
        findNavController().navigate(directions)
    } catch (e: IllegalArgumentException) {
        // if user frantically navigates forth and back between destinations this might occur, we don't want to crash the app in such case
    }
}

fun BaseFragment.navigateUp() {
    findNavController().navigateUp()
}


fun BaseFragment.listenToKeyboardOpened(callback: (Boolean, Int) -> Unit) {
    listenToKeyboardOpened(requireActivity(), callback)

}

fun DialogFragment.listenToKeyboardOpened(callback: (Boolean, Int) -> Unit) {
    listenToKeyboardOpened(requireActivity(), callback)
}

private fun listenToKeyboardOpened(activity: Activity, callback: (Boolean, Int) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(activity.findViewById(android.R.id.content)) { _, insets ->
        val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
        val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        callback.invoke(imeVisible, imeHeight)
        insets
    }
}

/** For now it is highly recommended to call enableTouchGestures in onDetach() to avoid app freeze unintentionally */
fun BaseFragment.disableTouchGestures() {
    requireActivity().disableTouchGestures()
}

fun BaseFragment.enableTouchGestures() {
    requireActivity().enableTouchGestures()
}

@Throws(Exception::class)
fun BaseFragment.disableWidget(widgetProviderClass: Class<*>) {
    val applicationContext = requireActivity().applicationContext
    applicationContext.packageManager.setComponentEnabledSetting(
        ComponentName(applicationContext, widgetProviderClass),
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
        PackageManager.DONT_KILL_APP
    )
}

@Throws(Exception::class)
fun BaseFragment.enableWidget(widgetProviderClass: Class<*>) {
    val applicationContext = requireActivity().applicationContext
    applicationContext.packageManager.setComponentEnabledSetting(
        ComponentName(applicationContext, widgetProviderClass),
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}

fun Fragment.delaySafely(delay: Int, action: () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenResumed {
        delay(delay.toLong())
        action()
    }
}

fun BaseFragment.getShortAnimTime200() =
    requireContext().resources.getInteger(android.R.integer.config_shortAnimTime)

fun BaseFragment.getMediumAnimTime400() =
    requireContext().resources.getInteger(android.R.integer.config_mediumAnimTime)

fun BaseFragment.getLongAnimTime500() =
    requireContext().resources.getInteger(android.R.integer.config_longAnimTime)

inline fun <reified T : ViewModel> Fragment.getNavGraphViewModel(
    @IdRes navGraphId: Int,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T? {
    try {
        val navBackStackEntry = findNavController().getBackStackEntry(navGraphId)

        require(navBackStackEntry.destination is NavGraph) {
            ("No NavGraph with ID $navGraphId is on the NavController's back stack")
        }
        return GlobalContext.get().getViewModel(
            qualifier,
            { ViewModelOwner.from(navBackStackEntry, navBackStackEntry) },
            T::class,
            parameters
        )
    } catch (e: Exception) {
        Timber.e(e)
        return null
    }
}