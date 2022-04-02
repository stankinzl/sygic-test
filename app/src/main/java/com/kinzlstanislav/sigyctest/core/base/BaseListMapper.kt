package com.kinzlstanislav.sigyctest.core.base

interface BaseListMapper<F, T>{
    fun map(from: List<F>?): List<T>
}