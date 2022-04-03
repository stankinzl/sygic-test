package com.kinzlstanislav.sigyctest.app.features.catlist.epoxy

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.data.Cat
import com.kinzlstanislav.sigyctest.core.base.BaseEpoxyModel
import com.kinzlstanislav.sigyctest.core.base.ViewBindingHolder
import com.kinzlstanislav.sigyctest.core.extensions.setTextOrGoneIfEmpty
import com.kinzlstanislav.sigyctest.databinding.ItemCatBinding

@EpoxyModelClass
abstract class CatModel : BaseEpoxyModel() {

    override fun getDefaultLayout() = R.layout.item_cat

    @EpoxyAttribute
    lateinit var cat: Cat

    override fun bind(holder: ViewBindingHolder) {
        super.bind(holder)
        with(ItemCatBinding.bind(holder.view)) {
            catItemOriginTextView.setTextOrGoneIfEmpty(cat.origin)
            catItemSpeciesNameTextView.setTextOrGoneIfEmpty(cat.breedName)
            Glide.with(this.root)
                .load(cat.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop() //4
                .placeholder(R.drawable.ic_app_icon)
                .error(R.drawable.ic_app_icon)
                .fallback(R.drawable.ic_app_icon)
                .into(catItemImageView)
        }
    }
}