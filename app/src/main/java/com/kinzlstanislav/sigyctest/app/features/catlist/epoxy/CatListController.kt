package com.kinzlstanislav.sigyctest.app.features.catlist.epoxy

import com.airbnb.epoxy.AsyncEpoxyController
import com.kinzlstanislav.sigyctest.app.data.Cat

class CatListController : AsyncEpoxyController() {

    var data: List<Cat> = emptyList()
        set(value) {
            field = value
            buildModels()
        }

    override fun buildModels() {

    }
}