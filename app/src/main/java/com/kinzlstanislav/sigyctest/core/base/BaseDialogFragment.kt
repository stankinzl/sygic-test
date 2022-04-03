package com.kinzlstanislav.sigyctest.core.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment(@LayoutRes private val layoutRes: Int) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        with(dialog) {
            val view = View.inflate(requireContext(), layoutRes, null)
            setContentView(view)
        }
        return dialog
    }
}