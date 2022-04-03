package com.kinzlstanislav.sigyctest.app.features.catlist

import android.os.Bundle
import com.kinzlstanislav.sigyctest.CatListNestedGraphDirections.actionCatListGlobalToCenterTextFragment
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.base.BaseAppFragment
import com.kinzlstanislav.sigyctest.app.features.catlist.viewmodel.CatListGraphViewModel
import com.kinzlstanislav.sigyctest.app.features.catlist.viewmodel.CatListGraphViewModel.NestedNavigationNoCats
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigation.*
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigationRouter
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel

class CatListLandingFragment : BaseAppFragment(R.layout.fragment_empty) {

    private val graphViewModel by koinNavGraphViewModel<CatListGraphViewModel>(R.id.catListNestedGraph)
    private val nestedNavigationRouter by inject<NestedNavigationRouter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nestedNavigationRouter.registerNestedNavigationFlow(graphViewModel) {
            when (it) {
                GenericLoading -> nestedNavigate(R.id.catListLoadingFragment)
                NestedNavigationNoCats, GenericServerError,
                GenericError, GenericNoConnectionError -> {
                    val text = when (it) {
                        NestedNavigationNoCats -> getString(R.string.no_cats)
                        GenericServerError -> getString(R.string.error_server)
                        GenericError -> getString(R.string.error_unknown)
                        else -> getString(R.string.error_no_internet)
                    }
                    nestedNavigate(actionCatListGlobalToCenterTextFragment(text))
                }
                GenericContent -> nestedNavigate(R.id.catListFragment)
            }
        }
    }
}