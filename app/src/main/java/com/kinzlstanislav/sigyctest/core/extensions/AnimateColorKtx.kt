@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment

fun animateColorWithFractionReturn(
    @ColorInt colorStart: Int,
    @ColorInt colorEnd: Int,
    onEnd: (() -> Unit)? = null,
    animDur: Int = 250,
    onColorUpdate: (Int, Float) -> Unit
): Animator = ValueAnimator.ofArgb(colorStart, colorEnd).apply {
    duration = animDur.toLong()
    addUpdateListener {
        onColorUpdate(it.animatedValue as Int, it.animatedFraction)
    }
    onEnd?.let { onEndAction ->
        doOnEnd {
            onEndAction.invoke()
        }
    }
    start()
}

fun animateColor(
    @ColorInt colorStart: Int,
    @ColorInt colorEnd: Int,
    animDur: Int = 250,
    onColorUpdate: (Int) -> Unit
) = finishAnimateColors(colorStart, colorEnd, animDur, onColorUpdate)

fun View.animateColorRes(
    @ColorRes colorStartRes: Int,
    @ColorRes colorEndRes: Int,
    animDur: Int = 250,
    onColorUpdate: (Int) -> Unit
) = animateColorPrivateRes(context, colorStartRes, colorEndRes, animDur, onColorUpdate)

fun Fragment.animateColorRes(
    @ColorRes colorStartRes: Int,
    @ColorRes colorEndRes: Int,
    animDur: Int = 250,
    onColorUpdate: (Int) -> Unit
) = animateColorPrivateRes(
    requireContext(),
    colorStartRes,
    colorEndRes,
    animDur,
    onColorUpdate
)

private fun animateColorPrivateRes(
    context: Context,
    @ColorRes colorStartRes: Int,
    @ColorRes colorEndRes: Int,
    animDur: Int = 250,
    onColorUpdate: (Int) -> Unit
) = finishAnimateColors(
    context.requireColor(colorStartRes),
    context.requireColor(colorEndRes),
    animDur,
    onColorUpdate
)

private fun finishAnimateColors(
    @ColorInt colorStart: Int,
    @ColorInt colorEnd: Int,
    animDur: Int = 250,
    onColorUpdate: (Int) -> Unit
): Animator = ValueAnimator.ofArgb(colorStart, colorEnd).apply {
    duration = animDur.toLong()
    addUpdateListener {
        onColorUpdate(it.animatedValue as Int)
    }
    start()
}