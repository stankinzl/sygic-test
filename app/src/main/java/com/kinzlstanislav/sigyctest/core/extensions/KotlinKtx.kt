@file:Suppress("unused", "UNCHECKED_CAST")

package com.kinzlstanislav.sigyctest.core.extensions

fun tryEmptyCatch(catchAction: () -> Unit = {}, tryAction: () -> Unit) {
    try {
        tryAction()
    } catch (e: Exception) {
        catchAction()
    }
}

fun <E : Exception> tryCatch(catchAction: (E) -> Unit = {}, tryAction: () -> Unit) {
    try {
        tryAction()
    } catch (e: Exception) {
        (e as? E)?.let {
            catchAction(e)
        }
    }
}