package com.messagegateway

import android.app.Application
import com.messagegateway.data.db.AppDatabase
import com.messagegateway.data.repository.LogRepository
import com.messagegateway.data.repository.RuleRepository
import com.messagegateway.data.repository.SettingsRepository
import com.messagegateway.forwarding.EmailForwarder
import com.messagegateway.forwarding.ForwardingEngine
import com.messagegateway.forwarding.SmsForwarder

class MessageGatewayApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val ruleRepository: RuleRepository by lazy { RuleRepository(database.ruleDao()) }
    val logRepository: LogRepository by lazy { LogRepository(database.forwardLogDao()) }
    val settingsRepository: SettingsRepository by lazy { SettingsRepository(this) }
    val smsForwarder: SmsForwarder by lazy { SmsForwarder(this) }
    val emailForwarder: EmailForwarder by lazy { EmailForwarder() }

    val forwardingEngine: ForwardingEngine by lazy {
        ForwardingEngine(
            ruleRepository = ruleRepository,
            logRepository = logRepository,
            settingsRepository = settingsRepository,
            smsForwarder = smsForwarder,
            emailForwarder = emailForwarder
        )
    }
}
