package com.kinzlstanislav.sigyctest.core.epoxy

import com.airbnb.epoxy.EpoxyModelClass
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.core.base.BaseEpoxyModel

@EpoxyModelClass
abstract class LoaderModel : BaseEpoxyModel() {

    override fun getDefaultLayout() = R.layout.item_loader
}