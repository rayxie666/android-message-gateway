package com.messagegateway.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.messagegateway.data.Converters
import com.messagegateway.data.model.ForwardLog
import com.messagegateway.data.model.ForwardingRule

@Database(
    entities = [ForwardingRule::class, ForwardLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ruleDao(): RuleDao
    abstract fun forwardLogDao(): ForwardLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "message_gateway.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
