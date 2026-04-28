package com.messagegateway.forwarding

import com.messagegateway.data.model.SmtpConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailForwarder {

    suspend fun forward(
        sender: String,
        body: String,
        targetEmail: String,
        smtpConfig: SmtpConfig
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (!smtpConfig.isConfigured) {
                return@withContext Result.failure(IllegalStateException("SMTP not configured"))
            }

            val props = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.host", smtpConfig.host)
                put("mail.smtp.port", smtpConfig.port.toString())
                put("mail.smtp.connectiontimeout", "15000")
                put("mail.smtp.timeout", "15000")
                if (smtpConfig.useTls) {
                    put("mail.smtp.starttls.enable", "true")
                }
            }

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(smtpConfig.username, smtpConfig.password)
                }
            })

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(smtpConfig.senderEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(targetEmail))
                subject = "SMS from $sender"
                setText(body)
            }

            Transport.send(message)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun testConnection(smtpConfig: SmtpConfig): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (!smtpConfig.isConfigured) {
                return@withContext Result.failure(IllegalStateException("SMTP not configured"))
            }

            val props = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.host", smtpConfig.host)
                put("mail.smtp.port", smtpConfig.port.toString())
                put("mail.smtp.connectiontimeout", "10000")
                put("mail.smtp.timeout", "10000")
                if (smtpConfig.useTls) {
                    put("mail.smtp.starttls.enable", "true")
                }
            }

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(smtpConfig.username, smtpConfig.password)
                }
            })

            val transport = session.getTransport("smtp")
            transport.connect()
            transport.close()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
