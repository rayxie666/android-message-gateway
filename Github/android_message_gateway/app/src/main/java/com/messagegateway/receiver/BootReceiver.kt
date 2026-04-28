package com.messagegateway.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.messagegateway.data.repository.SettingsRepository
import com.messagegateway.service.ForwardingService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val settingsRepository = SettingsRepository(context)
        val isEnabled = runBlocking { settingsRepository.serviceEnabledFlow.first() }

        if (isEnabled) {
            context.startForegroundService(Intent(context, ForwardingService::class.java))
        }
    }
}
