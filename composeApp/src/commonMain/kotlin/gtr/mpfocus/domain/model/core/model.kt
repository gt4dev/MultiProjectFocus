package gtr.mpfocus.domain.model.core

import okio.Path

class Project(
    val projectId: Long = 0, // default Room's "new entity"
    val folderPath: Path, // todo: using Path enforces to map it to 'SQLite's String'
)

enum class ProjectKnownFiles {
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