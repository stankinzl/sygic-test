package com.kinzlstanislav.sigyctest.core.epoxy

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.core.base.BaseEpoxyModel
import com.kinzlstanislav.sigyctest.core.base.ViewBindingHolder
import com.kinzlstanislav.sigyctest.core.extensions.setTextOrGoneIfEmpty
import com.kinzlstanislav.sigyctest.databinding.ItemHeaderBinding

@EpoxyModelClass
abstract class HeaderModel : BaseEpoxyModel() {

    override fun getDefaultLayout() = R.layout.item_header

    @EpoxyAttribute
    open var headerText: String? = null

    override fun bind(holder: ViewBindingHolder) {
        super.bind(holder)
        with(ItemHeaderBinding.bind(holder.view)) {
            headerTextView.setTextOrGoneIfEmpty(headerText)
        }
    }
}