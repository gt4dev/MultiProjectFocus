package gtr.mpfocus.system_actions

// todo: replace with Path from 'kotlinx-io' or 'okio.Path'
data class TmpPath(
    val path: String
)

interface FileSystemActions {

    // check whether file or folder exists
    fun pathExists(path: TmpPath): Boolean

    fun createFolder(path: TmpPath): Boolean
}