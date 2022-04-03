package com.kinzlstanislav.sigyctest.app.features.catlist

import androidx.recyclerview.widget.GridLayoutManager
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.base.BaseAppFragment
import com.kinzlstanislav.sigyctest.app.features.catlist.epoxy.CatListController
import com.kinzlstanislav.sigyctest.app.features.catlist.epoxy.CatModel
import com.kinzlstanislav.sigyctest.app.features.catlist.viewmodel.CatListGraphViewModel
import com.kinzlstanislav.sigyctest.core.extensions.collectFlow
import com.kinzlstanislav.sigyctest.core.helpers.viewBinding
import com.kinzlstanislav.sigyctest.databinding.FragmentCatListBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class CatListFragment : BaseAppFragment(R.layout.fragment_cat_list) {

    private val binding by viewBinding(FragmentCatListBinding::bind)
    private val graphViewModel by koinNavGraphViewModel<CatListGraphViewModel>(R.id.catListNestedGraph)

    override fun onFragmentViewCreated(): Unit = with(binding) {
        val listController = CatListController(requireContext())
        (catList.layoutManager as GridLayoutManager).spanSizeLookup = listController.spanSizeLookup
        catList.setController(listController)
        collectFlow(graphViewModel.catsFlow, viewLifecycleOwner) {
            listController.data = it
        }
    }
}