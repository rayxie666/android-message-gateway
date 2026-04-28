package com.messagegateway.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.messagegateway.MessageGatewayApp
import com.messagegateway.data.model.ForwardMethod
import com.messagegateway.data.model.ForwardingRule
import com.messagegateway.data.model.MatchType
import kotlinx.coroutines.launch

class RuleEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val ruleRepository = (application as MessageGatewayApp).ruleRepository

    var name by mutableStateOf("")
    var senderPattern by mutableStateOf("")
    var senderMatchType by mutableStateOf(MatchType.CONTAINS)
    var contentKeyword by mutableStateOf("")
    var forwardMethod by mutableStateOf(ForwardMethod.SMS)
    var targetPhone by mutableStateOf("")
    var targetEmail by mutableStateOf("")

    var isEditing by mutableStateOf(false)
        private set
    var isSaved by mutableStateOf(false)
        private set
    var nameError by mutableStateOf<String?>(null)
        private set
    var targetError by mutableStateOf<String?>(null)
        private set

    private var editingRuleId: Long = 0

    fun loadRule(ruleId: Long) {
        viewModelScope.launch {
            val rule = ruleRepository.getById(ruleId) ?: return@launch
            isEditing = true
            editingRuleId = rule.id
            name = rule.name
            senderPattern = rule.senderPattern
            senderMatchType = rule.senderMatchType
            contentKeyword = rule.contentKeyword
            forwardMethod = rule.forwardMethod
            targetPhone = rule.targetPhone
            targetEmail = rule.targetEmail
        }
    }

    fun save() {
        nameError = null
        targetError = null

        if (name.isBlank()) {
            nameError = "Rule name is required"
            return
        }

        when (forwardMethod) {
            ForwardMethod.SMS -> {
                if (targetPhone.isBlank()) {
                    targetError = "Target phone number is required"
                    return
                }
            }
            ForwardMethod.EMAIL -> {
                if (targetEmail.isBlank()) {
                    targetError = "Target email is required"
                    return
                }
            }
            ForwardMethod.BOTH -> {
                if (targetPhone.isBlank() && targetEmail.isBlank()) {
                    targetError = "At least one target is required"
                    return
                }
            }
        }

        viewModelScope.launch {
            val rule = ForwardingRule(
                id = if (isEditing) editingRuleId else 0,
                name = name.trim(),
                isEnabled = true,
                senderPattern = senderPattern.trim(),
                senderMatchType = senderMatchType,
                contentKeyword = contentKeyword.trim(),
                forwardMethod = forwardMethod,
                targetPhone = targetPhone.trim(),
                targetEmail = targetEmail.trim()
            )

            if (isEditing) {
                ruleRepository.update(rule)
            } else {
                ruleRepository.insert(rule)
            }
            isSaved = true
        }
    }
}
