package com.messagegateway.data.repository

import com.messagegateway.data.db.RuleDao
import com.messagegateway.data.model.ForwardingRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RuleRepository(private val ruleDao: RuleDao) {
    fun getAll(): Flow<List<ForwardingRule>> = ruleDao.getAll()

    suspend fun getById(id: Long): ForwardingRule? = withContext(Dispatchers.IO) {
        ruleDao.getById(id)
    }

    suspend fun getEnabled(): List<ForwardingRule> = withContext(Dispatchers.IO) {
        ruleDao.getEnabled()
    }

    suspend fun insert(rule: ForwardingRule): Long = withContext(Dispatchers.IO) {
        ruleDao.insert(rule)
    }

    suspend fun update(rule: ForwardingRule) = withContext(Dispatchers.IO) {
        ruleDao.update(rule.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun delete(rule: ForwardingRule) = withContext(Dispatchers.IO) {
        ruleDao.delete(rule)
    }

    suspend fun setEnabled(id: Long, enabled: Boolean) = withContext(Dispatchers.IO) {
        ruleDao.setEnabled(id, enabled)
    }
}
