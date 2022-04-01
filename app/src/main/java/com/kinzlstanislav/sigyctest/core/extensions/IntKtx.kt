@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

import android.content.res.Resources
import kotlin.math.roundToInt

val Int.px: Int
    get() = (this / Resources.getSystem().displayMetrics.density).roundToInt()
val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.butNotLessThan(value: Int): Int = if (this < value) value else this
fun Float.butNotLessThan(value: Float): Float = if (this < value) value else this
fun Int.butNotMoreThan(value: Int): Int = if (this > value) value else this