package gtr.mpfocus.domain.model.config

object Models {
    data class ProjectConfig(
        val requestedProjectPath: String,
        val returnedProjFiles: List<String>,
    )
}