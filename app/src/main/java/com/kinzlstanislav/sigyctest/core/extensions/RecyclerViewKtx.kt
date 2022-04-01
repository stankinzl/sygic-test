package com.kinzlstanislav.sigyctest.core.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.smoothScrollToPositionSnapStart(position: Int) {
    (layoutManager as? LinearLayoutManager)?.startSmoothScroll(object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }.apply {
        targetPosition = 0
    })
}