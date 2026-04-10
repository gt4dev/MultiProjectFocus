package gtr.mpfocus.domain.model.config.project_config

import com.akuleshov7.ktoml.Toml
import gtr.mpfocus.domain.model.config.GlobalProjectConfig
import gtr.mpfocus.domain.model.core.ProjectFile
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

@Serializable
data class GlobalTomlFile(
    val file0: FileSection? = null,
    val file1: FileSection? = null,
    val file2: FileSection? = null,
    val file3: FileSection? = null,
    val file4: FileSection? = null,
    val file5: FileSection? = null,
    val file6: FileSection? = null,
    val file7: FileSection? = null,
    val file8: FileSection? = null,
    val file9: FileSection? = null,
) {
    @Serializable
    data class FileSection(
        val name: String? = null,
        val desc: String? = null,
    )
}

class GlobalTomlParser {

    fun parseConfig(content: String): GlobalProjectConfig? {
        val tomlFile = try {
            Toml.decodeFromString<GlobalTomlFile>(content)
        } catch (e: Exception) {
            // todo: report the error to a user
            return null
        }
        return mapToConfig(tomlFile)
    }

    private fun mapToConfig(tomlFile: GlobalTomlFile): GlobalProjectConfig {
        val fileNames = buildMap {
            tomlFile.file0?.name?.let { put(ProjectFile.File0, it) }
            tomlFile.file1?.name?.let { put(ProjectFile.File1, it) }
            tomlFile.file2?.name?.let { put(ProjectFile.File2, it) }
            tomlFile.file3?.name?.let { put(ProjectFile.File3, it) }
            tomlFile.file4?.name?.let { put(ProjectFile.File4, it) }
            tomlFile.file5?.name?.let { put(ProjectFile.File5, it) }
            tomlFile.file6?.name?.let { put(ProjectFile.File6, it) }
            tomlFile.file7?.name?.let { put(ProjectFile.File7, it) }
            tomlFile.file8?.name?.let { put(ProjectFile.File8, it) }
            tomlFile.file9?.name?.let { put(ProjectFile.File9, it) }
        }
        return GlobalProjectConfig(fileNames = fileNames)
    }
}
