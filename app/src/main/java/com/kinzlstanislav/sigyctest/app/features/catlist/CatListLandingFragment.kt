package com.kinzlstanislav.sigyctest.app.features.catlist

import android.os.Bundle
import com.kinzlstanislav.sigyctest.CatListNestedGraphDirections.actionCatListGlobalToCenterTextFragment
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.base.BaseAppFragment
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigation.*
import com.kinzlstanislav.sigyctest.core.nestednavigation.NestedNavigationRouter
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel

class CatListLandingFragment : BaseAppFragment(R.layout.fragment_empty) {

    private val graphViewModel by koinNavGraphViewModel<CatListGraphViewModel>(R.id.catListNestedGraph)
    private val nestedNavigationRouter by inject<NestedNavigationRouter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nestedNavigationRouter.registerNestedNavigation(graphViewModel) {
            with(nestedNavigationRouter) {
                when (it) {
                    GenericLoading -> nestedNavigate(R.id.catListLoadingFragment)
                    is GenericTextContent -> {
                        nestedNavigate(
                            actionCatListGlobalToCenterTextFragment(
                                it.text
                            )
                        )
                    }
                    is GenericContent -> nestedNavigate(R.id.catListFragment)
                    is GenericLoading -> nestedNavigate(R.id.catListLoadingFragment)
                }
            }
        }
    }
}