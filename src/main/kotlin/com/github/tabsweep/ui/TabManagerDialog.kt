package com.github.tabsweep.ui

import com.github.tabsweep.services.TabManagerService
import com.github.tabsweep.util.FuzzyMatcher
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.SearchTextField
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.DefaultListModel
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.ListSelectionModel
import javax.swing.event.DocumentEvent

/**
 * Dialog for managing open editor tabs.
 * Displays a list of all open tabs with checkboxes for selection
 * and provides bulk operations like closing selected tabs.
 */
class TabManagerDialog(private val project: Project) : DialogWrapper(project) {

    private val tabService: TabManagerService = project.service()
    private val listModel = DefaultListModel<TabInfo>()
    private val tabList = JBList(listModel)
    private val cellRenderer = TabListCellRenderer()

    // Store all tabs for filtering
    private var allTabs: MutableList<TabInfo> = mutableListOf()

    // Search field (initially hidden)
    private val searchField = SearchTextField(false)
    private var searchQuery = ""

    init {
        title = "TabSweep"
        setOKButtonText("Close Selected")
        setCancelButtonText("Cancel")
        init()
        loadTabs()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        panel.preferredSize = Dimension(400, 300)

        // Configure search field (hidden initially)
        searchField.isVisible = false
        searchField.textEditor.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                searchQuery = searchField.text
                filterTabs()
            }
        })

        // Configure the list
        tabList.cellRenderer = cellRenderer
        tabList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        tabList.visibleRowCount = 15

        // Add scroll pane
        val scrollPane = JBScrollPane(tabList)
        scrollPane.border = JBUI.Borders.empty()

        // Create button panel
        val buttonPanel = createButtonPanel()

        // Top panel with search
        val topPanel = JPanel(BorderLayout())
        topPanel.add(searchField, BorderLayout.CENTER)

        panel.add(topPanel, BorderLayout.NORTH)
        panel.add(scrollPane, BorderLayout.CENTER)
        panel.add(buttonPanel, BorderLayout.SOUTH)

        // Setup event handlers
        setupKeyboardNavigation()
        setupMouseHandling()

        return panel
    }

    private fun createButtonPanel(): JPanel {
        val panel = JPanel()

        val selectAllButton = JButton("Select All")
        selectAllButton.mnemonic = KeyEvent.VK_A
        selectAllButton.addActionListener { selectAll() }

        val closeSelectedButton = JButton("Close Selected")
        closeSelectedButton.mnemonic = KeyEvent.VK_C
        closeSelectedButton.addActionListener { closeSelectedTabs() }

        val closeAndExitButton = JButton("Close & Exit")
        closeAndExitButton.mnemonic = KeyEvent.VK_X
        closeAndExitButton.addActionListener { doOKAction() }

        panel.add(selectAllButton)
        panel.add(closeSelectedButton)
        panel.add(closeAndExitButton)

        return panel
    }

    private fun setupKeyboardNavigation() {
        tabList.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                when (e.keyCode) {
                    KeyEvent.VK_SPACE -> {
                        toggleSelectedItem()
                        e.consume()
                    }
                    KeyEvent.VK_DELETE -> {
                        closeHighlightedTab()
                        e.consume()
                    }
                    KeyEvent.VK_BACK_SPACE -> {
                        // Remove last character from search
                        if (searchQuery.isNotEmpty()) {
                            searchField.text = searchQuery.dropLast(1)
                            if (searchField.text.isEmpty()) {
                                searchField.isVisible = false
                            }
                        }
                        e.consume()
                    }
                    KeyEvent.VK_ENTER -> {
                        doOKAction()
                        e.consume()
                    }
                    KeyEvent.VK_ESCAPE -> {
                        if (searchQuery.isNotEmpty()) {
                            // Clear search first
                            clearSearch()
                            e.consume()
                        }
                        // Otherwise let dialog handle escape to close
                    }
                    KeyEvent.VK_UP, KeyEvent.VK_DOWN -> {
                        // Let default list navigation handle these
                    }
                    else -> {
                        // Handle typing for fuzzy search
                        val char = e.keyChar
                        if (char.isLetterOrDigit() || char == '.' || char == '-' || char == '_') {
                            appendToSearch(char)
                            e.consume()
                        }
                    }
                }
            }
        })
    }

    private fun appendToSearch(char: Char) {
        if (!searchField.isVisible) {
            searchField.isVisible = true
        }
        searchField.text = searchQuery + char
        tabList.requestFocusInWindow()  // Keep focus on list
    }

    private fun clearSearch() {
        searchField.text = ""
        searchField.isVisible = false
        searchQuery = ""
        filterTabs()
    }

    private fun filterTabs() {
        val previouslySelected = if (tabList.selectedIndex >= 0) {
            listModel.getElementAt(tabList.selectedIndex)
        } else null

        listModel.clear()

        val filtered = if (searchQuery.isEmpty()) {
            allTabs
        } else {
            allTabs.filter { FuzzyMatcher.matches(it.displayName, searchQuery) }
        }

        filtered.forEach { listModel.addElement(it) }

        // Try to restore selection
        if (listModel.size() > 0) {
            val newIndex = if (previouslySelected != null) {
                val idx = (0 until listModel.size()).indexOfFirst {
                    listModel.getElementAt(it).file == previouslySelected.file
                }
                if (idx >= 0) idx else 0
            } else 0
            tabList.selectedIndex = newIndex
        }
    }

    private fun setupMouseHandling() {
        tabList.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val index = tabList.locationToIndex(e.point)
                if (index < 0) return

                val cellBounds = tabList.getCellBounds(index, index)
                val relativeX = e.x - cellBounds.x

                // Check if click was on checkbox area (first ~30 pixels)
                if (relativeX < 30) {
                    toggleItemAt(index)
                }
                // Check if click was on close button area (last ~20 pixels)
                else if (relativeX > cellBounds.width - 25) {
                    closeTabAt(index)
                }
            }
        })
    }

    private fun loadTabs() {
        allTabs = tabService.getOpenTabs().toMutableList()
        filterTabs()
    }

    private fun toggleSelectedItem() {
        val index = tabList.selectedIndex
        if (index >= 0) {
            toggleItemAt(index)
        }
    }

    private fun toggleItemAt(index: Int) {
        val tabInfo = listModel.getElementAt(index)
        tabInfo.isSelected = !tabInfo.isSelected
        listModel.setElementAt(tabInfo, index)  // Trigger repaint
    }

    private fun closeHighlightedTab() {
        val index = tabList.selectedIndex
        if (index >= 0) {
            closeTabAt(index)
        }
    }

    private fun closeTabAt(index: Int) {
        val tabInfo = listModel.getElementAt(index)
        tabService.closeTab(tabInfo)

        // Remove from both lists
        allTabs.removeIf { it.file == tabInfo.file }
        listModel.removeElementAt(index)

        // Adjust selection
        if (listModel.size() > 0) {
            tabList.selectedIndex = minOf(index, listModel.size() - 1)
        }
    }

    private fun selectAll() {
        // Select all visible (filtered) tabs
        for (i in 0 until listModel.size()) {
            val tabInfo = listModel.getElementAt(i)
            tabInfo.isSelected = true
            listModel.setElementAt(tabInfo, i)
        }
    }

    private fun closeSelectedTabs() {
        // Close all selected tabs but keep dialog open
        val selectedTabs = allTabs.filter { it.isSelected }

        if (selectedTabs.isNotEmpty()) {
            tabService.closeTabs(selectedTabs)
            // Remove closed tabs from our list
            allTabs.removeAll(selectedTabs.toSet())
            filterTabs()
        }
    }

    override fun doOKAction() {
        // Close all selected tabs and close dialog
        val selectedTabs = allTabs.filter { it.isSelected }

        if (selectedTabs.isNotEmpty()) {
            tabService.closeTabs(selectedTabs)
        }

        super.doOKAction()
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return tabList
    }
}
