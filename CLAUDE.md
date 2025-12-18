# CLAUDE.md - TabSweep IntelliJ Plugin

## Project Overview

**TabSweep** is an IntelliJ IDEA plugin that provides enhanced tab management capabilities inspired by Emacs buffer management. The plugin allows users to view, search, select, and close multiple editor tabs through a keyboard-navigable dialog.

**Tagline**: "Sweep through your tabs with ease"

## Tech Stack

- **Language**: Kotlin 1.9.25
- **Build System**: Gradle with Kotlin DSL
- **Platform**: IntelliJ Platform SDK (2024.3+)
- **Gradle Plugin**: IntelliJ Platform Gradle Plugin 2.3.0
- **JDK**: 21
- **Testing**: JUnit 4, Mockito

## Project Structure

```
tabsweep/
├── build.gradle.kts              # Main build configuration
├── settings.gradle.kts           # Gradle settings
├── gradle.properties             # Gradle properties
├── CLAUDE.md                     # This file (developer docs)
├── README.md                     # User documentation
├── INITIAL_IMPLEMENTATION.md     # Implementation history
└── src/
    ├── main/
    │   ├── kotlin/
    │   │   └── com/github/tabmanager/
    │   │       ├── actions/
    │   │       │   └── OpenTabManagerAction.kt
    │   │       ├── services/
    │   │       │   ├── TabManagerService.kt
    │   │       │   └── TabManagerServiceImpl.kt
    │   │       ├── ui/
    │   │       │   ├── TabInfo.kt
    │   │       │   ├── TabListCellRenderer.kt
    │   │       │   └── TabManagerDialog.kt
    │   │       └── util/
    │   │           └── FuzzyMatcher.kt
    │   └── resources/
    │       └── META-INF/
    │           └── plugin.xml
    └── test/
        └── kotlin/
            └── com/github/tabmanager/
                ├── ui/
                │   └── TabInfoTest.kt
                └── util/
                    └── FuzzyMatcherTest.kt
```

## Build Commands

```bash
# Build the plugin
./gradlew build

# Run the plugin in a sandbox IDE instance
./gradlew runIde

# Run tests
./gradlew test

# Build distributable plugin zip
./gradlew buildPlugin

# Verify plugin compatibility
./gradlew verifyPlugin

# Clean build artifacts
./gradlew clean
```

## Key IntelliJ Platform APIs Used

### FileEditorManager
- `FileEditorManager.getInstance(project)` - Get the file editor manager
- `fileEditorManager.openFiles` - Get all open files
- `fileEditorManager.closeFile(file)` - Close a specific file

### DialogWrapper
- Base class for modal dialogs in IntelliJ
- Override `createCenterPanel()` to build the UI
- Override `doOKAction()` for OK button behavior
- Override `getPreferredFocusedComponent()` for initial focus

### AnAction
- Base class for actions (menu items, toolbar buttons, shortcuts)
- Override `actionPerformed(e)` to handle the action
- Override `update(e)` to control action availability

## Plugin Configuration

**Plugin ID**: `com.github.tabsweep`
**Plugin Name**: TabSweep

Configured in `src/main/resources/META-INF/plugin.xml`:
- Dependencies: `com.intellij.modules.platform` (core platform)
- Actions registered in `<actions>` section
- Services registered in `<extensions>` section

## Current Implementation Status

### Completed ✅

1. **Tab List Display**
   - [x] Show all open tabs in a list
   - [x] Display file icon
   - [x] Display file name (with * for modified)
   - [x] De-duplicate files open in multiple splits
   - [x] Sort tabs alphabetically
   - [x] Fuzzy search/filter tabs

2. **Selection Mechanism**
   - [x] Checkbox for each tab
   - [x] Toggle with spacebar
   - [x] Toggle with mouse click
   - [x] Select All button (Alt+A)
   - [x] Deselect All button (Alt+D)

3. **Tab Closing**
   - [x] Close selected tabs (keeps dialog open) - Alt+C
   - [x] Close selected tabs and exit - Alt+X
   - [x] Close individual tab with X button
   - [x] Close highlighted tab with Delete key

4. **UI/UX**
   - [x] Keyboard-navigable list (Up/Down arrows)
   - [x] Proper focus management
   - [x] Two-key shortcut: Ctrl+O, B
   - [x] Fuzzy search appears on typing
   - [x] Escape clears search, then closes dialog
   - [x] Dark theme support (automatic with IntelliJ)

5. **Testing**
   - [x] FuzzyMatcher unit tests
   - [x] TabInfo unit tests

### Future Enhancements (Nice to Have)
- [ ] Confirm before closing modified files
- [ ] Preview file on hover/selection
- [ ] Group tabs by project module
- [ ] Pin/favorite tabs
- [ ] Tab history (recently closed)
- [ ] Remember dialog size/position

## Code Style Guidelines

- Use Kotlin idioms (data classes, extension functions, lambdas)
- Follow IntelliJ Platform conventions for service registration
- Services must be classes, not `object` declarations
- Use nullable types appropriately with Kotlin null safety

## Notes for Claude Code

When working on this project:

1. **Always run the IDE sandbox** after changes to test functionality:
   ```bash
   ./gradlew runIde
   ```

2. **Plugin.xml is critical** - any syntax error will prevent the plugin from loading

3. **Services must not be Kotlin objects** - use classes, not `object` declarations

4. **UI must run on EDT** - use `ApplicationManager.getApplication().invokeLater { }` for background operations

5. **Test incrementally** - small changes, test often

6. **Run tests before committing**:
   ```bash
   ./gradlew test
   ```

## Resources

- [IntelliJ Platform SDK Docs](https://plugins.jetbrains.com/docs/intellij/)
- [IntelliJ Platform Gradle Plugin](https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html)
- [Kotlin for Plugin Developers](https://plugins.jetbrains.com/docs/intellij/using-kotlin.html)
- [Dialog Wrapper Guide](https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html)

## Version History

- **1.0.0**: Initial release with fuzzy search, keyboard navigation, and bulk tab closing
