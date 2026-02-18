package gtr.mpfocus.domain.model.commands

import gtr.mpfocus.domain.model.core.ProjectFiles
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CommandParserTest {

    @Test
    fun `parse current project open folder`() {
        assertEquals(
            ProjectCurrent.OpenFolder,
            CommandParser.parse("ProjectCurrent.OpenFolder")
        )
    }

    @Test
    fun `parse current project open file`() {
        assertEquals(
            ProjectCurrent.OpenFile(ProjectFiles.File1),
            CommandParser.parse("ProjectCurrent.OpenFile(file:F1)")
        )
        assertEquals(
            ProjectCurrent.OpenFile(ProjectFiles.File5),
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
            ProjectPinned.OpenFile(pinPosition = 1, file = ProjectFiles.File2),
            CommandParser.parse("ProjectPinned(pinPosition:1).OpenFile(file:F2)")
        )
        assertEquals(
            ProjectPinned.OpenFile(pinPosition = 3, file = ProjectFiles.File7),
            CommandParser.parse("ProjectPinned(pinPosition:3).OpenFile(file:F7)")
        )
    }

    @Test
    fun `parse project by path open file`() {
        assertEquals(
            ProjectByPath.OpenFile(
                projectPath = """c:\some path to\folder with\project123""",
                file = ProjectFiles.File1
            ),
            CommandParser.parse(
                """ProjectByPath(projectPath:c:\some path to\folder with\project123).OpenFile(file:F1)"""
            )
        )
    }

    @Test
    fun `parse load initial data`() {
        assertEquals(
            LoadInitialData("""c:\path to\folder with\init-config.toml"""),
            CommandParser.parse(
                """LoadInitialData(tomlFilePath:c:\path to\folder with\init-config.toml)"""
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
