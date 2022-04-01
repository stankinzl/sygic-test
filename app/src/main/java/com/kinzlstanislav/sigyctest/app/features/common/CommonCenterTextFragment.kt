package com.kinzlstanislav.sigyctest.app.features.common

import androidx.navigation.fragment.navArgs
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.base.BaseAppFragment
import com.kinzlstanislav.sigyctest.core.helpers.viewBinding
import com.kinzlstanislav.sigyctest.databinding.FragmentGenericCenterTextBinding

class CommonCenterTextFragment : BaseAppFragment(R.layout.fragment_generic_center_text) {

    private val navArgs by navArgs<CommonCenterTextFragmentArgs>()
    private val binding by viewBinding(FragmentGenericCenterTextBinding::bind)

    override fun onFragmentViewCreated(): Unit = with(binding) {
        centerText.text = navArgs.text
    }
}