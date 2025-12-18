package com.github.tabsweep.ui

import com.intellij.icons.AllIcons
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.util.IconUtil
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JCheckBox
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

/**
 * Custom cell renderer for the tab list that displays:
 * - Checkbox for selection
 * - File type icon
 * - File name (with * for modified files)
 * - Close button (X)
 */
class TabListCellRenderer : ListCellRenderer<TabInfo> {

    private val panel = JPanel(BorderLayout())
    private val leftPanel = JPanel(FlowLayout(FlowLayout.LEFT, 4, 0))
    private val checkbox = JCheckBox()
    private val iconLabel = JBLabel()
    private val nameLabel = JBLabel()
    private val closeButton = JBLabel(AllIcons.Actions.Close)

    init {
        panel.border = JBUI.Borders.empty(2, 4)

        leftPanel.isOpaque = false
        leftPanel.add(checkbox)
        leftPanel.add(iconLabel)
        leftPanel.add(nameLabel)

        closeButton.toolTipText = "Close tab"
        closeButton.preferredSize = Dimension(16, 16)

        panel.add(leftPanel, BorderLayout.CENTER)
        panel.add(closeButton, BorderLayout.EAST)
    }

    override fun getListCellRendererComponent(
        list: JList<out TabInfo>,
        value: TabInfo,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        // Set checkbox state
        checkbox.isSelected = value.isSelected
        checkbox.isOpaque = false

        // Set file icon
        val icon = value.file.fileType.icon ?: AllIcons.FileTypes.Any_type
        iconLabel.icon = IconUtil.scale(icon, iconLabel, 1.0f)

        // Set file name with modification indicator
        val displayText = if (value.isModified) {
            "*${value.displayName}"
        } else {
            value.displayName
        }
        nameLabel.text = displayText

        // Handle selection colors
        if (isSelected) {
            panel.background = list.selectionBackground
            nameLabel.foreground = list.selectionForeground
        } else {
            panel.background = list.background
            nameLabel.foreground = list.foreground
        }

        // Modified files get a different color
        if (value.isModified && !isSelected) {
            nameLabel.foreground = JBColor.BLUE
        }

        return panel
    }

    /**
     * Gets the bounds of the checkbox within the cell.
     * Used for mouse click detection.
     */
    fun getCheckboxBounds(): java.awt.Rectangle {
        return checkbox.bounds
    }

    /**
     * Gets the bounds of the close button within the cell.
     * Used for mouse click detection.
     */
    fun getCloseButtonBounds(): java.awt.Rectangle {
        return closeButton.bounds
    }
}
