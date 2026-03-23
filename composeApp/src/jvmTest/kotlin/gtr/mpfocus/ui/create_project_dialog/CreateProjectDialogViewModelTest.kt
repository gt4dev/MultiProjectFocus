package gtr.mpfocus.ui.create_project_dialog

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import gtr.mpfocus.domain.model.core.CoreResult
import gtr.mpfocus.domain.model.core.CreateProjectService
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath
import gtr.mpfocus.ui.composables.CreateFolderPanel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


// todo: replace with hotest integration tests
@OptIn(ExperimentalCoroutinesApi::class)
class CreateProjectDialogViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `missing folder shows create folder panel`() = runTest(dispatcher) {
        val folderPath = FolderPath("c:/some/path/to/folder".toPath())
        val createProjectService = mock<CreateProjectService>(MockMode.autofill) {
            everySuspend { createProject(folderPath.path.toString()) } returns CoreResult.Error.FolderDoesNotExist(folderPath)
        }
        val viewModel = CreateProjectDialogViewModel(
            folderPicker = FakeFolderPicker(),
            createProjectService = createProjectService,
            fileSystemActions = mock(MockMode.autofill),
        )

        viewModel.onAction(CreateProjectDialog.Actions.ProjectPathChanged(folderPath.path.toString()))
        viewModel.onAction(CreateProjectDialog.Actions.CreateClicked)
        advanceUntilIdle()

        assertEquals(
            CreateFolderPanel.State(folderPath = folderPath),
            viewModel.uiState.value.createFolderPanel,
        )
        assertNull(viewModel.uiState.value.projectPathError)
        assertTrue(!viewModel.uiState.value.isSubmitting)
    }

    @Test
    fun `create folder action updates panel to success`() = runTest(dispatcher) {
        val folderPath = FolderPath("c:/some/path/to/folder".toPath())
        val createProjectService = mock<CreateProjectService>(MockMode.autofill) {
            everySuspend { createProject(folderPath.path.toString()) } returns CoreResult.Error.FolderDoesNotExist(folderPath)
        }
        val fileSystemActions = mock<FileSystemActions>(MockMode.autofill) {
            every { createFolder(folderPath) } returns true
        }
        val viewModel = CreateProjectDialogViewModel(
            folderPicker = FakeFolderPicker(),
            createProjectService = createProjectService,
            fileSystemActions = fileSystemActions,
        )

        viewModel.onAction(CreateProjectDialog.Actions.ProjectPathChanged(folderPath.path.toString()))
        viewModel.onAction(CreateProjectDialog.Actions.CreateClicked)
        advanceUntilIdle()
        viewModel.onAction(CreateProjectDialog.Actions.CreateFolderPanelAction(CreateFolderPanel.Actions.CreateClicked))
        advanceUntilIdle()

        assertEquals(
            CreateFolderPanel.Status.Success,
            viewModel.uiState.value.createFolderPanel?.status,
        )
        verify { fileSystemActions.createFolder(folderPath) }
    }

    @Test
    fun `create folder action updates panel to error when creation fails`() = runTest(dispatcher) {
        val folderPath = FolderPath("c:/some/path/to/folder".toPath())
        val fileSystemActions = mock<FileSystemActions>(MockMode.autofill) {
            every { createFolder(folderPath) } returns false
        }
        val viewModel = CreateProjectDialogViewModel(
            folderPicker = FakeFolderPicker(),
            createProjectService = mock<CreateProjectService>(MockMode.autofill) {
                everySuspend { createProject(folderPath.path.toString()) } returns CoreResult.Error.FolderDoesNotExist(folderPath)
            },
            fileSystemActions = fileSystemActions,
        )

        viewModel.onAction(CreateProjectDialog.Actions.ProjectPathChanged(folderPath.path.toString()))
        viewModel.onAction(CreateProjectDialog.Actions.CreateClicked)
        advanceUntilIdle()
        viewModel.onAction(CreateProjectDialog.Actions.CreateFolderPanelAction(CreateFolderPanel.Actions.CreateClicked))
        advanceUntilIdle()

        assertEquals(
            CreateFolderPanel.Status.Error("Unable to create folder."),
            viewModel.uiState.value.createFolderPanel?.status,
        )
    }

    private class FakeFolderPicker : FolderPicker {
        override fun pickFolder(initialPath: String?): String? = null
    }
}
