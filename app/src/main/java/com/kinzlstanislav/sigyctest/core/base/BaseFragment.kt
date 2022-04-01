package com.kinzlstanislav.sigyctest.core.base

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.kinzlstanislav.sigyctest.core.extensions.enableTouchGestures

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    open var customBackButtonAction: (() -> Unit)? = null
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentViewCreated()
    }

    override fun onResume() {
        super.onResume()
        customBackButtonAction?.let {
            onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    it.invoke()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner, onBackPressedCallback!! // this will never be null ^
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // https://stackoverflow.com/questions/57512327/navigation-component-illegalstateexception-fragment-not-associated-with-a-fragm
        onBackPressedCallback = null
        onBackPressedCallback?.remove()
    }

    open fun onFragmentViewCreated() {}

    override fun onDetach() {
        super.onDetach()
        enableTouchGestures()
    }
}