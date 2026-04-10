package gtr.mpfocus.domain.model.config.project_config

import gtr.mpfocus.domain.model.config.GlobalProjectConfig
import gtr.mpfocus.domain.model.core.ProjectFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GlobalTomlParserTest {

    private val parser = GlobalTomlParser()

    @Test
    fun `parse regular toml content`() {
        val toml = """
            [file2]
            name = "myFile2.md"
            desc = "desc of myFile2"

            [file3]
            name = "myFile3.md"
            desc = "desc of myFile3"
        """.trimIndent()

        val result = parser.parseConfig(toml)

        val expected = GlobalProjectConfig(
            fileNames = mapOf(
                ProjectFile.File2 to "myFile2.md",
                ProjectFile.File3 to "myFile3.md",
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `parse empty toml content`() {
        val toml = """
            # empty toml
        """.trimIndent()

        val result = parser.parseConfig(toml)

        val expected = GlobalProjectConfig(fileNames = emptyMap())
        assertEquals(expected, result)
    }

    @Test
    fun `parse invalid toml content returns null`() {
        val toml = """
            this is not valid toml
        """.trimIndent()

        val result = parser.parseConfig(toml)

        assertNull(result)
    }
}
