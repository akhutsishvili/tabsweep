package com.github.tabmanager.services

import com.github.tabmanager.ui.TabInfo
import com.intellij.openapi.components.Service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project

/**
 * Implementation of TabManagerService that uses IntelliJ's FileEditorManager
 * to manage editor tabs.
 */
@Service(Service.Level.PROJECT)
class TabManagerServiceImpl(private val project: Project) : TabManagerService {

    override fun getOpenTabs(): List<TabInfo> {
        val fileEditorManager = FileEditorManager.getInstance(project)
        val fileDocumentManager = FileDocumentManager.getInstance()

        return fileEditorManager.openFiles
            .distinctBy { it.path }  // De-duplicate files open in multiple splits
            .map { file ->
                val document = fileDocumentManager.getDocument(file)
                val isModified = document?.let { fileDocumentManager.isDocumentUnsaved(it) } ?: false
                TabInfo(
                    file = file,
                    displayName = file.name,
                    isModified = isModified
                )
            }
            .sortedBy { it.displayName.lowercase() }
    }

    override fun closeTab(tabInfo: TabInfo) {
        val fileEditorManager = FileEditorManager.getInstance(project)
        fileEditorManager.closeFile(tabInfo.file)
    }

    override fun closeTabs(tabs: List<TabInfo>) {
        val fileEditorManager = FileEditorManager.getInstance(project)
        tabs.forEach { tabInfo ->
            fileEditorManager.closeFile(tabInfo.file)
        }
    }

    override fun refreshTabInfo(tabInfo: TabInfo): TabInfo? {
        val fileEditorManager = FileEditorManager.getInstance(project)
        val fileDocumentManager = FileDocumentManager.getInstance()

        // Check if file is still open
        if (!fileEditorManager.openFiles.contains(tabInfo.file)) {
            return null
        }

        val document = fileDocumentManager.getDocument(tabInfo.file)
        val isModified = document?.let { fileDocumentManager.isDocumentUnsaved(it) } ?: false

        return tabInfo.copy(isModified = isModified)
    }
}
