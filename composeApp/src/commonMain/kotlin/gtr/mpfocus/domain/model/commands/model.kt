package gtr.mpfocus.domain.model.commands

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FilePath

sealed interface Command

object NoExplicitCommand : Command

object ShowUi : Command

object ProjectCurrent {
    data object OpenFolder : Command
    data class OpenFile(val file: ProjectFile) : Command
}

object ProjectPinned {
    data class OpenFolder(val pinPosition: Int) : Command
    data class OpenFile(val pinPosition: Int, val file: ProjectFile) : Command
}

object ProjectByPath {
    data class OpenFile(val projectPath: String, val file: ProjectFile) : Command
}

data class LoadInitialData(val tomlFilePath: FilePath) : Command

class CommandParseException(message: String) : IllegalArgumentException(message)
