package com.kinzlstanislav.sigyctest.app.network

import com.kinzlstanislav.sigyctest.BuildConfig
import com.kinzlstanislav.sigyctest.app.network.data.CatResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    private companion object {
        const val DEFAULT_CAT_REQUEST_LIMIT_20 = 20
        const val DEFAULT_CAT_REQUEST_ORDER_DESC = "ASC"
        const val DEFAULT_CAT_REQUEST_FORMAT_JSON = "json"
        const val DEFAULT_CAT_REQUEST_IMAGE_TYPE_JPG = "jpg"
        const val DEFAULT_CAT_REQUEST_SIZE = "small"
        const val DEFAULT_CAT_REQUEST_BREEDS_ONLY_1 = 1
    }

    /**
     * @see https://documenter.getpostman.com/view/5578104/RWgqUxxh
     * */
    @GET("images/search")
    suspend fun fetchCats(
        @HeaderMap headers: Map<String, String> = linkedMapOf(
            Pair("Content-Type", "application/json"),
            Pair("x-api-key", BuildConfig.CAT_API_KEY)
        ),
        @QueryMap defaultStringQueries: Map<String, String> = linkedMapOf(
            Pair("size", DEFAULT_CAT_REQUEST_SIZE),
            Pair("mime_types", DEFAULT_CAT_REQUEST_IMAGE_TYPE_JPG),
            Pair("format", DEFAULT_CAT_REQUEST_FORMAT_JSON),
            Pair("order", DEFAULT_CAT_REQUEST_ORDER_DESC)
        ),
        @QueryMap defaultIntQueries: Map<String, Int> = linkedMapOf(
            Pair("limit", DEFAULT_CAT_REQUEST_LIMIT_20),
            Pair("has_breeds", DEFAULT_CAT_REQUEST_BREEDS_ONLY_1)
        ),
        @Query(value = "page") page: Int
    ): List<CatResponse?>?
}