package com.kinzlstanislav.sigyctest.core.helpers

import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

fun <F : Fragment> tag(classReference: KClass<F>): String {
    return classReference.simpleName!!
}