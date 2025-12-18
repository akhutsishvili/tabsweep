package com.github.tabsweep.util

import org.junit.Assert.*
import org.junit.Test

class FuzzyMatcherTest {

    @Test
    fun `matches returns true for exact match`() {
        assertTrue(FuzzyMatcher.matches("TabManagerDialog", "TabManagerDialog"))
    }

    @Test
    fun `matches returns true for prefix match`() {
        assertTrue(FuzzyMatcher.matches("TabManagerDialog", "Tab"))
    }

    @Test
    fun `matches returns true for fuzzy match with gaps`() {
        assertTrue(FuzzyMatcher.matches("TabManagerDialog", "tmd"))
        assertTrue(FuzzyMatcher.matches("TabManagerDialog", "TMD"))
    }

    @Test
    fun `matches returns true for case insensitive match`() {
        assertTrue(FuzzyMatcher.matches("TabManagerDialog", "tabmanagerdialog"))
        assertTrue(FuzzyMatcher.matches("tabmanagerdialog", "TABMANAGERDIALOG"))
    }

    @Test
    fun `matches returns false when characters not in order`() {
        assertFalse(FuzzyMatcher.matches("TabManagerDialog", "dmt"))
    }

    @Test
    fun `matches returns false when pattern has extra characters`() {
        assertFalse(FuzzyMatcher.matches("Tab", "TabManager"))
    }

    @Test
    fun `matches returns true for empty pattern`() {
        assertTrue(FuzzyMatcher.matches("anything", ""))
    }

    @Test
    fun `matches returns false for empty text with non-empty pattern`() {
        assertFalse(FuzzyMatcher.matches("", "abc"))
    }

    @Test
    fun `matches returns true for both empty`() {
        assertTrue(FuzzyMatcher.matches("", ""))
    }

    @Test
    fun `matches works with file extensions`() {
        assertTrue(FuzzyMatcher.matches("TabManagerDialog.kt", "tmd.kt"))
        assertTrue(FuzzyMatcher.matches("TabManagerDialog.kt", "dialog.kt"))
    }

    @Test
    fun `matches works with special characters`() {
        assertTrue(FuzzyMatcher.matches("my-file-name.txt", "mfn"))
        assertTrue(FuzzyMatcher.matches("my_file_name.txt", "mfn"))
    }

    // Score tests

    @Test
    fun `score returns -1 for non-matching pattern`() {
        assertEquals(-1, FuzzyMatcher.score("abc", "xyz"))
    }

    @Test
    fun `score returns 0 for empty pattern`() {
        assertEquals(0, FuzzyMatcher.score("anything", ""))
    }

    @Test
    fun `score gives higher score for consecutive matches`() {
        val consecutiveScore = FuzzyMatcher.score("abcdef", "abc")
        val gapScore = FuzzyMatcher.score("axbxcxdef", "abc")
        assertTrue("Consecutive matches should score higher", consecutiveScore > gapScore)
    }

    @Test
    fun `score gives higher score for match at start`() {
        val startScore = FuzzyMatcher.score("abc", "a")
        val middleScore = FuzzyMatcher.score("xabc", "a")
        assertTrue("Start matches should score higher", startScore > middleScore)
    }

    @Test
    fun `score gives higher score for shorter text`() {
        val shortScore = FuzzyMatcher.score("Tab.kt", "tab")
        val longScore = FuzzyMatcher.score("TabManagerDialogVeryLongName.kt", "tab")
        assertTrue("Shorter text should score higher", shortScore > longScore)
    }

    @Test
    fun `score gives bonus for matching after separator`() {
        val afterSeparatorScore = FuzzyMatcher.score("my-dialog", "d")
        val middleScore = FuzzyMatcher.score("mydialog", "d")
        assertTrue("Match after separator should score higher", afterSeparatorScore > middleScore)
    }
}
