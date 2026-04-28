# Message Gateway

An Android app that listens for incoming SMS messages and forwards them via **SMS** or **Email** based on configurable rules.

## Features

- **SMS Forwarding** - Forward received messages to another phone number via SMS
- **Email Forwarding** - Forward received messages to an email address via SMTP
- **Configurable Rules** - Create rules with filters (sender pattern, content keyword) and choose forwarding method (SMS, Email, or Both)
- **Foreground Service** - Runs persistently in the background to ensure reliable message reception
- **Auto-start on Boot** - Service automatically restarts after device reboot
- **Forward Logs** - View history of all forwarded messages with success/failure status

## Setup

1. Open the project in Android Studio
2. Build and install on your Android device
3. Grant SMS permissions when prompted
4. (For email forwarding) Go to **Settings** and configure your SMTP server

### Gmail SMTP Configuration

| Field | Value |
|-------|-------|
| SMTP Host | `smtp.gmail.com` |
| SMTP Port | `587` |
| Username | Your Gmail address |
| Password | [App Password](https://myaccount.google.com/apppasswords) (not your Gmail password) |
| Sender Email | Your Gmail address |
| Use TLS | Enabled |

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Database**: Room (rules & logs)
- **Settings**: DataStore Preferences (SMTP config)
- **Email**: JavaMail API
- **Architecture**: MVVM, Single Activity

## Requirements

- Android 8.0 (API 26) or higher
- SMS permissions (RECEIVE_SMS, SEND_SMS)
- Internet permission (for email forwarding)
