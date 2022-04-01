@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

import androidx.constraintlayout.motion.widget.MotionLayout

fun MotionLayout.listenToMotionState(action: (Int) -> Unit) {
    setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            action.invoke(currentId)
        }
    })
}