package gtr.mpfocus.domain.model.init_data

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.decodeFromString
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectTomlTest {

    @Test
    fun `decode sample project toml`() {
        val toml = """
            currentProject = "alias1"
            
            [pinned-projects]
            pin0 = "alias1"
            pin1 = "alias2"
            pin2 = "alias3"
            
            [projects]
            alias1 = "path to the project"
            alias2 = "path to MPF project"
            alias3 = "path to HOT project"
        """.trimIndent()

        val projectToml = Toml.decodeFromString<ProjectToml>(toml)

        assertEquals("alias1", projectToml.currentProject)
        assertEquals(
            mapOf(
                "pin0" to "alias1",
                "pin1" to "alias2",
                "pin2" to "alias3",
            ),
            projectToml.pinnedProjects
        )
        assertEquals(
            mapOf(
                "alias1" to "path to the project",
                "alias2" to "path to MPF project",
                "alias3" to "path to HOT project",
            ),
            projectToml.projects
        )
    }
}
