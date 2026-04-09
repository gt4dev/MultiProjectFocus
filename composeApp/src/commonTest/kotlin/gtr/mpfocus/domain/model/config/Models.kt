package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.core.ProjectFile

object Models {
    
    data class ProjectConfig(
        val requestedProjectPath: String,
        val returnedProjFiles: List<String>,
    )

    data class File(
        val id: ProjectFile,
        val name: String,
    )
}