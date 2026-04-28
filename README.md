# Message Gateway

An Android app that listens for incoming SMS messages and forwards them via **SMS** or **Email** based on configurable rules.

## Features

- **SMS Forwarding** - Forward received messages to another phone number via SMS
- **Email Forwarding** - Forward received messages to an email address via SMTP
- **Configurable Rules** - Create rules with filters (sender pattern, content keyword) and choose forwarding method (SMS, Email, or Both)
- **Foreground Service** - Runs persistently in the background, even with screen off
- **Auto-start on Boot** - Service automatically restarts after device reboot
- **Forward Logs** - View history of all forwarded messages with success/failure status

## Quick Start

### 1. Install

- Open the project in Android Studio
- Connect your Android device via USB (enable USB Debugging)
- Build and Run

### 2. Grant Permissions

On first launch, the app will request the following permissions — **allow all of them**:

- **SMS permissions** (RECEIVE_SMS, SEND_SMS) — required for receiving and forwarding SMS
- **Battery optimization** — a system dialog will pop up asking to disable battery optimization for this app, tap **Allow** to keep the service running with screen off

### 3. Configure Email Forwarding (Optional)

Go to **Settings** (gear icon on top right) and fill in your SMTP server info.

#### Gmail SMTP Setup

1. Enable **2-Step Verification** on your Google account: https://myaccount.google.com/security
2. Generate an **App Password**: https://myaccount.google.com/apppasswords
   - Enter a name like `Message Gateway`, click **Create**
   - Copy the 16-character password
3. Fill in the Settings screen:

| Field | Value |
|-------|-------|
| SMTP Host | `smtp.gmail.com` |
| SMTP Port | `587` |
| Username | Your Gmail address |
| Password | The 16-character App Password |
| Sender Email | Your Gmail address |
| Use TLS | Enabled |

4. Tap **Test Connection** to verify, then **Save**

### 4. Create a Forwarding Rule

1. On the main screen, tap the **+** button
2. Fill in the rule:
   - **Rule Name** — a label for this rule (e.g. "Forward all to email")
   - **Sender Pattern** (optional) — filter by sender number. Leave empty to match all senders
   - **Match Type** — `Contains`, `Exact`, or `Regex`
   - **Content Keyword** (optional) — filter by message content. Leave empty to match all messages
   - **Forward Method** — choose `SMS`, `EMAIL`, or `BOTH`
   - **Target Phone / Target Email** — where to forward to
3. Tap **Save**

The rule is enabled by default. You can toggle it on/off from the main screen.

### 5. Verify

- The forwarding service starts automatically (check the notification bar for "Message Gateway - Listening for incoming messages")
- Send a test SMS from another phone to this device
- Check the **Logs** screen (clock icon on top right) to see if the message was forwarded successfully

## Background Running

The app uses a foreground service with WakeLock to stay alive in the background. To ensure reliable operation:

- **Allow** the battery optimization exemption when prompted on first launch
- On Chinese brand phones (Xiaomi, Huawei, OPPO, vivo), you may also need to:
  - Go to **Settings > Battery > Background Activity Management** and set this app to **Unrestricted**
  - Enable **Auto-start** for this app in the phone's security/permission settings
  - Lock the app in the recent apps list (swipe down on the app card to pin it)

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
