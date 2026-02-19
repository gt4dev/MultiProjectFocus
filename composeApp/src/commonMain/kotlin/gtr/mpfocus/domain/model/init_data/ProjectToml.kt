package gtr.mpfocus.domain.model.init_data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectToml(
    val currentProject: String,
    @SerialName("pinned-projects")
    val pinnedProjects: Map<String, String> = emptyMap(),
    val projects: Map<String, String> = emptyMap(),
)