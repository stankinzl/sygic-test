package com.kinzlstanislav.sigyctest.app.database

import androidx.room.Dao
import androidx.room.Query
import com.kinzlstanislav.sigyctest.app.network.data.CatResponse
import com.kinzlstanislav.sigyctest.core.base.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CatsDao : BaseDao<CatResponse>(
    tableName = CatResponse::class.java.simpleName
) {

    @Query("SELECT * FROM CatResponse")
    abstract fun observeCatEntries(): Flow<List<CatResponse>?>
}