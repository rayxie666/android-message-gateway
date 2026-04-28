package com.messagegateway.util

import com.messagegateway.data.model.MatchType

object PatternMatcher {

    fun matchesSender(sender: String, pattern: String, matchType: MatchType): Boolean {
        if (pattern.isBlank()) return true
        return try {
            when (matchType) {
                MatchType.CONTAINS -> sender.contains(pattern, ignoreCase = true)
                MatchType.EXACT -> sender.equals(pattern, ignoreCase = true)
                MatchType.REGEX -> Regex(pattern, RegexOption.IGNORE_CASE).containsMatchIn(sender)
            }
        } catch (_: Exception) {
            false
        }
    }

    fun matchesContent(body: String, keyword: String): Boolean {
        if (keyword.isBlank()) return true
        return body.contains(keyword, ignoreCase = true)
    }
}
