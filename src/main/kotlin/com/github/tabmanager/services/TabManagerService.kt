package com.github.tabmanager.services

import com.github.tabmanager.ui.TabInfo

/**
 * Service interface for managing editor tabs.
 */
interface TabManagerService {
    /**
     * Gets a list of all currently open tabs.
     * Files open in multiple splits are de-duplicated.
     *
     * @return List of TabInfo objects representing open tabs
     */
    fun getOpenTabs(): List<TabInfo>

    /**
     * Closes a single tab.
     *
     * @param tabInfo The tab to close
     */
    fun closeTab(tabInfo: TabInfo)

    /**
     * Closes multiple tabs.
     *
     * @param tabs The list of tabs to close
     */
    fun closeTabs(tabs: List<TabInfo>)

    /**
     * Refreshes the tab info for a specific file (e.g., after modification status changes).
     *
     * @param tabInfo The tab to refresh
     * @return Updated TabInfo or null if the file is no longer open
     */
    fun refreshTabInfo(tabInfo: TabInfo): TabInfo?
}
