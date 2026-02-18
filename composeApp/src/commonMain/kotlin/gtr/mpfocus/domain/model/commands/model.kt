package gtr.mpfocus.domain.model.commands

import gtr.mpfocus.domain.model.core.ProjectFiles

sealed interface Command

object ProjectCurrent {
    data object OpenFolder : Command
    data class OpenFile(val file: ProjectFiles) : Command
}

object ProjectPinned {
    data class OpenFolder(val pinPosition: Int) : Command
    data class OpenFile(val pinPosition: Int, val file: ProjectFiles) : Command
}

object ProjectByPath {
    data class OpenFile(val projectPath: String, val file: ProjectFiles) : Command
}

data class LoadInitialData(val tomlFilePath: String) : Command

class CommandParseException(message: String) : IllegalArgumentException(message)
