package gtr.mpfocus

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import gtr.mpfocus.domain.model.core.ProjectFiles
import gtr.mpfocus.ui.composables.MessagePanelState
import gtr.mpfocus.ui.composables.MessagePanelState.Tone
import gtr.mpfocus.ui.composables.ProjectRowState
import gtr.mpfocus.ui.main_screen.MainScreen
import gtr.mpfocus.ui.main_screen.MainScreenState

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

private fun previewMainScreenState(): MainScreenState {
    return MainScreenState(
        message = MessagePanelState(
            text = "Sample message panel. Use it for status, warnings, or next actions.",
            tone = Tone.Info,
        ),
        currentProject = ProjectRowState(
            projectId = 1,
            pathText = "c:\\projects\\product-a\\feature-xyz",
            selectedFile = ProjectFiles.File1,
            isPinned = false,
            canSetAsCurrent = false,
        ),
        pinnedProjects = listOf(
            ProjectRowState(
                projectId = 2,
                pathText = "c:\\projects\\product-b\\bugfix-123",
                selectedFile = ProjectFiles.File2,
                isPinned = true,
                canMovePinnedUp = false,
                canMovePinnedDown = true,
            ),
            ProjectRowState(
                projectId = 3,
                pathText = "c:\\projects\\personal\\writing",
                selectedFile = ProjectFiles.File1,
                isPinned = true,
                canMovePinnedUp = true,
                canMovePinnedDown = false,
            ),
        ),
        otherProjects = listOf(
            ProjectRowState(
                projectId = 4,
                pathText = "c:\\projects\\ops\\maintenance",
                selectedFile = ProjectFiles.File3,
                isPinned = false,
            ),
            ProjectRowState(
                projectId = 5,
                pathText = "c:\\projects\\home\\garden-plans",
                selectedFile = ProjectFiles.File1,
                isPinned = false,
            ),
        ),
    )
}
