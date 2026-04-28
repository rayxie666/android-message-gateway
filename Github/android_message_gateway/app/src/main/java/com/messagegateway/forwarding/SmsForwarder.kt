package com.messagegateway.forwarding

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SmsForwarder(private val context: Context) {

    suspend fun forward(sender: String, body: String, targetPhone: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    SmsManager.getDefault()
                }

                val message = "[Fwd from $sender] $body"
                val parts = smsManager.divideMessage(message)

                if (parts.size == 1) {
                    smsManager.sendTextMessage(targetPhone, null, message, null, null)
                } else {
                    smsManager.sendMultipartTextMessage(targetPhone, null, parts, null, null)
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
