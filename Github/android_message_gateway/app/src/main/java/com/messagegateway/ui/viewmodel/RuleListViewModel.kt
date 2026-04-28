package com.messagegateway.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.messagegateway.MessageGatewayApp
import com.messagegateway.data.model.ForwardingRule
import com.messagegateway.service.ForwardingService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RuleListViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MessageGatewayApp
    private val ruleRepository = app.ruleRepository
    private val settingsRepository = app.settingsRepository

    val rules: StateFlow<List<ForwardingRule>> = ruleRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviceEnabled: StateFlow<Boolean> = settingsRepository.serviceEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleRule(id: Long, enabled: Boolean) {
        viewModelScope.launch { ruleRepository.setEnabled(id, enabled) }
    }

    fun deleteRule(rule: ForwardingRule) {
        viewModelScope.launch { ruleRepository.delete(rule) }
    }

    fun setServiceEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setServiceEnabled(enabled)
            val context = getApplication<MessageGatewayApp>()
            if (enabled) {
                context.startForegroundService(Intent(context, ForwardingService::class.java))
            } else {
                context.stopService(Intent(context, ForwardingService::class.java))
            }
        }
    }
}
