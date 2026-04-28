package com.messagegateway.data.repository

import com.messagegateway.data.db.ForwardLogDao
import com.messagegateway.data.model.ForwardLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LogRepository(private val logDao: ForwardLogDao) {
    fun getAll(): Flow<List<ForwardLog>> = logDao.getAll()

    suspend fun insert(log: ForwardLog) = withContext(Dispatchers.IO) {
        logDao.insert(log)
        logDao.trimOldEntries()
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        logDao.deleteAll()
    }
}
