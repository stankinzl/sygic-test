package com.kinzlstanislav.sigyctest.app.features.catlist

import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.base.BaseAppFragment
import com.kinzlstanislav.sigyctest.app.features.catlist.viewmodel.CatListGraphViewModel
import com.kinzlstanislav.sigyctest.core.extensions.collectFlow
import com.kinzlstanislav.sigyctest.core.helpers.viewBinding
import com.kinzlstanislav.sigyctest.databinding.FragmentCatListBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class CatListDetailFragment : BaseAppFragment(R.layout.fragment_cat_list) {

    private val binding by viewBinding(FragmentCatListBinding::bind)
    private val graphViewModel by koinNavGraphViewModel<CatListGraphViewModel>(R.id.catListNestedGraph)

    override fun onFragmentViewCreated(): Unit = with(binding) {
        collectFlow(graphViewModel.catsFlow, viewLifecycleOwner) {

        }
    }
}