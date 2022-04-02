package com.kinzlstanislav.sigyctest.core.base

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
abstract class BaseDao<T>(
    private val tableName: String,
    ) {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(obj: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(obj: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(obj: List<T>)

    @RawQuery
    protected abstract suspend fun genericQuery(query: SupportSQLiteQuery): Int

    @RawQuery
    protected abstract suspend fun getFirst(query: SupportSQLiteQuery): T?

    @RawQuery
    protected abstract suspend fun getAll(query: SupportSQLiteQuery): List<T>?

    private fun query(query: String) = SimpleSQLiteQuery(query)

    suspend fun nukeTable() {
        genericQuery(query("DELETE FROM $tableName"))
    }

    suspend fun getFirst(): T? {
        return getFirst(query("SELECT * FROM $tableName LIMIT 1"))
    }

    suspend fun getAll(): List<T>? {
        return getAll(query("SELECT * FROM $tableName"))
    }

    @Transaction
    open suspend fun insertOrUpdate(obj: T) {
        val id = insert(obj)
        if (id == -1L) update(obj)
    }

    @Transaction
    open suspend fun insertOrUpdate(objList: List<T>) {
        val insertResult = insert(objList)
        val updateList = mutableListOf<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(objList[i])
        }

        if (updateList.isNotEmpty()) update(updateList)
    }
}