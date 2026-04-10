package gtr.mpfocus

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.domain.model.read.FileName
import gtr.mpfocus.ui.composables.MessagePanelState
import gtr.mpfocus.ui.composables.MessagePanelState.Tone
import gtr.mpfocus.ui.composables.ProjectRow
import gtr.mpfocus.ui.main_screen.MainScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainScreen(
            uiState = previewMainScreenState(),
            onAction = {},
        )
    }
}

private fun previewMainScreenState(): MainScreen.State {
    return MainScreen.State(
        message = MessagePanelState(
            text = "Sample message panel. Use it for status, warnings, or next actions.",
            tone = Tone.Info,
        ),
        currentProject = previewRowState(
            projectId = 1,
            pathText = "c:\\projects\\product-a\\feature-xyz",
            canSetAsCurrent = false,
        ),
        pinnedProjects = listOf(
            previewRowState(
                projectId = 2,
                pathText = "c:\\projects\\product-b\\bugfix-123",
                canMovePinnedUp = false,
                canMovePinnedDown = true,
            ),
            previewRowState(
                projectId = 3,
                pathText = "c:\\projects\\personal\\writing",
                canMovePinnedUp = true,
                canMovePinnedDown = false,
            ),
        ),
        otherProjects = listOf(
            previewRowState(
                projectId = 4,
                pathText = "c:\\projects\\ops\\maintenance",
            ),
            previewRowState(
                projectId = 5,
                pathText = "c:\\projects\\home\\garden-plans",
            ),
        ),
    )
}

private fun previewRowState(
    projectId: Long,
    pathText: String,
    canSetAsCurrent: Boolean = true,
    canMovePinnedUp: Boolean = false,
    canMovePinnedDown: Boolean = false,
    pinPosition: Int? = null,
) = ProjectRow.State(
    projectId = projectId,
    pathText = pathText,
    selectedFile = ProjectFile.File1,
    availableFiles = ProjectFile.entries.map { FileName(it, "${it.ordinal} ...") },
    canSetAsCurrent = canSetAsCurrent,
    canMovePinnedUp = canMovePinnedUp,
    canMovePinnedDown = canMovePinnedDown,
    pinPosition = pinPosition,
)
