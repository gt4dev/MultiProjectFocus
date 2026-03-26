package gtr.mpfocus.ui.create_project_dialog

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import dev.hotest.hotest
import dev.hotest.variants.Async.variant
import dev.hotest.variants.Async.variants
import gtr.mpfocus.hotest.koinAddObject
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`given 'file system mock' exists`
import gtr.mpfocus.system_actions.FileSystemActionsSteps.`then 'file system mock' creates folder`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`given 'create project dialog' sets 'create folder panel' as`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`given 'create project dialog' shows 'create folder panel' with no folder info`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`given in 'create project dialog' user enters project path`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`given in 'create project dialog' user marks 'set as current project' as`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`then 'create folder panel' is`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`then in 'create project mock' is called create project`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`when 'create project dialog' is shown`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`when in 'create folder panel' is clicked button 'create folder'`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`when in 'create folder panel' user clicks button 'add'`
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CreateProjectDialogTest {

    @Test
    fun `test 'create folder panel' visibility`() {
        hotest {
            runComposeUiTest {
                koinAddObject(this)

                `given 'file system mock' exists`()

                variants("panel visibility =") {

                    variant("visible") {
                        `given 'create project dialog' sets 'create folder panel' as`("visible")
                        `when 'create project dialog' is shown`()
                        `then 'create folder panel' is`("visible")
                    }

                    variant("invisible") {
                        `given 'create project dialog' sets 'create folder panel' as`("invisible")
                        `when 'create project dialog' is shown`()
                        `then 'create folder panel' is`("invisible")
                    }
                }
            }
        }
    }

    @Test
    fun `test 'create folder panel' click 'create folder'`() {
        hotest {
            runComposeUiTest {
                koinAddObject(this)

                `given 'file system mock' exists`()
                `given 'create project dialog' shows 'create folder panel' with no folder info`("/my/sample/path123")
                `when 'create project dialog' is shown`()
                `when in 'create folder panel' is clicked button 'create folder'`()
                `then 'file system mock' creates folder`("/my/sample/path123")
            }
        }
    }

    @Test
    fun `test creating project that is set as current project`() {
        hotest {
            runComposeUiTest {
                koinAddObject(this)

                `given 'file system mock' exists`()
                `when 'create project dialog' is shown`()
                `given in 'create project dialog' user enters project path`("my/path/123")

                variants("'set as current project' ='") {

                    variant("checked") {
                        `given in 'create project dialog' user marks 'set as current project' as`("checked")
                        `when in 'create folder panel' user clicks button 'add'`()
                        `then in 'create project mock' is called create project`(
                            "my/path/123",
                            "is current project"
                        )
                    }

                    variant("unchecked") {
                        `given in 'create project dialog' user marks 'set as current project' as`("unchecked")
                        `when in 'create folder panel' user clicks button 'add'`()
                        `then in 'create project mock' is called create project`(
                            "my/path/123",
                            "is not current project"
                        )
                    }
                }
            }
        }
    }
}
