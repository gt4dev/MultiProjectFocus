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
    private val projectActionPreferences: ProjectActions.Preferences = ProjectActions.Preferences.UI,
    // todo: impl. real user reactions
    private val projectActionCallerNotification: ProjectActions.CallerNotification = ProjectActions.CallerNotification.CancelAll,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreen.State(message = initialMessage))
    val uiState: StateFlow<MainScreen.State> = _uiState.asStateFlow()

    private var pendingDeleteProjectId: Long? = null

    private val _effects = MutableSharedFlow<MainScreenEffect>()
    val effects: SharedFlow<MainScreenEffect> = _effects.asSharedFlow()

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

    init {
        bindDomainState()
    }

    fun onAction(action: MainScreen.Actions) {
        when (action) {
            is MainScreen.Actions.ScreenHeader -> onScreenHeaderAction(action.action)
            is MainScreen.Actions.MessagePanel -> onMessagePanelAction(action.action)
            is MainScreen.Actions.CurrentProjectSection -> onCurrentProjectSectionAction(action.action)
            is MainScreen.Actions.PinnedProjectsSection -> onPinnedProjectsSectionAction(action.action)
            is MainScreen.Actions.OtherProjectsSection -> onOtherProjectsSectionAction(action.action)
            // todo: probably to delete
            is MainScreen.Actions.DeleteProjectDialog -> onDeleteProjectDialogAction(action.action)
        }
    }

    private fun onScreenHeaderAction(action: ScreenHeaderUiActions) {
        when (action) {
            ScreenHeaderUiActions.AddProjectClicked -> onOpenCreateProjectDialog(null)
        }
    }

    private fun onMessagePanelAction(action: MessagePanelUiActions) {
        when (action) {
            MessagePanelUiActions.DismissClicked -> onDismissMessage()
        }
    }

    private fun onDeleteProjectDialogAction(action: DeleteProjectDialog.Actions) {
        when (action) {
            DeleteProjectDialog.Actions.CancelClicked -> onDeleteProjectDialogClear()
            DeleteProjectDialog.Actions.ConfirmClicked -> onDeleteProjectDialogConfirm()
        }
    }

    private fun onCurrentProjectSectionAction(action: CurrentProjectSectionUiActions) {
        when (action) {
            is CurrentProjectSectionUiActions.CurrentProjectRowActions -> {
                when (val projectRowAction = action.action) {
                    is ProjectRowActions.OpenFolderClicked -> onOpenCurrentProjectFolder()
                    is ProjectRowActions.OpenFileClicked -> onOpenCurrentProjectFile(projectRowAction.file)
                    else -> onProjectRowAction(projectRowAction)
                }
            }

            is CurrentProjectSectionUiActions.UnsetCurrentProjectClicked -> onUnsetCurrentProject()
        }
    }

    private fun onPinnedProjectsSectionAction(action: PinnedProjectsSectionUiActions) {
        when (action) {
            is PinnedProjectsSectionUiActions.PinnedProjectRowActions -> {
                when (val projectRowAction = action.action) {
                    is ProjectRowActions.OpenFolderClicked -> onOpenPinnedProjectFolder(action.pinPosition)
                    is ProjectRowActions.OpenFileClicked -> onOpenPinnedProjectFile(
                        pinPosition = action.pinPosition,
                        file = projectRowAction.file,
                    )

                    else -> onProjectRowAction(projectRowAction)
                }
            }

            PinnedProjectsSectionUiActions.ToggleReorderModeClicked -> onTogglePinnedProjectsReorderMode()
        }
    }

    private fun onOtherProjectsSectionAction(action: OtherProjectsSectionUiActions) {
        when (action) {
            is OtherProjectsSectionUiActions.OtherProjectRowActions -> {
                when (val projectRowAction = action.action) {
                    is ProjectRowActions.OpenFolderClicked -> onOpenRegularProjectFolder(projectRowAction.projectId)
                    is ProjectRowActions.OpenFileClicked -> onOpenRegularProjectFile(
                        projectId = projectRowAction.projectId,
                        file = projectRowAction.file,
                    )
                    else -> onProjectRowAction(projectRowAction)
                }
            }
        }
    }

    private fun onProjectRowAction(action: ProjectRowActions) {
        when (action) {
            is ProjectRowActions.AddProjectClicked -> onOpenCreateProjectDialog(action.relatedProjectId)
            is ProjectRowActions.DeleteClicked -> onDeleteProject(action.projectId)
            is ProjectRowActions.FileSelected -> onSelectProjectFile(action.projectId, action.file)
            is ProjectRowActions.MovePinnedDownClicked -> onMovePinnedProjectDown(action.projectId)
            is ProjectRowActions.MovePinnedUpClicked -> onMovePinnedProjectUp(action.projectId)
            is ProjectRowActions.SetCurrentClicked -> onSetCurrentProject(action.projectId)
            is ProjectRowActions.TogglePinnedClicked -> onTogglePinnedProject(action.projectId)
            is ProjectRowActions.OpenFileClicked -> Unit
            is ProjectRowActions.OpenFolderClicked -> Unit
        }
    }

    private fun onDismissMessage() {
        _uiState.update { it.copy(message = null) }
    }

    private fun onUnsetCurrentProject() {
        viewModelScope.launch {
            projectRepository.setCurrentProject(null)
        }
    }

    private fun onTogglePinnedProjectsReorderMode() {
        _uiState.update {
            it.copy(isPinnedProjectsReorderMode = !it.isPinnedProjectsReorderMode)
        }
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

    private fun onOpenCurrentProjectFile(file: ProjectFile) {
        executeProjectAction {
            projectActions.openCurrentProjectFile(
                file = file,
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification,
            )
        }
    }

    private fun onOpenPinnedProjectFile(pinPosition: Int, file: ProjectFile) {
        executeProjectAction {
            projectActions.openPinnedProjectFile(
                pinPosition = pinPosition,
                file = file,
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification,
            )
        }
    }

    private fun onOpenRegularProjectFile(projectId: Long, file: ProjectFile) {
        executeProjectAction {
            projectActions.openRegularProjectFile(
                projectId = projectId,
                file = file,
                actionPreferences = projectActionPreferences,
                callerNotification = projectActionCallerNotification,
            )
        }
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
        viewModelScope.launch {
            val project = projectRepository.getProject(projectId) ?: return@launch
            pendingDeleteProjectId = project.projectId
            _uiState.update {
                it.copy(
                    deleteProjectDialog = DeleteProjectDialog.State(
                        path = project.folderPath.path.toString(),
                    ),
                )
            }
        }
    }

    private fun onOpenCreateProjectDialog(relatedProjectId: Long?) {
        viewModelScope.launch {
            _effects.emit(MainScreenEffect.CreateProjectDialogRequested(relatedProjectId))
        }
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

    private fun onDeleteProjectDialogConfirm() {
        val projectId = pendingDeleteProjectId ?: return
        viewModelScope.launch {
            projectRepository.deleteProject(projectId)
            onDeleteProjectDialogClear()
        }
    }

    private fun onDeleteProjectDialogClear() {
        pendingDeleteProjectId = null
        _uiState.update { it.copy(deleteProjectDialog = null) }
    }

    private fun showInfo(text: String) {
        _uiState.update { it.copy(message = MessagePanelState(text = text)) }
    }

    private fun Project.toRowState(): ProjectRowState {
        return ProjectRowState(
            projectId = projectId,
            pathText = folderPath.path.toString(),
            pinPosition = pinPosition,
        )
    }

    private fun bindDomainState() {
        combine(
            currentProject,
            pinnedProjects,
            otherProjects,
        ) { current: Project?, pinned: List<Project>, other: List<Project> ->
            // warning: side effects in 'flow' but the code is simpler
            _uiState.update { state ->
                state.copy(
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
                )
            }
        }.launchIn(viewModelScope)
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
