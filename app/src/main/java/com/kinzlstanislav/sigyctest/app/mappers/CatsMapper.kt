package com.kinzlstanislav.sigyctest.app.mappers

import com.kinzlstanislav.sigyctest.app.data.Cat
import com.kinzlstanislav.sigyctest.app.network.data.CatResponse
import com.kinzlstanislav.sigyctest.core.base.BaseListMapper
import org.koin.core.annotation.Factory

@Factory
class CatsMapper : BaseListMapper<CatResponse?, Cat>{

    override fun map(from: List<CatResponse?>?): List<Cat> = from?.mapNotNull {
        Cat(breed = "Some breed")
    }.orEmpty()
}