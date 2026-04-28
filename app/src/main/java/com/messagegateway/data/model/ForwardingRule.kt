package com.messagegateway.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MatchType { CONTAINS, REGEX, EXACT }
enum class ForwardMethod { SMS, EMAIL, BOTH }

@Entity(tableName = "forwarding_rules")
data class ForwardingRule(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val isEnabled: Boolean = false,
    val senderPattern: String = "",
    val senderMatchType: MatchType = MatchType.CONTAINS,
    val contentKeyword: String = "",
    val forwardMethod: ForwardMethod = ForwardMethod.SMS,
    val targetPhone: String = "",
    val targetEmail: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
