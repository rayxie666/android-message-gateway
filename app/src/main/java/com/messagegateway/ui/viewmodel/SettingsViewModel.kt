package com.messagegateway.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.messagegateway.MessageGatewayApp
import com.messagegateway.data.model.SmtpConfig
import com.messagegateway.forwarding.EmailForwarder
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = (application as MessageGatewayApp).settingsRepository
    private val emailForwarder = (application as MessageGatewayApp).emailForwarder

    var host by mutableStateOf("")
    var port by mutableStateOf("587")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var senderEmail by mutableStateOf("")
    var useTls by mutableStateOf(true)

    var isSaved by mutableStateOf(false)
        private set
    var testResult by mutableStateOf<String?>(null)
        private set
    var isTesting by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            settingsRepository.smtpConfigFlow.collect { config ->
                host = config.host
                port = config.port.toString()
                username = config.username
                password = config.password
                senderEmail = config.senderEmail
                useTls = config.useTls
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            settingsRepository.saveSmtpConfig(buildConfig())
            isSaved = true
        }
    }

    fun testConnection() {
        isTesting = true
        testResult = null
        viewModelScope.launch {
            val result = emailForwarder.testConnection(buildConfig())
            testResult = if (result.isSuccess) {
                "Connection successful!"
            } else {
                "Failed: ${result.exceptionOrNull()?.message}"
            }
            isTesting = false
        }
    }

    private fun buildConfig() = SmtpConfig(
        host = host.trim(),
        port = port.toIntOrNull() ?: 587,
        username = username.trim(),
        password = password,
        senderEmail = senderEmail.trim(),
        useTls = useTls
    )
}
