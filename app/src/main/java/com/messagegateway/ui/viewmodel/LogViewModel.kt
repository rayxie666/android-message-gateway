package com.messagegateway.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.messagegateway.MessageGatewayApp
import com.messagegateway.data.model.ForwardLog
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogViewModel(application: Application) : AndroidViewModel(application) {

    private val logRepository = (application as MessageGatewayApp).logRepository

    val logs: StateFlow<List<ForwardLog>> = logRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearAll() {
        viewModelScope.launch { logRepository.deleteAll() }
    }
}
