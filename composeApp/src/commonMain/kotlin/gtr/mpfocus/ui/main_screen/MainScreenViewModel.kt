package gtr.mpfocus.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gtr.mpfocus.domain.model.core.ActionResult
import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.model.core.ProjectActions
import gtr.mpfocus.domain.model.core.ProjectFile
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.ui.composables.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val projectRepository: ProjectRepository,
    private val projectActions: ProjectActions,
    initialMessage: MessagePanelState? = null,
    // todo-soon: replace with 'VM defaults specialized for VM'
    private val projectActionPreferences: ProjectActions.Preferences = ProjectActions.Preferences.UI,
    private val projectActionCallerNotification: ProjectActions.CallerNotification = ProjectActions.CallerNotification.CancelAll,
) : ViewModel() {

    private val messageState = MutableStateFlow(initialMessage)
    private val isPinnedProjectsReorderMode = MutableStateFlow(false)

    private val currentProject = projectRepository.getCurrentProject()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = null,
        )

    private val pinnedProjects = projectRepository.getPinnedProjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptyList(),
        )

    private val otherProjects = projectRepository.getOtherProjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptyList(),
        )

    val uiState: StateFlow<MainScreenState> = combine(
        currentProject,
        pinnedProjects,
        otherProjects,
        messageState,
        isPinnedProjectsReorderMode,
    ) { current: Project?, pinned: List<Project>, other: List<Project>, message: MessagePanelState?, reorderMode: Boolean ->
        MainScreenState(
            message = message,
            currentProject = current
                ?.toRowState()
                ?.copy(
                    canSetAsCurrent = false,
                ),
            pinnedProjects = pinned.mapIndexed { index, project ->
                project
                    .toRowState()
                    .copy(
                        canSetAsCurrent = current?.projectId != project.projectId,
                        canMovePinnedUp = index > 0,
                        canMovePinnedDown = index < pinned.lastIndex,
                    )
            },
            otherProjects = other.map { project ->
                project
                    .toRowState()
                    .copy(
                        canSetAsCurrent = current?.projectId != project.projectId,
                    )
            },
            isPinnedProjectsReorderMode = reorderMode,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = MainScreenState(message = initialMessage),
    )

    fun onAction(action: MainScreenUiActions) {
        when (action) {
            is MainScreenUiActions.ScreenHeader -> onScreenHeaderAction(action.action)
            is MainScreenUiActions.MessagePanel -> onMessagePanelAction(action.action)
            is MainScreenUiActions.CurrentProjectSection -> onCurrentProjectSectionAction(action.action)
            is MainScreenUiActions.PinnedProjectsSection -> onPinnedProjectsSectionAction(action.action)
            is MainScreenUiActions.OtherProjectsSection -> onOtherProjectsSectionAction(action.action)
        }
    }

    private fun onScreenHeaderAction(action: ScreenHeaderUiActions) {
        when (action) {
            ScreenHeaderUiActions.AddProjectClicked -> onAddProject()
        }
    }

    private fun onMessagePanelAction(action: MessagePanelUiActions) {
        when (action) {
            MessagePanelUiActions.DismissClicked -> onDismissMessage()
        }
    }

    private fun onCurrentProjectSectionAction(action: CurrentProjectSectionUiActions) {
        when (action) {
            is CurrentProjectSectionUiActions.ProjectRowActions -> {
                when (val projectRowAction = action.action) {
                    is ProjectRowUiActions.OpenFolderClicked -> onOpenCurrentProjectFolder()
                    else -> onProjectRowAction(projectRowAction)
                }
            }

            is CurrentProjectSectionUiActions.UnsetCurrentProjectClicked -> onUnsetCurrentProject()
        }
    }

    private fun onPinnedProjectsSectionAction(action: PinnedProjectsSectionUiActions) {
        when (action) {
            is PinnedProjectsSectionUiActions.ProjectRowActions -> {
                when (val projectRowAction = action.action) {
                    is ProjectRowUiActions.OpenFolderClicked -> onOpenPinnedProjectFolder(action.pinPosition)

                    else -> onProjectRowAction(projectRowAction)
                }
            }

            PinnedProjectsSectionUiActions.ToggleReorderModeClicked -> onTogglePinnedProjectsReorderMode()
        }
    }

    private fun onOtherProjectsSectionAction(action: OtherProjectsSectionUiActions) {
        when (action) {
            is OtherProjectsSectionUiActions.ProjectRowActions -> {
                when (val projectRowAction = action.action) {
                    is ProjectRowUiActions.OpenFolderClicked -> onOpenRegularProjectFolder(projectRowAction.projectId)
                    else -> onProjectRowAction(projectRowAction)
                }
            }
        }
    }

    private fun onProjectRowAction(action: ProjectRowUiActions) {
        when (action) {
            is ProjectRowUiActions.AddSubProjectClicked -> onAddSubProject(action.projectId)
            is ProjectRowUiActions.DeleteClicked -> onDeleteProject(action.projectId)
            is ProjectRowUiActions.FileSelected -> onSelectProjectFile(action.projectId, action.file)
            is ProjectRowUiActions.MovePinnedDownClicked -> onMovePinnedProjectDown(action.projectId)
            is ProjectRowUiActions.MovePinnedUpClicked -> onMovePinnedProjectUp(action.projectId)
            is ProjectRowUiActions.OpenFileClicked -> onOpenProjectFile(action.projectId, action.file)
            is ProjectRowUiActions.SetCurrentClicked -> onSetCurrentProject(action.projectId)
            is ProjectRowUiActions.TogglePinnedClicked -> onTogglePinnedProject(action.projectId)
            is ProjectRowUiActions.OpenFolderClicked -> Unit
        }
    }

    // todo: handle locally in compo
    private fun onDismissMessage() {
        messageState.value = null
    }

    private fun onAddProject() {
        showInfo(
            """
            Add project is not implemented yet.
            Check README.md to learn how to add projects from a TOML file.
            """.trimIndent()
        )
    }

    private fun onUnsetCurrentProject() {
        viewModelScope.launch {
            projectRepository.setCurrentProject(null)
        }
    }

    private fun onTogglePinnedProjectsReorderMode() {
        isPinnedProjectsReorderMode.update { !it }
    }

    private fun onSetCurrentProject(projectId: Long) {
        viewModelScope.launch {
            projectRepository.setCurrentProject(projectId)
        }
    }

    private fun onOpenCurrentProjectFolder() {
        executeProjectAction {
            projectActions.openCurrentProjectFolder(
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification,
            )
        }
    }

    private fun onOpenPinnedProjectFolder(pinPosition: Int) {
        executeProjectAction {
            projectActions.openPinnedProjectFolder(
                pinPosition = pinPosition,
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification,
            )
        }
    }

    private fun onOpenRegularProjectFolder(projectId: Long) {
        executeProjectAction {
            projectActions.openRegularProjectFolder(
                projectId = projectId,
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification,
            )
        }
    }

    private fun onOpenProjectFile(projectId: Long, file: ProjectFile) {
        showInfo("Not implemented yet.\nonOpenProjectFile(projectId: $projectId, file: $file)")
    }

    private fun executeProjectAction(action: suspend () -> ActionResult) {
        viewModelScope.launch {
            when (val result = action()) {
                ActionResult.Success -> Unit
                is ActionResult.Error -> showInfo(result.msg)
            }
        }
    }

    private fun onTogglePinnedProject(projectId: Long) {
        // todo: move to 'model' PinnedService
        viewModelScope.launch {
            val currentPinned = pinnedProjects.value
            if (currentPinned.any { it.projectId == projectId }) {
                repinProjects(currentPinned.filterNot { it.projectId == projectId })
            } else {
                projectRepository.pinProject(projectId, currentPinned.size + 1)
            }
        }
    }

    // todo: handle in compo locally
    private fun onSelectProjectFile(projectId: Long, file: ProjectFile) {
    }

    private fun onDeleteProject(projectId: Long) {
        showInfo("Not implemented yet.")
    }

    private fun onAddSubProject(projectId: Long) {
        showInfo("Not implemented yet.")
    }

    private fun onMovePinnedProjectUp(projectId: Long) {
        movePinnedProject(projectId = projectId, offset = -1)
    }

    private fun onMovePinnedProjectDown(projectId: Long) {
        movePinnedProject(projectId = projectId, offset = 1)
    }

    private fun movePinnedProject(
        projectId: Long,
        offset: Int,
    ) {
        // todo: move to PinnedService
        viewModelScope.launch {
            val currentPinned = pinnedProjects.value.toMutableList()
            val currentIndex = currentPinned.indexOfFirst { it.projectId == projectId }
            if (currentIndex == -1) {
                return@launch
            }

            val targetIndex = currentIndex + offset
            if (targetIndex !in currentPinned.indices) {
                return@launch
            }

            val movedProject = currentPinned.removeAt(currentIndex)
            currentPinned.add(targetIndex, movedProject)
            repinProjects(currentPinned)
        }
    }

    private suspend fun repinProjects(projects: List<Project>) {
        projectRepository.clearAllPins()
        projects.forEachIndexed { index, project ->
            projectRepository.pinProject(
                projectId = project.projectId,
                pinPosition = index + 1,
            )
        }
    }

    private fun showInfo(text: String) {
        messageState.value = MessagePanelState(text = text)
    }

    private fun Project.toRowState(): ProjectRowState {
        return ProjectRowState(
            projectId = projectId,
            pathText = folderPath.path.toString(),
            pinPosition = pinPosition,
        )
    }

    companion object {
        private const val STOP_TIMEOUT_MS = 5_000L
    }
}

class MainScreenViewModelFactory(
    private val projectRepository: ProjectRepository,
    private val projectActions: ProjectActions,
    private val projectActionPreferences: ProjectActions.Preferences = ProjectActions.Preferences.UI,
    private val projectActionCallerNotification: ProjectActions.CallerNotification = ProjectActions.CallerNotification.CancelAll,
) {
    fun create(initialMessage: MessagePanelState? = null): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                MainScreenViewModel(
                    projectRepository = projectRepository,
                    projectActions = projectActions,
                    initialMessage = initialMessage,
                    projectActionPreferences = projectActionPreferences,
                    projectActionCallerNotification = projectActionCallerNotification,
                )
            }
        }
    }
}
