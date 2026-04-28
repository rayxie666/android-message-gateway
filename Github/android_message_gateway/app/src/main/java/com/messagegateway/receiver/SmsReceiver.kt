package com.messagegateway.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.messagegateway.service.ForwardingService

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return

        val grouped = messages.groupBy { it.originatingAddress ?: "unknown" }
        for ((sender, parts) in grouped) {
            val body = parts.joinToString("") { it.messageBody ?: "" }
            if (body.isBlank()) continue

            val serviceIntent = Intent(context, ForwardingService::class.java).apply {
                putExtra(ForwardingService.EXTRA_SENDER, sender)
                putExtra(ForwardingService.EXTRA_BODY, body)
            }
            context.startForegroundService(serviceIntent)
        }
    }
}
