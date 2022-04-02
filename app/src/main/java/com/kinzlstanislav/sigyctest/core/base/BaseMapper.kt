package com.kinzlstanislav.sigyctest.core.base

interface BaseMapper <F, T>{
    fun map(from: F): T
}