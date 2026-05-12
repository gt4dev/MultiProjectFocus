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
            # This config globally defines project file names used in all projects in MultiProjectFocus.
            # Feel free to change it as you like.
            #
            # To reset this file: 1/ remove it, 2/ restart the app. MPF will create this file on a start.
            # 
            # Thanks to this file, MPF knows how to map a 'file number' to a 'real file name', e.g. "file1" to "main-notes.md".
            # Later, you can quickly access numbered files using keyboard shortcuts like:
            # - "open file1 from the current project"
            # - "open file2 from pinned project no. 3"
            #
            # Without this config, MPF uses default file names like: file1='file1.md', file2='file2.md' , ...
            #
            # Uncomment particular section eg: [file5] to activate it.
            #
            #
            #
            
            [file1]
            name = 'main-notes.md'
            desc = 'File for any notes regarding this project. For example: 1/ current plans / tasks, 2/ notes how to do sth, etc.'
            
            [file2]
            name = 'distractions.md'
            desc = 'File for distractions not related to this project, but worth noting for later. For example: 1/ your ideas what to change on "other projects", 2/ tasks to do later etc.'
            
            # 
            # [file3]
            # name = 'another-file-nr3.txt'
            # desc = ''
            # 
            # [file4]
            # name = 'yet-another-file-nr4.txt'
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
