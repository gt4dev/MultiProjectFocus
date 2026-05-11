package gtr.mpfocus.domain.model.config.project_config

import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath

class GlobalTomlFileActions(
    private val fileSystemActions: FileSystemActions,
    private val appMainFolder: FolderPath,
) {
    val filePath: FilePath
        get() = FilePath(FILE_NAME, appMainFolder)

    fun fileExists(): Boolean {
        return fileSystemActions.pathExists(filePath)
    }

    fun createFile() {
        fileSystemActions.createFolder(appMainFolder)
        fileSystemActions.createFile(filePath)
        fileSystemActions.writeContent(filePath, FILE_DEFAULT_CONTENT)
    }

    fun readFileContent(): String {
        return fileSystemActions.readFile(filePath)
    }

    companion object {
        const val FILE_NAME = "global-project-config.toml"

        private val FILE_DEFAULT_CONTENT = """
            # GLOBAL PROJECT CONFIG
            #
            # This file globally defines file names used in all projects in MultiProjectFocus.
            # Feel free to change it as you like.
            #
            # To reset this file: remove it and restart the app. MPF will create this file on a start.
            # 
            # Thanks to this file, MPF knows how to map a 'file number' to a 'real file name',
            # e.g. "file1" to "main-notes.md".
            # Later, MPF can correctly perform commands like 'open file1 from the current project'.
            # Without this file, MPF maps file1, file2, ... to default file names like:
            # 'file1.md', 'file2.md', ...
            #
            # Uncomment particular [file..] sections to activate them.
            #
            #
            #
           
            [file1]
            name = "main-notes.md"
            desc = "File for notes regarding current project. For example: current goals / plans / tasks, notes about different aspects of the project, etc."

            [file2]
            name = "distractions.md"
            desc = "file for distractions you encounter when working on this project, but not related to it. For example: your thoughts, notes, etc. regarding other projects."

            # 
            # [file3]
            # name = "another-file-nr3.txt"
            # desc = ""
            # 
            # [file4]
            # name = "yet-another-file-nr4.txt"
            # desc = ""
            # 
            # [file5]
            # name = ""
            # desc = ""
            # 
            # [file6]
            # name = ""
            # desc = ""
            # 
            # [file7]
            # name = ""
            # desc = ""
            # 
            # [file8]
            # name = ""
            # desc = ""
            # 
            # [file9]
            # name = ""
            # desc = ""
            # 
            # [file0]
            # name = ""
            # desc = ""
        """.trimIndent()
    }
}
