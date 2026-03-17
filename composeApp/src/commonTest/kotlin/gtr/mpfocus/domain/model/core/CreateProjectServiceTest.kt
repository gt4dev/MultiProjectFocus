package gtr.mpfocus.domain.model.core

import dev.mokkery.*
import dev.mokkery.answering.returns
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.system_actions.FileSystemActions
import gtr.mpfocus.system_actions.FolderPath
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okio.Path.Companion.toPath
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateProjectServiceTest {

    @Test
    fun `blank path returns required error`() = runTest {
        val repository = mock<ProjectRepository>(MockMode.autofill)
        val fileSystemActions = mock<FileSystemActions>(MockMode.autofill)
        val service = CreateProjectServiceImpl(repository, fileSystemActions)

        val result = service.createProject("   ")

        assertEquals(
            CreateProjectService.Result.Error("Project path is required."),
            result,
        )
    }

    @Test
    fun `missing folder returns does not exist error`() = runTest {
        val folder = "path/to/project"
        val folderPath = FolderPath(folder.toPath())
        val repository = mock<ProjectRepository>(MockMode.autofill)
        val fileSystemActions = mock<FileSystemActions>(MockMode.autofill) {
            every { pathExists(folderPath) } returns false
        }
        val service = CreateProjectServiceImpl(repository, fileSystemActions)

        val result = service.createProject(folder)

        assertEquals(
            CreateProjectService.Result.Error("Project folder does not exist."),
            result,
        )
    }

    @Test
    fun `duplicate folder returns already exists error`() = runTest {
        val folder = "path/to/project"
        val folderPath = FolderPath(folder.toPath())
        val existingProject = Project(folderPath = folderPath)
        val repository = mock<ProjectRepository>(MockMode.autofill) {
            every { getAll() } returns flowOf(listOf(existingProject))
        }
        val fileSystemActions = mock<FileSystemActions>(MockMode.autofill) {
            every { pathExists(folderPath) } returns true
        }
        val service = CreateProjectServiceImpl(repository, fileSystemActions)

        val result = service.createProject(folder)

        assertEquals(
            CreateProjectService.Result.Error("Project already exists."),
            result,
        )
    }

    @Test
    fun `valid folder adds project and returns success`() = runTest {
        val folder = "path/to/project"
        val folderPath = FolderPath(folder.toPath())
        val repository = mock<ProjectRepository>(MockMode.autofill) {
            every { getAll() } returns flowOf(emptyList())
            everySuspend { addProject(folderPath) } returns 123L
        }
        val fileSystemActions = mock<FileSystemActions>(MockMode.autofill) {
            every { pathExists(folderPath) } returns true
        }
        val service = CreateProjectServiceImpl(repository, fileSystemActions)

        val result = service.createProject(folder)

        assertEquals(CreateProjectService.Result.Success, result)
        verifySuspend { repository.addProject(folderPath) }
    }
}
