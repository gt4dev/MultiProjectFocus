package gtr.mpfocus.domain.model.commands

import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.system_actions.FilePath
import gtr.mpfocus.system_actions.FolderPath
import okio.Path.Companion.toPath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CommandParserTest {

    @Test
    fun `parse empty command returns no command`() {
        assertEquals(
            NoExplicitCommand,
            CommandParser.parse("")
        )
        assertEquals(
            NoExplicitCommand,
            CommandParser.parse("   ")
        )
    }

    @Test
    fun `parse current project open folder`() {
        assertEquals(
            ProjectCurrent.OpenFolder,
            CommandParser.parse("ProjectCurrent.OpenFolder")
        )
    }

    @Test
    fun `parse show ui`() {
        assertEquals(
            ShowUi,
            CommandParser.parse("ShowUI")
        )
    }

    @Test
    fun `parse current project open file`() {
        assertEquals(
            ProjectCurrent.OpenFile(ProjectFile.File1),
            CommandParser.parse("ProjectCurrent.OpenFile(file:F1)")
        )
        assertEquals(
            ProjectCurrent.OpenFile(ProjectFile.File5),
            CommandParser.parse("ProjectCurrent.OpenFile(file:F5)")
        )
    }

    @Test
    fun `parse pinned project open folder`() {
        assertEquals(
            ProjectPinned.OpenFolder(pinPosition = 3),
            CommandParser.parse("ProjectPinned(pinPosition:3).OpenFolder")
        )
    }

    @Test
    fun `parse pinned project open file`() {
        assertEquals(
            ProjectPinned.OpenFile(pinPosition = 1, file = ProjectFile.File2),
            CommandParser.parse("ProjectPinned(pinPosition:1).OpenFile(file:F2)")
        )
        assertEquals(
            ProjectPinned.OpenFile(pinPosition = 3, file = ProjectFile.File7),
            CommandParser.parse("ProjectPinned(pinPosition:3).OpenFile(file:F7)")
        )
    }

    @Test
    fun `parse project by path open file`() {
        assertEquals(
            ProjectByPath.OpenFile(
                projectPath = """c:\some path to\folder with\project123""",
                file = ProjectFile.File1
            ),
            CommandParser.parse(
                """ProjectByPath(folder:c:\some path to\folder with\project123).OpenFile(file:F1)"""
            )
        )
    }

    @Test
    fun `parse load initial data`() {
        assertEquals(
            LoadInitialData(
                FilePath(
                    fileName = "init-config.toml",
                    folderPath = FolderPath("""c:\path to\folder123""".toPath())
                )
            ),
            CommandParser.parse(
                """LoadInitialData(file:c:\path to\folder123\init-config.toml)"""
            )
        )
    }

    @Test
    fun `parse unsupported command throws`() {
        assertFailsWith<CommandParseException> {
            CommandParser.parse("Unsupported.Action")
        }
    }
}
