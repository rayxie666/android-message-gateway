package com.messagegateway

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.messagegateway.service.ForwardingService
import com.messagegateway.ui.navigation.AppNavigation
import com.messagegateway.ui.theme.MessageGatewayTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Auto-start forwarding service if enabled
        val app = application as MessageGatewayApp
        val isEnabled = runBlocking { app.settingsRepository.serviceEnabledFlow.first() }
        if (isEnabled) {
            startForegroundService(Intent(this, ForwardingService::class.java))
        }

        setContent {
            MessageGatewayTheme {
                AppNavigation()
            }
        }
    }
}
