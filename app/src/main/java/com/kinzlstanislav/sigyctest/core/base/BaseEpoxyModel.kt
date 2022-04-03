package com.kinzlstanislav.sigyctest.core.base

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder

abstract class BaseEpoxyModel : EpoxyModelWithHolder<ViewBindingHolder>()

class ViewBindingHolder : EpoxyHolder() {

    lateinit var view: View

    public override fun bindView(itemView: View) {
        view = itemView
    }
}