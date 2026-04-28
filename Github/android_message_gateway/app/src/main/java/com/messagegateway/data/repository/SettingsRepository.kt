package com.messagegateway.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.messagegateway.data.model.SmtpConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val SMTP_HOST = stringPreferencesKey("smtp_host")
        val SMTP_PORT = intPreferencesKey("smtp_port")
        val SMTP_USERNAME = stringPreferencesKey("smtp_username")
        val SMTP_PASSWORD = stringPreferencesKey("smtp_password")
        val SMTP_SENDER_EMAIL = stringPreferencesKey("smtp_sender_email")
        val SMTP_USE_TLS = booleanPreferencesKey("smtp_use_tls")
        val SERVICE_ENABLED = booleanPreferencesKey("service_enabled")
    }

    val smtpConfigFlow: Flow<SmtpConfig> = context.dataStore.data.map { prefs ->
        SmtpConfig(
            host = prefs[Keys.SMTP_HOST] ?: "",
            port = prefs[Keys.SMTP_PORT] ?: 587,
            username = prefs[Keys.SMTP_USERNAME] ?: "",
            password = prefs[Keys.SMTP_PASSWORD] ?: "",
            senderEmail = prefs[Keys.SMTP_SENDER_EMAIL] ?: "",
            useTls = prefs[Keys.SMTP_USE_TLS] ?: true
        )
    }

    val serviceEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.SERVICE_ENABLED] ?: true
    }

    suspend fun saveSmtpConfig(config: SmtpConfig) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SMTP_HOST] = config.host
            prefs[Keys.SMTP_PORT] = config.port
            prefs[Keys.SMTP_USERNAME] = config.username
            prefs[Keys.SMTP_PASSWORD] = config.password
            prefs[Keys.SMTP_SENDER_EMAIL] = config.senderEmail
            prefs[Keys.SMTP_USE_TLS] = config.useTls
        }
    }

    suspend fun setServiceEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SERVICE_ENABLED] = enabled
        }
    }
}
