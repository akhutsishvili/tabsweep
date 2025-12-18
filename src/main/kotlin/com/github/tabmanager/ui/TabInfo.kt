package com.github.tabmanager.ui

import com.intellij.openapi.vfs.VirtualFile

/**
 * Represents information about an open editor tab.
 *
 * @property file The virtual file associated with this tab
 * @property displayName The name to display in the list (usually the file name)
 * @property isModified Whether the file has unsaved changes
 * @property isSelected Whether this tab is selected for bulk operations
 */
data class TabInfo(
    val file: VirtualFile,
    val displayName: String,
    val isModified: Boolean,
    var isSelected: Boolean = false
)
