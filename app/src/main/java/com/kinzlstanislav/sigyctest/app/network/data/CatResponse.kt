package com.kinzlstanislav.sigyctest.app.network.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kpstv.bindings.AutoGenerateListConverter
import com.kpstv.bindings.ConverterType

@Entity
@TypeConverters(CatBreedResponseListConverter::class)
data class CatResponse(
    @PrimaryKey val id: String,
    val breeds: List<CatBreedResponse>? = null,
    val url: String? = null
)

@kotlinx.serialization.Serializable
@AutoGenerateListConverter(using = ConverterType.KOTLIN_SERIALIZATION)
data class CatBreedResponse(
    val name: String? = null
)