package com.github.tabsweep.util

/**
 * Utility object for fuzzy string matching.
 */
object FuzzyMatcher {
    /**
     * Performs fuzzy matching on the given text against a pattern.
     * Returns true if all characters in the pattern appear in the text in order.
     *
     * @param text The text to search in
     * @param pattern The pattern to match
     * @return true if the pattern matches the text
     */
    fun matches(text: String, pattern: String): Boolean {
        if (pattern.isEmpty()) return true
        if (text.isEmpty()) return false

        val lowerText = text.lowercase()
        val lowerPattern = pattern.lowercase()

        var patternIdx = 0
        for (char in lowerText) {
            if (patternIdx < lowerPattern.length && char == lowerPattern[patternIdx]) {
                patternIdx++
            }
        }
        return patternIdx == lowerPattern.length
    }

    /**
     * Calculates a match score for ranking purposes.
     * Higher scores indicate better matches.
     *
     * @param text The text to search in
     * @param pattern The pattern to match
     * @return A score where higher is better, or -1 if no match
     */
    fun score(text: String, pattern: String): Int {
        if (!matches(text, pattern)) return -1
        if (pattern.isEmpty()) return 0

        val lowerText = text.lowercase()
        val lowerPattern = pattern.lowercase()

        var score = 0
        var patternIdx = 0
        var consecutiveMatches = 0
        var prevMatchIdx = -2

        for ((textIdx, char) in lowerText.withIndex()) {
            if (patternIdx < lowerPattern.length && char == lowerPattern[patternIdx]) {
                // Bonus for consecutive matches
                if (textIdx == prevMatchIdx + 1) {
                    consecutiveMatches++
                    score += consecutiveMatches * 2
                } else {
                    consecutiveMatches = 1
                }

                // Bonus for matching at start
                if (textIdx == 0) score += 10

                // Bonus for matching after separator (., -, _, space)
                if (textIdx > 0) {
                    val prevChar = lowerText[textIdx - 1]
                    if (prevChar == '.' || prevChar == '-' || prevChar == '_' || prevChar == ' ') {
                        score += 5
                    }
                }

                prevMatchIdx = textIdx
                patternIdx++
            }
        }

        // Bonus for shorter texts (more specific matches)
        score += maxOf(0, 20 - text.length)

        return score
    }
}
