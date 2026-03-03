package gtr.mpfocus.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.model.core.ProjectFiles
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.ui.composables.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val projectRepository: ProjectRepository,
    initialMessage: MessagePanelState? = null,
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
            currentProject = current?.toRowState(
                canSetAsCurrent = false,
                isPinned = current.pinPosition != null,
                canMovePinnedUp = false,
                canMovePinnedDown = false,
            ),
            pinnedProjects = pinned.mapIndexed { index, project ->
                project.toRowState(
                    canSetAsCurrent = current?.projectId != project.projectId,
                    isPinned = true,
                    canMovePinnedUp = index > 0,
                    canMovePinnedDown = index < pinned.lastIndex,
                )
            },
            otherProjects = other.map { project ->
                project.toRowState(
                    canSetAsCurrent = current?.projectId != project.projectId,
                    isPinned = false,
                    canMovePinnedUp = false,
                    canMovePinnedDown = false,
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
            is CurrentProjectSectionUiActions.ProjectRowActions -> onProjectRowAction(action.action)
            is CurrentProjectSectionUiActions.UnsetCurrentProjectClicked -> onUnsetCurrentProject()
        }
    }

    private fun onPinnedProjectsSectionAction(action: PinnedProjectsSectionUiActions) {
        when (action) {
            is PinnedProjectsSectionUiActions.ProjectRowActions -> onProjectRowAction(action.action)
            PinnedProjectsSectionUiActions.ToggleReorderModeClicked -> onTogglePinnedProjectsReorderMode()
        }
    }

    private fun onOtherProjectsSectionAction(action: OtherProjectsSectionUiActions) {
        when (action) {
            is OtherProjectsSectionUiActions.ProjectRowActions -> onProjectRowAction(action.action)
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
            is ProjectRowUiActions.OpenFolderClicked -> onOpenProjectFolder(action.projectId)
            is ProjectRowUiActions.SetCurrentClicked -> onSetCurrentProject(action.projectId)
            is ProjectRowUiActions.TogglePinnedClicked -> onTogglePinnedProject(action.projectId)
        }
    }

    // todo: handle locally in compo
    private fun onDismissMessage() {
        messageState.value = null
    }

    private fun onAddProject() {
        showInfo("Add project is not implemented yet.")
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

    private fun onOpenProjectFolder(projectId: Long) {
        showInfo("Not implemented yet.")
    }

    private fun onOpenProjectFile(projectId: Long, file: ProjectFiles) {
        showInfo("Not implemented yet.")
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
    private fun onSelectProjectFile(projectId: Long, file: ProjectFiles) {
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

    private fun Project.toRowState(
        canSetAsCurrent: Boolean,
        isPinned: Boolean,
        canMovePinnedUp: Boolean,
        canMovePinnedDown: Boolean,
    ): ProjectRowState {
        return ProjectRowState(
            projectId = projectId,
            pathText = folderPath.path.toString(),
            isPinned = isPinned,
            canSetAsCurrent = canSetAsCurrent,
            canMovePinnedUp = canMovePinnedUp,
            canMovePinnedDown = canMovePinnedDown,
        )
    }

    companion object {
        private const val STOP_TIMEOUT_MS = 5_000L
    }
}

class MainScreenViewModelFactory(
    private val projectRepository: ProjectRepository,
) {
    fun create(initialMessage: MessagePanelState? = null): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                MainScreenViewModel(
                    projectRepository = projectRepository,
                    initialMessage = initialMessage,
                )
            }
        }
    }
}
