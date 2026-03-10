package gtr.mpfocus

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import gtr.mpfocus.domain.model.core.ProjectFile
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
            selectedFile = ProjectFile.File1,
            canSetAsCurrent = false,
        ),
        pinnedProjects = listOf(
            ProjectRowState(
                projectId = 2,
                pathText = "c:\\projects\\product-b\\bugfix-123",
                selectedFile = ProjectFile.File2,
                canMovePinnedUp = false,
                canMovePinnedDown = true,
            ),
            ProjectRowState(
                projectId = 3,
                pathText = "c:\\projects\\personal\\writing",
                selectedFile = ProjectFile.File1,
                canMovePinnedUp = true,
                canMovePinnedDown = false,
            ),
        ),
        otherProjects = listOf(
            ProjectRowState(
                projectId = 4,
                pathText = "c:\\projects\\ops\\maintenance",
                selectedFile = ProjectFile.File3,
            ),
            ProjectRowState(
                projectId = 5,
                pathText = "c:\\projects\\home\\garden-plans",
                selectedFile = ProjectFile.File1,
            ),
        ),
    )
}
