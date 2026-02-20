package gtr.mpfocus.domain.model.commands

import gtr.mpfocus.domain.model.core.ProjectFiles
import gtr.mpfocus.system_actions.FilePath
import okio.Path.Companion.toPath

object CommandParser {

    private val projectCurrentOpenFilePattern = Regex("""^ProjectCurrent\.OpenFile\(file:(F\d+)\)$""")
    private val projectPinnedOpenFolderPattern =
        Regex("""^ProjectPinned\(pinPosition:(\d+)\)\.OpenFolder$""")
    private val projectPinnedOpenFilePattern =
        Regex("""^ProjectPinned\(pinPosition:(\d+)\)\.OpenFile\(file:(F\d+)\)$""")
    private val projectByPathOpenFilePattern =
        Regex("""^ProjectByPath\(folder:(.+)\)\.OpenFile\(file:(F\d+)\)$""")
    private val loadInitialDataPattern = Regex("""^LoadInitialData\(file:(.+)\)$""")

    fun parse(rawCommand: String): Command {
        val command = normalizeCommand(rawCommand)

        if (command == "ProjectCurrent.OpenFolder") {
            return ProjectCurrent.OpenFolder
        }

        projectCurrentOpenFilePattern.matchEntire(command)?.let { match ->
            return ProjectCurrent.OpenFile(
                file = parseProjectFile(match.groupValues[1])
            )
        }

        projectPinnedOpenFolderPattern.matchEntire(command)?.let { match ->
            return ProjectPinned.OpenFolder(
                pinPosition = parsePinPosition(match.groupValues[1])
            )
        }

        projectPinnedOpenFilePattern.matchEntire(command)?.let { match ->
            return ProjectPinned.OpenFile(
                pinPosition = parsePinPosition(match.groupValues[1]),
                file = parseProjectFile(match.groupValues[2])
            )
        }

        projectByPathOpenFilePattern.matchEntire(command)?.let { match ->
            return ProjectByPath.OpenFile(
                projectPath = match.groupValues[1],
                file = parseProjectFile(match.groupValues[2])
            )
        }

        loadInitialDataPattern.matchEntire(command)?.let { match ->
            val path = match.groupValues[1]
            return LoadInitialData(
                tomlFilePath = FilePath(path.toPath())
            )
        }

        throw CommandParseException("Unsupported command: $rawCommand")
    }

    private fun normalizeCommand(rawCommand: String): String {
        val trimmed = rawCommand.trim()
        if (trimmed.isEmpty()) {
            throw CommandParseException("Command is empty")
        }
        return trimmed
    }

    private fun parsePinPosition(rawPinPosition: String): Int {
        val pinPosition = rawPinPosition.toIntOrNull()
            ?: throw CommandParseException("Invalid pinPosition: $rawPinPosition")
        if (pinPosition < 0) {
            throw CommandParseException("pinPosition must be >= 0, actual: $pinPosition")
        }
        return pinPosition
    }

    private fun parseProjectFile(rawFile: String): ProjectFiles {
        if (!rawFile.startsWith("F")) {
            throw CommandParseException("Invalid file token: $rawFile")
        }

        val fileIndex = rawFile.removePrefix("F").toIntOrNull()
            ?: throw CommandParseException("Invalid file token: $rawFile")

        return ProjectFiles.entries.getOrNull(fileIndex)
            ?: throw CommandParseException(
                "File token out of range: $rawFile (supported F0..F${ProjectFiles.entries.lastIndex})"
            )
    }
}
