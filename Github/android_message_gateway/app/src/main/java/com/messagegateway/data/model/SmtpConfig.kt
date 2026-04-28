package com.messagegateway.data.model

data class SmtpConfig(
    val host: String = "",
    val port: Int = 587,
    val username: String = "",
    val password: String = "",
    val senderEmail: String = "",
    val useTls: Boolean = true
) {
    val isConfigured: Boolean
        get() = host.isNotBlank() && username.isNotBlank() && password.isNotBlank() && senderEmail.isNotBlank()
}
