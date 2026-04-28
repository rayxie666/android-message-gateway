package com.messagegateway.forwarding

import android.util.Log
import com.messagegateway.data.model.*
import com.messagegateway.data.repository.LogRepository
import com.messagegateway.data.repository.RuleRepository
import com.messagegateway.data.repository.SettingsRepository
import com.messagegateway.util.PatternMatcher
import kotlinx.coroutines.flow.first

class ForwardingEngine(
    private val ruleRepository: RuleRepository,
    private val logRepository: LogRepository,
    private val settingsRepository: SettingsRepository,
    private val smsForwarder: SmsForwarder,
    private val emailForwarder: EmailForwarder
) {

    suspend fun processMessage(sender: String, body: String) {
        val enabledRules = ruleRepository.getEnabled()
        if (enabledRules.isEmpty()) return

        val needsEmail = enabledRules.any {
            it.forwardMethod == ForwardMethod.EMAIL || it.forwardMethod == ForwardMethod.BOTH
        }
        val smtpConfig = if (needsEmail) settingsRepository.smtpConfigFlow.first() else null

        for (rule in enabledRules) {
            if (!matchesRule(rule, sender, body)) continue

            Log.d(TAG, "Rule '${rule.name}' matched for sender=$sender")

            when (rule.forwardMethod) {
                ForwardMethod.SMS -> forwardViaSms(rule, sender, body)
                ForwardMethod.EMAIL -> forwardViaEmail(rule, sender, body, smtpConfig!!)
                ForwardMethod.BOTH -> {
                    forwardViaSms(rule, sender, body)
                    forwardViaEmail(rule, sender, body, smtpConfig!!)
                }
            }
        }
    }

    private fun matchesRule(rule: ForwardingRule, sender: String, body: String): Boolean {
        return PatternMatcher.matchesSender(sender, rule.senderPattern, rule.senderMatchType)
                && PatternMatcher.matchesContent(body, rule.contentKeyword)
    }

    private suspend fun forwardViaSms(rule: ForwardingRule, sender: String, body: String) {
        if (rule.targetPhone.isBlank()) {
            logResult(rule, sender, body, ForwardMethod.SMS, rule.targetPhone, ForwardStatus.FAILED, "Target phone is empty")
            return
        }

        val result = smsForwarder.forward(sender, body, rule.targetPhone)
        logResult(
            rule, sender, body, ForwardMethod.SMS, rule.targetPhone,
            if (result.isSuccess) ForwardStatus.SUCCESS else ForwardStatus.FAILED,
            result.exceptionOrNull()?.message
        )
    }

    private suspend fun forwardViaEmail(rule: ForwardingRule, sender: String, body: String, smtpConfig: SmtpConfig) {
        if (rule.targetEmail.isBlank()) {
            logResult(rule, sender, body, ForwardMethod.EMAIL, rule.targetEmail, ForwardStatus.FAILED, "Target email is empty")
            return
        }

        val result = emailForwarder.forward(sender, body, rule.targetEmail, smtpConfig)
        logResult(
            rule, sender, body, ForwardMethod.EMAIL, rule.targetEmail,
            if (result.isSuccess) ForwardStatus.SUCCESS else ForwardStatus.FAILED,
            result.exceptionOrNull()?.message
        )
    }

    private suspend fun logResult(
        rule: ForwardingRule,
        sender: String,
        body: String,
        method: ForwardMethod,
        destination: String,
        status: ForwardStatus,
        error: String?
    ) {
        logRepository.insert(
            ForwardLog(
                ruleId = rule.id,
                ruleName = rule.name,
                senderNumber = sender,
                messageBody = body,
                forwardMethod = method,
                targetDestination = destination,
                status = status,
                errorMessage = error
            )
        )
    }

    companion object {
        private const val TAG = "ForwardingEngine"
    }
}
