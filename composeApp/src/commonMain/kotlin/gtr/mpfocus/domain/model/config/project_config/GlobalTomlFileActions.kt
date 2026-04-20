package gtr.mpfocus.domain.model.config.project_config

import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath

class GlobalTomlFileActions(
    private val fileSystemActions: FileSystemActions,
    private val appMainFolder: FolderPath,
) {

    fun fileExists(): Boolean {
        val filePath = FilePath(FILE_NAME, appMainFolder)
        return fileSystemActions.pathExists(filePath)
    }

    fun createFile() {
        fileSystemActions.createFolder(appMainFolder)
        val filePath = FilePath(FILE_NAME, appMainFolder)
        fileSystemActions.createFile(filePath)
        fileSystemActions.writeContent(filePath, FILE_DEFAULT_CONTENT)
    }

    fun readFileContent(): String {
        val filePath = FilePath(FILE_NAME, appMainFolder)
        return fileSystemActions.readFile(filePath)
    }

    companion object {
        const val FILE_NAME = "global-project-config.toml"

        private val FILE_DEFAULT_CONTENT = """
            # This file sets global file names used in projects in MultiProjectFocus.
            # Feel free to name project files in any way you like.
            #
            # Thanks of it, MultiProjectFocus knows which real file to open when you call CLI command like eg: ProjectCurrent.OpenFile(file:F1)
            # Without it, MultiProjectFocus maps 'file1' to file name 'file1.md', file2 to 'file2.md' etc.
            #
            # To reset this file: remove it and restart the app. MultiProjectFocus will re-create this file.
            
            [file1]
            name = "main-notes.md"
            desc = "File for notes regarding current project. For example: current goals / plans / tasks, notes about different aspects of the project, etc."

            [file2]
            name = "distractions.md"
            desc = "file for distractions you encounter when working on this project, but not related to it. For example: your thoughts, notes, etc. regarding other projects."

            # uncomment below section to define your own file names for other files
            # 
            # [file3]
            # name = ""
            # desc = ""
            # 
            # [file4]
            # name = ""
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
