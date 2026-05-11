package gtr.mpfocus.domain.model.config.project_config

import com.akuleshov7.ktoml.Toml
import gtr.mpfocus.domain.model.config.LocalProjectConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

@Serializable
data class LocalTomlFile(
    val projectName: String? = null,
    val projectDescription: String? = null,
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
)

class LocalTomlParser {

    fun parseConfig(content: String): LocalProjectConfig? {
        val tomlFile = try {
            Toml.decodeFromString<LocalTomlFile>(content)
        } catch (e: Exception) {
            // todo: report the error to a user
            return null
        }
        return mapToConfig(tomlFile)
    }

    private fun mapToConfig(tomlFile: LocalTomlFile): LocalProjectConfig {
        val fileNames = mapFileNames(
            tomlFile.file0,
            tomlFile.file1,
            tomlFile.file2,
            tomlFile.file3,
            tomlFile.file4,
            tomlFile.file5,
            tomlFile.file6,
            tomlFile.file7,
            tomlFile.file8,
            tomlFile.file9,
        )
        return LocalProjectConfig(
            projectName = tomlFile.projectName,
            projectDescription = tomlFile.projectDescription,
            fileNames = fileNames,
        )
    }
}
