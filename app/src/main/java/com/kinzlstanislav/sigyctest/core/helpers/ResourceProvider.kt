package com.kinzlstanislav.sigyctest.core.helpers

import android.content.Context
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class ResourceProvider(private val context: Context) {

    fun getString(@StringRes resId: Int): String = try {
        context.getString(resId)
    } catch (e: Exception) {
        Timber.e(e)
        ""
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: String): String = try {
        context.getString(resId, *formatArgs)
    } catch (e: Exception) {
        Timber.e(e)
        ""
    }

    fun getInteger(@IntegerRes resId: Int) : Int = try {
        context.resources.getInteger(resId)
    } catch (e: Exception) {
        Timber.e(e)
        0
    }
}