package com.github.tabmanager.ui

import com.intellij.openapi.vfs.VirtualFile
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class TabInfoTest {

    private fun createMockFile(name: String): VirtualFile {
        val file = mock(VirtualFile::class.java)
        `when`(file.name).thenReturn(name)
        `when`(file.path).thenReturn("/path/to/$name")
        return file
    }

    @Test
    fun `TabInfo creates with default isSelected as false`() {
        val file = createMockFile("test.kt")
        val tabInfo = TabInfo(file, "test.kt", isModified = false)

        assertFalse(tabInfo.isSelected)
    }

    @Test
    fun `TabInfo stores file reference correctly`() {
        val file = createMockFile("MyFile.kt")
        val tabInfo = TabInfo(file, "MyFile.kt", isModified = true)

        assertEquals(file, tabInfo.file)
        assertEquals("MyFile.kt", tabInfo.displayName)
        assertTrue(tabInfo.isModified)
    }

    @Test
    fun `TabInfo isSelected can be modified`() {
        val file = createMockFile("test.kt")
        val tabInfo = TabInfo(file, "test.kt", isModified = false)

        assertFalse(tabInfo.isSelected)
        tabInfo.isSelected = true
        assertTrue(tabInfo.isSelected)
    }

    @Test
    fun `TabInfo copy works correctly`() {
        val file = createMockFile("original.kt")
        val original = TabInfo(file, "original.kt", isModified = true, isSelected = true)
        val copy = original.copy(isModified = false)

        assertEquals(original.file, copy.file)
        assertEquals(original.displayName, copy.displayName)
        assertFalse(copy.isModified)
        assertTrue(copy.isSelected)
    }

    @Test
    fun `TabInfo equals works correctly`() {
        val file = createMockFile("test.kt")
        val tabInfo1 = TabInfo(file, "test.kt", isModified = false)
        val tabInfo2 = TabInfo(file, "test.kt", isModified = false)

        assertEquals(tabInfo1, tabInfo2)
    }

    @Test
    fun `TabInfo not equals with different isModified`() {
        val file = createMockFile("test.kt")
        val tabInfo1 = TabInfo(file, "test.kt", isModified = false)
        val tabInfo2 = TabInfo(file, "test.kt", isModified = true)

        assertNotEquals(tabInfo1, tabInfo2)
    }
}
