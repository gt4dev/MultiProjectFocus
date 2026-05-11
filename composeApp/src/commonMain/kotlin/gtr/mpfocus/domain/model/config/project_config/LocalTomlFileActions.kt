package gtr.mpfocus.domain.model.config.project_config

import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath

class LocalTomlFileActions(
    private val fileSystemActions: FileSystemActions,
) {

    fun filePath(projectFolder: FolderPath): FilePath =
        FilePath(FILE_NAME, projectFolder)

    fun fileExists(projectFolder: FolderPath): Boolean =
        fileSystemActions.pathExists(filePath(projectFolder))

    fun createFile(projectFolder: FolderPath) {
        val filePath = filePath(projectFolder)
        fileSystemActions.createFile(filePath)
        fileSystemActions.writeContent(filePath, FILE_DEFAULT_CONTENT)
    }

    fun readFileContent(projectFolder: FolderPath): String =
        fileSystemActions.readFile(filePath(projectFolder))

    companion object {
        const val FILE_NAME = ".mpf-local-project-config.toml"

        private val FILE_DEFAULT_CONTENT = """
            # LOCAL PROJECT CONFIG
            # 
            # This file is optional.
            # 
            # This file allows you to set file names only for this project. It allows overriding global settings.
            # For example:
            #   if the global `file1` is `notes_1.txt`
            #   then you can rename it in this project to `my-notes.txt` or any other real file
            # 
            #   just uncomment and set:
            #   ```
            #   [file1]
            #   name = "my-notes.md"
            #   ```
            # 
            # Thanks to this file, MultiProjectFocus knows how to map a `file number` to a `real file name`.
            # For example, `file1`, `file2`, etc. can be mapped to real files like `main-notes.txt`, `side-notes.txt`, ...
            # 
            # Later, you can quickly access numbered files using keyboard shortcuts like:
            # - "open file1 from the current project"
            # - "open file2 from pinned project no. 3"
            # 
            # You can remove this file. In such a case, MPF uses global file names.
            # 
            # To quickly create this file, click the "Project config" option in the project context menu.
            # It creates this file for the project and opens it for further editing.
            #
            # Uncomment particular [file..] sections to activate them.
            #
            #
            #
            # projectName = 'your custom project name'
            # projectDescription = 'some description of the project'
            # 
            # [file1]
            # name = "sample-file-name1.txt"
            # desc = ""
            # 
            # [file2]
            # name = "sample-file-name2.txt"
            # desc = ""
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
