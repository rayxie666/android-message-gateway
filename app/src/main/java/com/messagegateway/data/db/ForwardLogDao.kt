package com.messagegateway.data.db

import androidx.room.*
import com.messagegateway.data.model.ForwardLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ForwardLogDao {
    @Query("SELECT * FROM forward_logs ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ForwardLog>>

    @Insert
    suspend fun insert(log: ForwardLog)

    @Query("DELETE FROM forward_logs")
    suspend fun deleteAll()

    @Query("DELETE FROM forward_logs WHERE id NOT IN (SELECT id FROM forward_logs ORDER BY timestamp DESC LIMIT 500)")
    suspend fun trimOldEntries()
}
