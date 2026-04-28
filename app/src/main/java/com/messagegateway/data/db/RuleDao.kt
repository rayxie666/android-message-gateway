package com.messagegateway.data.db

import androidx.room.*
import com.messagegateway.data.model.ForwardingRule
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao {
    @Query("SELECT * FROM forwarding_rules ORDER BY createdAt DESC")
    fun getAll(): Flow<List<ForwardingRule>>

    @Query("SELECT * FROM forwarding_rules WHERE id = :id")
    suspend fun getById(id: Long): ForwardingRule?

    @Query("SELECT * FROM forwarding_rules WHERE isEnabled = 1")
    suspend fun getEnabled(): List<ForwardingRule>

    @Insert
    suspend fun insert(rule: ForwardingRule): Long

    @Update
    suspend fun update(rule: ForwardingRule)

    @Delete
    suspend fun delete(rule: ForwardingRule)

    @Query("UPDATE forwarding_rules SET isEnabled = :enabled, updatedAt = :timestamp WHERE id = :id")
    suspend fun setEnabled(id: Long, enabled: Boolean, timestamp: Long = System.currentTimeMillis())
}
