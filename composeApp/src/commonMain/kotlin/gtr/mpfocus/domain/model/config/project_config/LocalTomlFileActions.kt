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
            # This config is optional, but useful to customize particular project config.
            # If not set then global config is used.
            # Here you can set `project file names` used only in this project.
            # You can override all or only selected configs.
            # For example:
            #   if the global `file1` is `notes_1.txt`
            #   then you can rename it in this project to `my-notes.txt`
            # 
            #   just uncomment and set:
            #   ```
            #   [file1]
            #   name = "my-notes.md"
            #   ```
            # 
            # Uncomment particular section eg: [file5] to activate it.
            # 
            # To reset this file: delete it, 2/ in app in selected project click the "Project config" option.
            # Then MPF creates this file.
            #
            #
            #
            # projectName = 'your custom project name'
            # projectDescription = 'some description of the project'
            # 
            # 
            # 
            # [file1]
            # name = 'your-name-for-file1.txt'
            # desc = ''
            # 
            # [file2]
            # name = 'your-name-for-file2.txt'
            # desc = ''
            # 
            # [file3]
            # name = ''
            # desc = ''
            # 
            # [file4]
            # name = ''
            # desc = ''
            # 
            # [file5]
            # name = ''
            # desc = ''
            # 
            # [file6]
            # name = ''
            # desc = ''
            # 
            # [file7]
            # name = ''
            # desc = ''
            # 
            # [file8]
            # name = ''
            # desc = ''
            # 
            # [file9]
            # name = ''
            # desc = ''
            # 
            # [file0]
            # name = ''
            # desc = ''            
        """.trimIndent()
    }
}
