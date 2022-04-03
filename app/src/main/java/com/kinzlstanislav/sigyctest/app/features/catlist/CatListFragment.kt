package com.kinzlstanislav.sigyctest.app.features.catlist

import android.annotation.SuppressLint
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.base.BaseAppFragment
import com.kinzlstanislav.sigyctest.app.features.catlist.epoxy.CatListController
import com.kinzlstanislav.sigyctest.app.features.catlist.viewmodel.CatListGraphViewModel
import com.kinzlstanislav.sigyctest.core.extensions.collectFlow
import com.kinzlstanislav.sigyctest.core.helpers.viewBinding
import com.kinzlstanislav.sigyctest.databinding.FragmentCatListBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class CatListFragment : BaseAppFragment(R.layout.fragment_cat_list) {

    private companion object {
        const val PERCENT_OF_LOADER_VIEW_TO_START_PAGING = 10
    }
    private val binding by viewBinding(FragmentCatListBinding::bind)
    private val graphViewModel by koinNavGraphViewModel<CatListGraphViewModel>(R.id.catListNestedGraph)

    @SuppressLint("Range")
    override fun onFragmentViewCreated(): Unit = with(binding.catList) {
        val listController = CatListController(
            requireContext(),
            onLoaderBecameVisible = {
                graphViewModel.fetchNextCatPage()
            }
        )
        (layoutManager as GridLayoutManager).spanSizeLookup = listController.spanSizeLookup
        setController(listController)
        EpoxyVisibilityTracker().apply {
            partialImpressionThresholdPercentage = PERCENT_OF_LOADER_VIEW_TO_START_PAGING
        }.attach(this)
        collectFlow(graphViewModel.catsFlow, viewLifecycleOwner) {
            listController.data = it
        }
        scaleX = 0.8f
        scaleY = 0.8f
        animate()
            .scaleX(1f)
            .scaleY(1f)
            .start()
    }
}