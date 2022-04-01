package com.kinzlstanislav.sigyctest.core.helpers

import androidx.annotation.Nullable
import com.squareup.moshi.*
import com.squareup.moshi.JsonAdapter.Factory
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type
import kotlin.jvm.Throws

/**
 * Annotates a DTO variable to automatically convert single {} JSON objects into array.
 * */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class SingleToArray

class SingleToArrayAdapter internal constructor(
    private val delegateAdapter: JsonAdapter<List<Any>>,
    private val elementAdapter: JsonAdapter<Any>
) : JsonAdapter<List<Any>>() {

    @Nullable
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): List<Any> {
        return if (reader.peek() !== JsonReader.Token.BEGIN_ARRAY) {
            try {
                listOf(elementAdapter.fromJson(reader)!!)
            } catch (e: Exception) {
                emptyList()
            }
        } else delegateAdapter.fromJson(reader)!!
    }

    companion object {
        val FACTORY = Factory { type, annotations, moshi ->
            val delegateAnnotations: Set<Annotation?> =
                Types.nextAnnotations(annotations, SingleToArray::class.java) ?: return@Factory null
            require(!(Types.getRawType(type) !== MutableList::class.java)) { "Only lists may be annotated with @SingleToArray. Found: $type" }
            val elementType: Type = Types.collectionElementType(type, MutableList::class.java)
            val delegateAdapter: JsonAdapter<List<Any>> = moshi.adapter(type, delegateAnnotations)
            val elementAdapter = moshi.adapter<Any>(elementType)
            SingleToArrayAdapter(delegateAdapter, elementAdapter)
        }
    }
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: List<Any>?) {
        if (value?.size == 1) {
            elementAdapter.toJson(writer, value[0])
        } else {
            delegateAdapter.toJson(writer, value)
        }
    }
}