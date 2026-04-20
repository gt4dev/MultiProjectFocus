package gtr.mpfocus.system_actions

import okio.Path
import okio.Path.Companion.toPath


data class FolderPath(val path: Path)


data class FilePath(
    val fileName: String,
    val folderPath: FolderPath,
) {
    val path: Path
        get() = folderPath.path / fileName

    companion object {

        fun create(path: Path): FilePath {
            val pathAsString = path.toString()
            // sometimes path is provided by human (eg. in tests scenarios, cmd line)
            // thus: linux separator '/' can be run on windows machines etc.
            // thus: both form / and \ are valid separators
            val lastUnixDividerIndex = pathAsString.lastIndexOf('/')
            val lastWindowsDividerIndex = pathAsString.lastIndexOf('\\')
            val lastPathDividerIndex = maxOf(lastUnixDividerIndex, lastWindowsDividerIndex)

            val folderPath = if (lastPathDividerIndex >= 0) {
                pathAsString.substring(0, lastPathDividerIndex)
            } else {
                ""
            }

            val fileName = if (lastPathDividerIndex >= 0) {
                pathAsString.substring(lastPathDividerIndex + 1)
            } else {
                pathAsString
            }

            return FilePath(
                fileName = fileName,
                folderPath = FolderPath(folderPath.toPath()),
            )
        }
    }
}