package com.messagegateway

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
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

        // Request to disable battery optimization
        requestIgnoreBatteryOptimization()

        setContent {
            MessageGatewayTheme {
                AppNavigation()
            }
        }
    }

    private fun requestIgnoreBatteryOptimization() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }
    }
}
