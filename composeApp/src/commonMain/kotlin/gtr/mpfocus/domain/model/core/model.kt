package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FolderPath

class Project(
    val projectId: Long = 0, // default Room's "new entity"
    val folderPath: FolderPath,
    val pinPosition: Int? = null,
    // todo: expose isCurrent
)

enum class ProjectFile {
    File0,
    File1,
    File2,
    File3,
    File4,
    File5,
    File6,
    File7,
    File8,
    File9,
}
