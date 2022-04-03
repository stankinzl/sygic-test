package com.kinzlstanislav.sigyctest.app.mappers

import com.kinzlstanislav.sigyctest.app.data.Cat
import com.kinzlstanislav.sigyctest.app.network.data.CatResponse
import com.kinzlstanislav.sigyctest.core.base.BaseListMapper
import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class CatsMapper : BaseListMapper<CatResponse?, Cat>{

    override fun map(from: List<CatResponse?>?): List<Cat> = from?.mapNotNull {
        try {
            requireNotNull(it)
            with(requireNotNull(it.breeds?.firstOrNull())) {
                Cat(
                    breedName = name.orEmpty(),
                    origin = origin.orEmpty(),
                    databaseKey = it.id,
                    url = it.url!! // mandatory
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }.orEmpty()
}