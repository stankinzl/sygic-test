package com.kinzlstanislav.sigyctest.core.nestednavigation

open class NestedNavigation {
    object GenericContent : NestedNavigation()
    object GenericEmpty : NestedNavigation()
    object GenericLoading : NestedNavigation()
    object GenericServerError : NestedNavigation()
    object GenericNoConnectionError : NestedNavigation()
    object GenericError : NestedNavigation()
}