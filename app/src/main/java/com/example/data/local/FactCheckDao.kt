package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FactCheckDao {
    @Query("SELECT * FROM fact_checks ORDER BY timestamp DESC")
    fun getAllFactChecks(): Flow<List<FactCheckEntity>>

    @Query("SELECT * FROM fact_checks WHERE isBookmarked = 1 ORDER BY timestamp DESC")
    fun getBookmarkedFactChecks(): Flow<List<FactCheckEntity>>

    @Query("SELECT * FROM fact_checks WHERE id = :id LIMIT 1")
    suspend fun getFactCheckById(id: String): FactCheckEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFactCheck(entity: FactCheckEntity)

    @Query("UPDATE fact_checks SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: String, isBookmarked: Boolean)

    @Query("DELETE FROM fact_checks WHERE id = :id")
    suspend fun deleteFactCheck(id: String)

    @Query("DELETE FROM fact_checks")
    suspend fun clearAll()
}
