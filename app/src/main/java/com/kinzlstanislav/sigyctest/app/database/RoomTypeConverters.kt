package com.kinzlstanislav.sigyctest.app.database

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object RoomTypeConverters : KoinComponent {

    private val moshi by inject<Moshi>()

    private inline fun <reified T> adapter() = moshi.adapter(T::class.java)
    private inline fun <reified T> listAdapter() =
        moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))

    private inline fun <reified T> from(response: T?): String? = adapter<T>().toJson(response)
    private inline fun <reified T> fromList(list: List<T>?): String? = listAdapter<T>().toJson(list)
    private inline fun <reified T> to(json: String?) = json?.let {
        adapter<T>().fromJson(json)
    }

    private inline fun <reified T> toList(json: String?) =
        json?.let { listAdapter<T>().fromJson(it) }.orEmpty()

    @TypeConverter
    fun fromStringList(list: List<String>?) = fromList(list)

    @TypeConverter
    fun toStringList(json: String) = toList<String?>(json)

    /**
    For Room entities / data classes use
    @AutoGenerateConverter(using = ConverterType.KOTLIN_SERIALIZATION)
    and generate the adapter
     * */
}