package com.github.tabsweep.actions

import com.github.tabsweep.ui.TabManagerDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * Action that opens the TabSweep dialog.
 * Registered in plugin.xml with keyboard shortcuts.
 */
class OpenTabManagerAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val dialog = TabManagerDialog(project)
        dialog.show()
    }

    override fun update(e: AnActionEvent) {
        // Enable only when a project is open
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
