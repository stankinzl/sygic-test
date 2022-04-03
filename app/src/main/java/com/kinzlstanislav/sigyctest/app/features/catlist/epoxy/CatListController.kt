package com.kinzlstanislav.sigyctest.app.features.catlist.epoxy

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.VisibilityState
import com.kinzlstanislav.sigyctest.R
import com.kinzlstanislav.sigyctest.app.data.Cat
import com.kinzlstanislav.sigyctest.core.epoxy.header
import com.kinzlstanislav.sigyctest.core.epoxy.loader

class CatListController(
    private val context: Context,
    private val onLoaderBecameVisible: () -> Unit,
    private val onCatClickListener: (String) -> Unit
) : AsyncEpoxyController() {

    var data: List<Cat> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        header {
            id("catListHeader")
            headerText(this@CatListController.context.getString(R.string.cats_list_header))
            spanSizeOverride { _, _, _ -> 2 }
        }
        data.forEach {
            cat {
                id(it.databaseKey)
                cat(it)
                onClick {
                    this@CatListController.onCatClickListener.invoke(it)
                }
            }
        }
        loader {
            id("catListLoader")
            onVisibilityStateChanged { _, _, visibilityState ->
                if (visibilityState == VisibilityState.PARTIAL_IMPRESSION_VISIBLE) {
                    this@CatListController.onLoaderBecameVisible.invoke()
                }
            }
            spanSizeOverride { _, _, _ -> 2 }
        }
    }
}