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
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`given 'create project dialog' shows 'create folder panel' no folder`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`then 'create folder panel' is`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`when 'create project dialog' is shown`
import gtr.mpfocus.ui.create_project_dialog.CreateProjectDialogSteps.`when in 'create folder panel' is clicked button 'create folder'`
import kotlin.test.Test

@Suppress("ClassName")
@OptIn(ExperimentalTestApi::class)
class `test 'create folder panel' nested in dialog` {

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
                `given 'create project dialog' shows 'create folder panel' no folder`("/my/sample/path123")
                `when 'create project dialog' is shown`()
                `when in 'create folder panel' is clicked button 'create folder'`()
                `then 'file system mock' creates folder`("/my/sample/path123")
            }
        }
    }
}
