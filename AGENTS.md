# Notes

## Skiko JVM test workaround

When running Compose Desktop JVM UI tests in the sandbox, Skiko may fail with `AccessDeniedException` because it tries to unpack native files into the default user home under `.skiko`.

Use a workspace-local Skiko path:

```powershell
$env:JAVA_TOOL_OPTIONS = "-Dskiko.data.path=$((Join-Path (Get-Location) '.skiko'))"
```

Example with the existing local Gradle cache overrides:

```powershell
$env:GRADLE_USER_HOME = (Join-Path (Get-Location) '.gradle-user-home')
$env:ANDROID_USER_HOME = (Join-Path (Get-Location) '.android-user-home')
$env:JAVA_TOOL_OPTIONS = "-Dskiko.data.path=$((Join-Path (Get-Location) '.skiko'))"
.\gradlew.bat :composeApp:jvmTest --tests "gtr.mpfocus.ui.main_screen.MainScreenOpenFileTest"
```
