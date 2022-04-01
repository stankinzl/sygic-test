package com.kinzlstanislav.sigyctest.app.base

import androidx.annotation.LayoutRes
import com.kinzlstanislav.sigyctest.app.MainViewModel
import com.kinzlstanislav.sigyctest.core.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class BaseAppFragment(@LayoutRes fragmentLayoutRes: Int) : BaseFragment(fragmentLayoutRes) {

    private val mainViewModel by sharedViewModel<MainViewModel>()
}