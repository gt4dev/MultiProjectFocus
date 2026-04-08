# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Project Is

**Multi Project Focus (MPF)** is a keyboard shortcut-driven productivity app that provides ultra-fast access to project folders and files. Users trigger OS-level keyboard shortcuts (via AutoHotkey on Windows) that invoke the CLI with a command string, e.g.:

```
MultiProjectFocus.exe "ProjectCurrent.OpenFile(file:F1)"
```

The app either opens a folder/file on the OS, or launches a Compose GUI for project management. It works entirely offline, reading TOML config files.

## Commands

```bash
./gradlew jvmRun              # Run desktop app (JVM)
./gradlew jvmTest             # Run JVM unit tests
./gradlew :composeApp:jvmTest # Run desktop UI tests
./gradlew allTests            # Run all platform tests
./gradlew build               # Full build
./gradlew check               # All checks
./gradlew createDistributable # Build platform distributable → appBin/
```

To run a single test class:
```powershell
$env:GRADLE_USER_HOME = (Join-Path (Get-Location) '.gradle-user-home')
$env:ANDROID_USER_HOME = (Join-Path (Get-Location) '.android-user-home')
$env:JAVA_TOOL_OPTIONS = "-Dskiko.data.path=$((Join-Path (Get-Location) '.skiko'))"
.\gradlew.bat :composeApp:jvmTest --tests "gtr.mpfocus.ui.main_screen.MainScreenOpenFileTest"
```

> The Skiko env vars are required for Compose Desktop JVM UI tests in sandboxes — Skiko fails with `AccessDeniedException` if it can't write to its default path.

## Architecture

**Tech stack**: Kotlin Multiplatform + Compose Multiplatform, Room (SQLite), Koin DI, ktoml (TOML parsing), OkIO.

**Targets**: JVM Desktop (primary), Android, iOS.

**Entry point**: `jvmMain/main.kt` — parses CLI args, bootstraps Koin, and either executes a command (open file/folder) or launches the Compose window.

### Layer Structure

```
CLI args → CommandParser → CommandHandler
                                ↓
                   Domain (ProjectActions, ProjectReadModel)
                                ↓
                   Infrastructure (Room DB via ProjectRepository)
                                ↓
                   System Actions (OperatingSystemActions, FileSystemActions)
```

UI (Compose) is **lazy-loaded** — only initialized when the window needs to open. This keeps CLI-only commands fast.

**Koin modules**: defined per layer in each layer's `koinModule.kt` (infra, domain, app-level, UI). UI module is loaded lazily.

### Command DSL

Commands are sealed classes in `domain/model/commands/`:
- `ProjectCurrent.OpenFolder`, `ProjectCurrent.OpenFile(file: ProjectFile)`
- `ProjectPinned.OpenFolder(pinPosition)`, `ProjectPinned.OpenFile(pinPosition, file)`
- `LoadInitialData(tomlFilePath)` — imports a TOML config into the DB
- No command arg → opens the management UI

`ProjectFile` is an enum: `File0`–`File9` (10 files per project).

### Data Model

**Database**: Single Room entity `ProjectEntity` (projectId, folderPath, pinPosition?). Schema exports live in `composeApp/schemas/`.

**TOML config** (`assets/projects.init.toml` for format reference):
```toml
currentProject = 'myProject'

[projects]
myProject = 'C:\path\to\project'

[pinned-projects]
pin1 = 'myProject'
```

File associations per project are stored separately (not in TOML — in the DB or config service).

### Key Interfaces

- `ProjectRepository` — data access (Flows for reactive UI)
- `ProjectActions` — domain operations (open folder/file, set current, pin)
- `ProjectReadModel` — read-side queries for UI
- `OperatingSystemActions` — OS-level file/folder open
- `FileSystemActions` — filesystem checks (existence, read/write)
- `AppUi` — show messages, launch window (abstracted for testability)

## Testing

- `commonTest/` — domain logic, command parsing (portable)
- `jvmTest/` — Compose UI tests (MainScreen, dialogs)
- `androidHostTest/` — Android-specific unit tests
- Mocking: Mokkery (`@MockMany`, `@Mock` annotations)
