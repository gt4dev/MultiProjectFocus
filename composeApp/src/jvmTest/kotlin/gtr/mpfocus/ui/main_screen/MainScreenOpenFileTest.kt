package gtr.mpfocus.ui.main_screen

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import dev.hotest.HOTestCtx
import dev.hotest.hotest
import dev.hotest.variants.variant
import dev.hotest.variants.variants
import gtr.mpfocus.domain.model.core.Models.Project
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`given 'project actions mock' exists`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns current project`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns other projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns pinned projects`
import gtr.mpfocus.hotest.koinAddObject
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`then is executed 'project actions' command`
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`then 'open file' options for project are`
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`when 'main screen' is started`
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`when button 'open file' is clicked for project and file`
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainScreenOpenFileTest {

    @Test
    fun `click button 'open file' - happy day scenario`() {
        hotest {
            runComposeUiTest {
                koinAddObject(this)

                `set up test`()

                `when 'main screen' is started`()

                variants {

                    variant("open file from current project") {

                        `when button 'open file' is clicked for project and file`(
                            "section 'current-project'",
                            "file3.md"
                        )
                        `then is executed 'project actions' command`("open file in current project, file: File3")
                    }

                    variant("open file from pinned project") {

                        `when button 'open file' is clicked for project and file`(
                            "section 'pinned-projects', position 2",
                            "file3.md",
                        )
                        `then is executed 'project actions' command`("open file in pinned project, pin: 2, file: File3")
                    }

                    variant("open file from regular project") {

                        `when button 'open file' is clicked for project and file`(
                            "section 'other-projects', position 2",
                            "file3.md",
                        )
                        `then is executed 'project actions' command`("open file in regular project, id: 555, file: File3")
                    }
                }
            }
        }
    }

    @Test
    fun `open file dropdown shows File1 to File9 and File0 last`() {
        hotest {
            runComposeUiTest {
                koinAddObject(this)

                `set up test`()

                `when 'main screen' is started`()

                `then 'open file' options for project are`(
                    "section 'current-project'",
                    *((1..9).map { "File $it: file$it.md" } + "File 0: file0.md").toTypedArray(),
                )
            }
        }
    }
}

fun HOTestCtx.`set up test`() {
    `given 'project actions mock' exists`()

    `given 'project repository mock' returns current project`(
        Project(1000, "proj 1000")
    )

    `given 'project repository mock' returns pinned projects`(
        Project(111, "proj 111", 1),
        Project(222, "proj 222", 2),
        Project(333, "proj 333", 3),
    )

    `given 'project repository mock' returns other projects`(
        Project(444, "proj 444"),
        Project(555, "proj 555"),
        Project(666, "proj 666"),
        Project(777, "proj 777"),
    )
}
