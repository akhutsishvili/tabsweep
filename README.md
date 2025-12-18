# TabSweep - IntelliJ IDEA Plugin

**Sweep through your tabs with ease.**

An enhanced tab management plugin for IntelliJ IDEA, inspired by Emacs buffer management.

## Features

- **Unified Tab View**: View all open tabs in a single popup dialog
- **Fuzzy Search**: Quickly find tabs by typing - no need to click a search box
- **Multi-Selection**: Select multiple tabs using checkboxes or spacebar
- **Bulk Close**: Close selected tabs at once, with or without closing the dialog
- **Keyboard-First**: Full keyboard navigation for power users
- **De-duplication**: Files open in multiple splits are shown only once

## Installation

### From JetBrains Marketplace (Coming Soon)
1. Open IntelliJ IDEA
2. Go to `Settings/Preferences` → `Plugins` → `Marketplace`
3. Search for "TabSweep"
4. Click `Install`

### Manual Installation
1. Download the latest release `.zip` file from [Releases](https://github.com/your-repo/tabsweep/releases)
2. Go to `Settings/Preferences` → `Plugins` → ⚙️ → `Install Plugin from Disk...`
3. Select the downloaded `.zip` file
4. Restart the IDE

## Usage

### Opening TabSweep

| Method | Action |
|--------|--------|
| **Keyboard** | `Ctrl+O` then `B` (two-key sequence) |
| **Menu** | `Window` → `TabSweep` |
| **Context Menu** | Right-click any editor tab → `TabSweep` |

### Keyboard Navigation

| Key | Action |
|-----|--------|
| `↑` / `↓` | Navigate through tabs |
| `Space` | Toggle selection (checkbox) of current tab |
| `Delete` | Close currently highlighted tab |
| `Enter` | Close all selected tabs and close dialog |
| `Escape` | Clear search (if searching) or close dialog |
| `Backspace` | Remove last character from search |

### Fuzzy Search

Just start typing! The search field appears automatically and filters tabs in real-time.

**How fuzzy matching works**: Characters must appear in order but don't need to be consecutive.

| Pattern | Matches |
|---------|---------|
| `tmd` | **T**ab**M**anager**D**ialog.kt |
| `bld` | **b**ui**ld**.gradle.kts |
| `svc` | Tab**S**weep**V**iew**C**ontroller.java |

Press `Escape` to clear the search, press again to close the dialog.

### Button Mnemonics

All buttons have keyboard shortcuts using `Alt` (Windows/Linux) or `Option` (macOS):

| Shortcut | Button | Action |
|----------|--------|--------|
| `Alt+A` | **S**elect **A**ll | Select all visible tabs |
| `Alt+D` | **D**eselect All | Deselect all visible tabs |
| `Alt+C` | **C**lose Selected | Close selected tabs (dialog stays open) |
| `Alt+X` | Close & E**x**it | Close selected tabs and close dialog |

### Mouse Controls

- **Click checkbox**: Toggle tab selection
- **Click X button**: Close that specific tab
- **Click tab row**: Highlight the tab

## Workflow Examples

### Close all test files
1. Open TabSweep (`Ctrl+O, B`)
2. Type `test` to filter
3. Press `Alt+A` to select all
4. Press `Alt+C` to close them

### Clean up Kotlin files
1. Open TabSweep
2. Type `.kt`
3. Use `↓` and `Space` to select specific files
4. Press `Alt+X` to close and exit

### Quick close a single file
1. Open TabSweep
2. Type part of the filename
3. Press `Delete` to close the highlighted file

## Development

### Prerequisites
- JDK 21 or later
- IntelliJ IDEA 2024.3 or later

### Building
```bash
./gradlew build
```

### Running (Sandbox IDE)
```bash
./gradlew runIde
```

### Testing
```bash
./gradlew test
```

### Creating Distribution
```bash
./gradlew buildPlugin
```
The plugin zip will be created in `build/distributions/`.

## Project Structure

```
src/
├── main/kotlin/com/github/tabmanager/
│   ├── actions/
│   │   └── OpenTabManagerAction.kt    # Action to open dialog
│   ├── services/
│   │   ├── TabManagerService.kt       # Service interface
│   │   └── TabManagerServiceImpl.kt   # Service implementation
│   ├── ui/
│   │   ├── TabInfo.kt                 # Tab data class
│   │   ├── TabListCellRenderer.kt     # Custom list renderer
│   │   └── TabManagerDialog.kt        # Main dialog
│   └── util/
│       └── FuzzyMatcher.kt            # Fuzzy search algorithm
└── test/kotlin/com/github/tabmanager/
    ├── ui/
    │   └── TabInfoTest.kt
    └── util/
        └── FuzzyMatcherTest.kt
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Changelog

### 1.0.0 (Initial Release)
- Tab list with file icons and modification indicators
- Fuzzy search filtering
- Checkbox selection with Select All/Deselect All
- Bulk close operations (with and without dialog exit)
- Full keyboard navigation
- Button mnemonics (Alt+A, Alt+D, Alt+C, Alt+X)
- Two-key shortcut: Ctrl+O, B
