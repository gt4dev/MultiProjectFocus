package gtr.mpfocus.domain.model.config.project_config

import gtr.mpfocus.domain.model.core.ProjectFile
import kotlinx.serialization.Serializable

@Serializable
data class FileSection(
    val name: String? = null,
    val desc: String? = null,
)

internal fun mapFileNames(
    file0: FileSection?,
    file1: FileSection?,
    file2: FileSection?,
    file3: FileSection?,
    file4: FileSection?,
    file5: FileSection?,
    file6: FileSection?,
    file7: FileSection?,
    file8: FileSection?,
    file9: FileSection?,
): Map<ProjectFile, String> = buildMap {
    file0?.name?.let { put(ProjectFile.File0, it) }
    file1?.name?.let { put(ProjectFile.File1, it) }
    file2?.name?.let { put(ProjectFile.File2, it) }
    file3?.name?.let { put(ProjectFile.File3, it) }
    file4?.name?.let { put(ProjectFile.File4, it) }
    file5?.name?.let { put(ProjectFile.File5, it) }
    file6?.name?.let { put(ProjectFile.File6, it) }
    file7?.name?.let { put(ProjectFile.File7, it) }
    file8?.name?.let { put(ProjectFile.File8, it) }
    file9?.name?.let { put(ProjectFile.File9, it) }
}
