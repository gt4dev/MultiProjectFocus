package gtr.mpfocus.system_actions

import okio.Path
import okio.Path.Companion.toPath
import kotlin.test.Test
import kotlin.test.assertEquals

class FilePathTest {

    val pathDivider = Path.DIRECTORY_SEPARATOR

    @Test
    fun `regular path with filename`() {

        val path = "folder${pathDivider}subfolder${pathDivider}file.txt".toPath()

        assertEquals(
            FilePath(
                fileName = "file.txt",
                folderPath = FolderPath("folder${pathDivider}subfolder".toPath()),
            ),
            FilePath.create(path)
        )
    }

    @Test
    fun `file path strange folder`() {
        assertEquals(
            FilePath(
                fileName = "file.txt",
                folderPath = FolderPath("".toPath()),
            ),
            FilePath.create("file.txt".toPath())
        )

        assertEquals(
            FilePath(
                fileName = "file.txt",
                folderPath = FolderPath(".".toPath()),
            ),
            FilePath.create(".${pathDivider}file.txt".toPath())
        )
    }
}
