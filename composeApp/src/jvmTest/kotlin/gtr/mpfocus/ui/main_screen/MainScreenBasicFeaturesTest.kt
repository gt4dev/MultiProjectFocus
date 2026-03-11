package gtr.mpfocus.ui.main_screen

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import gtr.hotest.HOTestCtx
import gtr.hotest.hotest
import gtr.mpfocus.domain.model.core.Models.Project
import gtr.mpfocus.domain.model.core.ProjectActionsSteps.`given 'fake project actions' exists`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'fake project repository' returns current project`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'fake projects repo' returns other projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`given 'fake projects repo' returns pinned projects`
import gtr.mpfocus.domain.repository.RepositorySteps.`then 'project repo' sets current project as`
import gtr.mpfocus.hotest.koinAddObject
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`then 'main screen' shows other projects`
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`then 'main screen' shows pinned projects`
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`when 'main screen' is started`
import gtr.mpfocus.ui.main_screen.MainScreenSteps.`when 'set current' is clicked on row with project id`
import kotlin.test.Test


@OptIn(ExperimentalTestApi::class)
class MainScreenBasicFeaturesTest {

    @Test
    fun `ui correctly show projects`() {
        hotest(
            beforeTest = { `set up test data`() }
        ) {
            runComposeUiTest {
                koinAddObject(this)

                `when 'main screen' is started`()

                `then 'main screen' shows pinned projects`(
                    Project(path = "proj 111"),
                    Project(path = "proj 222"),
                    Project(path = "proj 333"),
                )

                `then 'main screen' shows other projects`(
                    Project(path = "proj 444"),
                    Project(path = "proj 555"),
                    Project(path = "proj 666"),
                )
            }
        }
    }

    @Test
    fun `click set current`() {
        hotest(
            beforeTest = { `set up test data`() }
        ) {
            runComposeUiTest {
                koinAddObject(this)

                `when 'main screen' is started`()

                `when 'set current' is clicked on row with project id`(333)

                `then 'project repo' sets current project as`(333)
            }
        }
    }
}


fun HOTestCtx.`set up test data`() {

    `given 'fake project actions' exists`()

    `given 'fake project repository' returns current project`(
        Project(1000, "proj 000")
    )

    `given 'fake projects repo' returns pinned projects`(
        Project(111, "proj 111", 1),
        Project(222, "proj 222", 2),
        Project(333, "proj 333", 3),
    )

    `given 'fake projects repo' returns other projects`(
        Project(444, "proj 444"),
        Project(555, "proj 555"),
        Project(666, "proj 666"),
    )
}
