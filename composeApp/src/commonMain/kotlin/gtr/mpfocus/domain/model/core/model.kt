package gtr.mpfocus.domain.model.core

import kotlin.jvm.JvmInline

@JvmInline
value class ProjectId(val id: String)

class Project(
    val id: ProjectId
    // ..
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