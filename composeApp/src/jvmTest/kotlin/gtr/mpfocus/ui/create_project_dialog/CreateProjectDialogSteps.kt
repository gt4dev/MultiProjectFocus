package gtr.mpfocus.ui.create_project_dialog

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import dev.hotest.HOTestCtx
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import gtr.mpfocus.domain.model.core.CoreResult
import gtr.mpfocus.domain.model.core.CreateProjectService
import gtr.mpfocus.system_actions.FolderPath
import gtr.mpfocus.ui.composables.CreateFolderPanel
import kotlinx.coroutines.flow.update
import okio.Path.Companion.toPath
import org.koin.core.qualifier.named

@OptIn(ExperimentalTestApi::class)
object CreateProjectDialogSteps {

    const val VIEW_MODEL_FACTORY_KEY = "test vm factory"

    fun HOTestCtx.`given 'create project dialog' sets 'create folder panel' as`(
        visibility: String,
    ) {
        val viewModel = initCreateProjectDialogVm()
        val folder = FolderPath("c:/projects/sample-project".toPath())

        when (visibility) {
            "visible" -> viewModel._uiState.update { currentState ->
                currentState.copy(
                    createFolderPanel = CreateFolderPanel.State(folderPath = folder),
                )
            }

            "invisible" -> viewModel._uiState.update { currentState ->
                currentState.copy(createFolderPanel = null)
            }

            else -> error("Unknown visibility: $visibility")
        }
    }

    fun HOTestCtx.`given 'create project dialog' shows 'create folder panel' with no folder info`(
        path: String,
    ) {
        val folder = FolderPath(path.toPath())
        val viewModel = initCreateProjectDialogVm()

        viewModel._uiState.update { currentState ->
            currentState.copy(
                createFolderPanel = CreateFolderPanel.State(folderPath = folder),
            )
        }
    }

    fun HOTestCtx.`when 'create project dialog' is shown`() {
        initCreateProjectDialogVm()

        val cut: ComposeUiTest = koin.get()
        cut.setContent {
            MaterialTheme {
                CreateProjectDialogContainer(
                    viewModelFactory = koin.get(
                        named(VIEW_MODEL_FACTORY_KEY)
                    ),
                    onCloseRequest = {},
                    onCompleted = {},
                )
            }
        }

        cut.waitForIdle()
    }

    fun HOTestCtx.`then 'create folder panel' is`(visibility: String) {
        val cut: ComposeUiTest = koin.get()

        cut.onNodeWithText("Add project").assertExists()

        when (visibility) {
            "visible" -> cut.onNodeWithTag(CreateFolderPanel.TestTags.ROOT).assertExists()
            "invisible" -> cut.onNodeWithTag(CreateFolderPanel.TestTags.ROOT).assertDoesNotExist()
            else -> error("Unknown visibility: $visibility")
        }
    }

    fun HOTestCtx.`given in 'create project dialog' user enters project path`(path: String) {
        val cut: ComposeUiTest = koin.get()
        with(cut) {
            waitForIdle()
            onNodeWithTag(CreateProjectDialog.TestTags.PROJECT_PATH_INPUT)
                .assertExists()
                .performTextReplacement(path)
            waitForIdle()
        }
    }

    fun HOTestCtx.`given in 'create project dialog' user marks 'set as current project' as`(value: String) {
        val shouldBeChecked = when (value.lowercase()) {
            "checked" -> true
            "unchecked" -> false
            else -> error("Unknown set current value: $value")
        }

        val cut: ComposeUiTest = koin.get()
        with(cut) {
            waitForIdle()
            val checkbox = onNodeWithTag(
                CreateProjectDialog.TestTags.SET_AS_CURRENT_CHECKBOX,
                useUnmergedTree = true,
            )
                .assertExists()

            val isCurrentlyChecked =
                checkbox.fetchSemanticsNode().config[SemanticsProperties.ToggleableState] == ToggleableState.On

            if (isCurrentlyChecked != shouldBeChecked) {
                checkbox.performClick()
                waitForIdle()
            }

            val isCheckedAfterUpdate = onNodeWithTag(
                CreateProjectDialog.TestTags.SET_AS_CURRENT_CHECKBOX,
                useUnmergedTree = true,
            )
                .assertExists()
                .fetchSemanticsNode()
                .config[SemanticsProperties.ToggleableState] == ToggleableState.On

            check(isCheckedAfterUpdate == shouldBeChecked) {
                "Expected checkbox state '$value', but actual checked=$isCheckedAfterUpdate"
            }

            waitForIdle()
        }
    }

    fun HOTestCtx.`when in 'create folder panel' is clicked button 'create folder'`() {
        val cut: ComposeUiTest = koin.get()
        with(cut) {
            waitForIdle()
            onNodeWithTag(
                CreateFolderPanel.TestTags.CREATE_BUTTON,
                useUnmergedTree = true,
            ).assertExists().performClick()
            waitForIdle()
        }
    }

    fun HOTestCtx.`when in 'create folder panel' user clicks button 'add'`() {
        val cut: ComposeUiTest = koin.get()
        with(cut) {
            waitForIdle()
            onNodeWithTag(
                CreateProjectDialog.TestTags.ADD_BUTTON,
                useUnmergedTree = true,
            ).assertExists().performClick()
            waitForIdle()
        }
    }

    fun HOTestCtx.`then in 'create project mock' is called create project`(
        path: String,
        setCurrent: String,
    ) {
        val expectedSetAsCurrent = when (setCurrent.lowercase()) {
            "is current project" -> true
            "is not current project" -> false
            else -> error("Unknown set current value: $setCurrent")
        }

        val createProjectService: CreateProjectService = koin.get()
        verifySuspend {
            createProjectService.createProject(path, expectedSetAsCurrent)
        }
    }

    private fun HOTestCtx.initCreateProjectDialogVm(): CreateProjectDialogViewModel {
        val vmExisting = runCatching { koin.get<CreateProjectDialogViewModel>() }.getOrNull()
        if (vmExisting != null) {
            return vmExisting
        }

        val vmFactoryReal =
            CreateProjectDialogViewModelFactoryProvider(
                folderPicker = mock(MockMode.autofill),
                createProjectService = initCreateProjectServiceMock(),
                fileSystemActions = koin.get(),
            )
                .createFactory(relatedProjectId = null)

        val vmReal = vmFactoryReal.create(
            CreateProjectDialogViewModel::class,
            MutableCreationExtras(),
        )

        val vmFactoryTest = dummyViewModelFactory(vmReal)

        koinAdd {
            single(named(VIEW_MODEL_FACTORY_KEY)) {
                vmFactoryTest
            }
            single {
                vmReal
            }
        }

        return vmReal
    }

    private fun HOTestCtx.initCreateProjectServiceMock(): CreateProjectService {
        val existing = runCatching { koin.get<CreateProjectService>() }.getOrNull()
        if (existing != null) {
            return existing
        }

        val createProjectService = mock<CreateProjectService>(MockMode.autofill) {
            everySuspend { getRecommendedPath(any()) } returns null
            everySuspend { createProject(any(), any()) } returns CoreResult.Success
        }

        koinAdd {
            single { createProjectService }
        }

        return createProjectService
    }

    private fun dummyViewModelFactory(
        viewModelToReturn: CreateProjectDialogViewModel,
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(
            modelClass: kotlin.reflect.KClass<T>,
            extras: CreationExtras,
        ): T {
            @Suppress("UNCHECKED_CAST")
            return viewModelToReturn as T
        }
    }
}
