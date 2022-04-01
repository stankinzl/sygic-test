@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

fun String.asJsonRequestBody(): RequestBody =
    RequestBody.create(
        "application/json".toMediaTypeOrNull(), this.trimIndent()
        .removeNewlines())

fun String.asRequestBody(): RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), this)
