package com.kinzlstanislav.sigyctest.app.features.catlist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.core.base.BaseDialogFragment
import com.kinzlstanislav.sigyctest.databinding.FragmentCatImageDetailDialogBinding

class CatImageDetailDialogFragment : BaseDialogFragment(R.layout.fragment_cat_image_detail_dialog) {

    private val navArgs by navArgs<CatImageDetailDialogFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCatImageDetailDialogBinding.bind(view)
        Glide.with(binding.root)
            .load(navArgs.imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
            .into(binding.catItemImageView)
    }
}