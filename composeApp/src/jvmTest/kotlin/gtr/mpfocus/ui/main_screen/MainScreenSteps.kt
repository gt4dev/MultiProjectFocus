package gtr.mpfocus.ui.main_screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.model.core.Models
import gtr.mpfocus.ui.composables.ProjectRowTestTags

@OptIn(ExperimentalTestApi::class)
object MainScreenSteps {

    fun HOTestCtx.`when 'main screen' is started`() {
        val cut: ComposeUiTest = koin.get()
        cut.setContent {
            MaterialTheme {
                MainScreenContainer(
                    viewModelFactory = MainScreenViewModelFactory(
                        koin.get()
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

    // todo: gdzie to wrzucic? pp na razie zostawic w ui tests
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

}