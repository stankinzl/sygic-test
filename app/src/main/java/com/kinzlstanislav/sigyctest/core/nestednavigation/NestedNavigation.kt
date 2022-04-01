package com.kinzlstanislav.sigyctest.core.nestednavigation

open class NestedNavigation {
    object GenericContent : NestedNavigation()
    data class GenericTextContent(val text: String) : NestedNavigation()
    object GenericEmpty : NestedNavigation()
    object GenericLoading : NestedNavigation()
}