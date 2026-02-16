package gtr.mpfocus.domain.model.core

import gtr.mpfocus.system_actions.FolderPath

class Project(
    val projectId: Long = 0, // default Room's "new entity"
    val folderPath: FolderPath, // todo: using FolderPath suggests use of separated Room entities
    val pinPosition: Int? = null,
)

enum class ProjectFiles {
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
