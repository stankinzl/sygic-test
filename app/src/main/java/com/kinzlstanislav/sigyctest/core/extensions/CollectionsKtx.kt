@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

fun <T> List<T>.containsNot(vararg elements: T): Boolean {
    elements.forEach {
        if (contains(it)) return false
    }
    return true
}

fun <T> List<T>.isInBounds(index: Int) = index in 0 until size

fun <T> List<T>?.notNullAndNotEmpty(action: ((List<T>) -> List<T>)? = null): List<T>? {
    return if (this != null && isNotEmpty()) {
        action?.invoke(this)
        this
    } else null
}

fun <T> T.isAnyOf(vararg of: T): Boolean {
    of.forEach {
        if (it == this) return true
    }
    return false
}

fun <T> T.isNotAnyOf(vararg of: T): Boolean {
    of.forEach {
        if (it == this) return false
    }
    return true
}

/**
 * Returns a single list of all elements from all collections in the given collection.
 * @sample samples.collections.Iterables.Operations.flattenIterable
 */
fun <T> Iterable<Iterable<T>>.flatten(): List<T> {
    val result = ArrayList<T>()
    for (element in this) {
        result.addAll(element)
    }
    return result
}

fun <T> Iterable<T>.plusNotDouble(element: T): List<T> {
    return if (contains(element)) this.toList() else this.plus(element)
}

