package gtr.mpfocus.ui.main_screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import dev.mokkery.matcher.any
import dev.mokkery.verifySuspend
import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.core.Models
import gtr.mpfocus.domain.model.core.ProjectActions
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.hotest.CucumberExpressionMatcher
import gtr.mpfocus.ui.composables.OptionsSplitButtonTestTags
import gtr.mpfocus.ui.composables.ProjectRowTestTags

@OptIn(ExperimentalTestApi::class)
object MainScreenSteps {

    fun HOTestCtx.`when 'main screen' is started`() {
        val cut: ComposeUiTest = koin.get()
        cut.setContent {
            MaterialTheme {
                MainScreenContainer(
                    viewModelFactory = MainScreenViewModelFactory(
                        koin.get(),
                        koin.get(),
                    ).create(),
                )
            }
        }
    }

    fun HOTestCtx.`when 'set current' is clicked on row with project id`(projectId: Long) {
        val cut: ComposeUiTest = koin.get()
        with(cut) {
            waitForIdle()
            onNodeWithTag(
                ProjectRowTestTags.buttonSetCurrent(projectId)
            ).performClick()
            waitForIdle()
        }
    }

    fun HOTestCtx.`then 'main screen' shows pinned projects`(
        vararg pinnedProjects: Models.Project
    ) {
        val cut: ComposeUiTest = koin.get()

        val projectsSectionMatcher = hasTestTag(MainScreenTestTags.PINNED_PROJECTS_SECTION)
        val projectRowMatcher = hasProjectRowTag() and hasAnyAncestor(projectsSectionMatcher)

        cut.onNodeWithTag(MainScreenTestTags.PINNED_PROJECTS_SECTION).assertExists()
        cut.onAllNodes(projectRowMatcher, useUnmergedTree = true).assertCountEquals(pinnedProjects.size)

        pinnedProjects.forEach { project ->
            cut.onNode(
                hasText(project.path!!) and hasAnyAncestor(projectsSectionMatcher),
                useUnmergedTree = true,
            ).assertExists()
        }
    }

    fun HOTestCtx.`then 'main screen' shows other projects`(
        vararg otherProjects: Models.Project
    ) {
        val cut: ComposeUiTest = koin.get()

        val projectsSectionMatcher = hasTestTag(MainScreenTestTags.OTHER_PROJECTS_SECTION)
        val projectRowMatcher = hasProjectRowTag() and hasAnyAncestor(projectsSectionMatcher)

        cut.onNodeWithTag(MainScreenTestTags.OTHER_PROJECTS_SECTION).assertExists()
        cut.onAllNodes(projectRowMatcher, useUnmergedTree = true).assertCountEquals(otherProjects.size)

        otherProjects.forEach { project ->
            cut.onNode(
                hasText(project.path!!) and hasAnyAncestor(projectsSectionMatcher),
                useUnmergedTree = true,
            ).assertExists()
        }
    }

    fun HOTestCtx.`when 'open folder' is clicked on project`(project: String) {
        val cut: ComposeUiTest = koin.get()
        with(cut) {
            waitForIdle()
            val selectedRowMatcher = findProjectRowMatcher(project)
            onNode(
                hasText("Open folder") and hasAnyAncestor(selectedRowMatcher),
                useUnmergedTree = true,
            ).assertExists().performClick()

            waitForIdle()
        }
    }

    fun HOTestCtx.`when 'open file' is clicked on project`(project: String, file: ProjectFile) {
        val cut: ComposeUiTest = koin.get()
        with(cut) {
            waitForIdle()
            val selectedRowMatcher = findProjectRowMatcher(project)
            onNode(selectedRowMatcher, useUnmergedTree = true).performScrollTo()

            onNode(
                hasTestTag(OptionsSplitButtonTestTags.TRAILING_BUTTON) and hasAnyAncestor(selectedRowMatcher),
            ).assertExists().performClick()

            onNodeWithText(file.name).assertExists().performClick()
            waitForIdle()
        }
    }

    // it matches only nodes whose TestTag is exactly project-row-<number>
    private fun hasProjectRowTag(): SemanticsMatcher {
        val rowTagRegex = Regex("""^project-row-\d+$""")
        return SemanticsMatcher("project row tag matcher") { node ->
            if (SemanticsProperties.TestTag !in node.config) {
                false
            } else {
                rowTagRegex.matches(node.config[SemanticsProperties.TestTag])
            }
        }
    }

    fun HOTestCtx.`then is executed 'project actions' command`(command: String) {
        val projectActions: ProjectActions = koin.get()
        val cucumberExpMatcher = CucumberExpressionMatcher(command)
        when {

            cucumberExpMatcher.matches("open folder in current project") -> {
                verifySuspend {
                    projectActions.openCurrentProjectFolder(any(), any())
                }
            }

            cucumberExpMatcher.matches("open file in current project, file: {word}") -> {
                val file = ProjectFile.valueOf(cucumberExpMatcher.getString(0))
                verifySuspend {
                    projectActions.openCurrentProjectFile(file, any(), any())
                }
            }

            cucumberExpMatcher.matches("open folder in pinned project, pin: {int}") -> {
                val pin = cucumberExpMatcher.getInt(0)
                verifySuspend {
                    projectActions.openPinnedProjectFolder(pin, any(), any())
                }
            }

            cucumberExpMatcher.matches("open file in pinned project, pin: {int}, file: {word}") -> {
                val pin = cucumberExpMatcher.getInt(0)
                val file = ProjectFile.valueOf(cucumberExpMatcher.getString(1))
                verifySuspend {
                    projectActions.openPinnedProjectFile(pin, file, any(), any())
                }
            }

            cucumberExpMatcher.matches("open folder in regular project, id: {int}") -> {
                val id = cucumberExpMatcher.getInt(0)
                verifySuspend {
                    projectActions.openRegularProjectFolder(id.toLong(), any(), any())
                }
            }

            cucumberExpMatcher.matches("open file in regular project, id: {int}, file: {word}") -> {
                val id = cucumberExpMatcher.getInt(0)
                val file = ProjectFile.valueOf(cucumberExpMatcher.getString(1))
                verifySuspend {
                    projectActions.openRegularProjectFile(id.toLong(), file, any(), any())
                }
            }

            else -> error("Unknown command: $command")
        }
    }

    private fun ComposeUiTest.findProjectRowMatcher(project: String): SemanticsMatcher {
        val m = CucumberExpressionMatcher(project)
        val (section, position) = when {
            m.matches("section {string}, position {int}") -> {
                m.getString(0) to m.getInt(1)
            }

            m.matches("section {string}") -> {
                m.getString(0) to 1
            }

            else -> error("Unknown project selector: $project")
        }

        val sectionMatcher = hasTestTag(section)
        val rowInSectionMatcher = hasProjectRowTag() and hasAnyAncestor(sectionMatcher)

        onNode(sectionMatcher).assertExists()

        val rows = onAllNodes(rowInSectionMatcher, useUnmergedTree = true)
            .fetchSemanticsNodes(atLeastOneRootRequired = false)

        val rowTag = rows[position - 1].config[SemanticsProperties.TestTag]
        return hasTestTag(rowTag)
    }
}
