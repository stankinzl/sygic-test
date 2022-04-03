package com.kinzlstanislav.sigyctest.app.features.catlist.epoxy

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.data.Cat
import com.kinzlstanislav.sigyctest.core.epoxy.header

class CatListController(private val context: Context) : AsyncEpoxyController() {

    var data: List<Cat> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        header {
            id("catListHeader")
            headerText(this@CatListController.context.getString(R.string.cats_list_header))
            spanSizeOverride { _, _, _ ->
                2
            }
        }
        data.forEach {
            cat {
                id(it.databaseKey)
                cat(it)
            }
        }
    }
}