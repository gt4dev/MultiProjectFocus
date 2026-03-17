package gtr.mpfocus.ui.main_screen

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import dev.hotest.HOTestCtx
import dev.hotest.hotest
import gtr.mpfocus.domain.model.core.Models.Project
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`given 'project actions mock' exists`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns current project`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns other projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'project repository mock' returns pinned projects`
import gtr.mpfocus.hotest.koinAddObject
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`then is executed 'project actions' command`
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`when 'main screen' is started`
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`when 'open folder' is clicked on project`
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainScreenOpenFolderTest {

    @Test
    fun `click button 'open folder' in current project`() {
        hotest(
            beforeTest = { `set up test background`() }
        ) {
            runComposeUiTest {
                koinAddObject(this)

                `given 'project repository mock' returns current project`(
                    Project(111, "proj 111")
                )
                `when 'main screen' is started`()
                `when 'open folder' is clicked on project`("section 'current-project'")
                `then is executed 'project actions' command`("open folder in current project")
            }
        }
    }

    @Test
    fun `click button 'open folder' in pinned project`() {
        hotest(
            beforeTest = { `set up test background`() }
        ) {
            runComposeUiTest {
                koinAddObject(this)

                `given 'project repository mock' returns pinned projects`(
                    Project(111, "proj 111", 1),
                    Project(222, "proj 222", 2),
                    Project(333, "proj 333", 3),
                )
                `when 'main screen' is started`()
                `when 'open folder' is clicked on project`("section 'pinned-projects', position 2")
                `then is executed 'project actions' command`("open folder in pinned project, pin: 2")
            }
        }
    }

    @Test
    fun `click button 'open folder' in regular project`() {
        hotest(
            beforeTest = { `set up test background`() }
        ) {
            runComposeUiTest {
                koinAddObject(this)

                `given 'project repository mock' returns other projects`(
                    Project(111, "proj 111"),
                    Project(222, "proj 222"),
                    Project(333, "proj 333"),
                )
                `when 'main screen' is started`()
                `when 'open folder' is clicked on project`("section 'other-projects', position 3")
                `then is executed 'project actions' command`("open folder in regular project, id: 333")
            }
        }
    }
}

fun HOTestCtx.`set up test background`() {
    `given 'project actions mock' exists`()
}