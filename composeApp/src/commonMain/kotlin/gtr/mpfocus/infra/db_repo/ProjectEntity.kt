package gtr.mpfocus.infra.db_repo

import androidx.room.Entity
import androidx.room.PrimaryKey
import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.system_actions.FolderPath
import okio.Path.Companion.toPath

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val folderPath: String,
    val isCurrent: Boolean,
    val pinPosition: Int?
)

internal fun ProjectEntity.toDomain(): Project = Project(
    projectId = id,
    folderPath = FolderPath(folderPath.toPath()),
    pinPosition = pinPosition
)