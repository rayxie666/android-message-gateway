package com.messagegateway.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ForwardStatus { SUCCESS, FAILED }

@Entity(tableName = "forward_logs")
data class ForwardLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ruleId: Long,
    val ruleName: String,
    val senderNumber: String,
    val messageBody: String,
    val forwardMethod: ForwardMethod,
    val targetDestination: String,
    val status: ForwardStatus,
    val errorMessage: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
