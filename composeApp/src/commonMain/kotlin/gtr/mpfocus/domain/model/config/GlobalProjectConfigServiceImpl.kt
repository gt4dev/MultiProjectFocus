package gtr.mpfocus.domain.model.config

import gtr.mpfocus.domain.model.config.project_config.GlobalTomlFileActions
import gtr.mpfocus.domain.model.config.project_config.GlobalTomlParser
import gtr.mpfocus.system_actions.OperatingSystemActions

class GlobalProjectConfigServiceImpl(
    private val globalTomlFileActions: GlobalTomlFileActions,
    private val globalTomlParser: GlobalTomlParser,
    private val operatingSystemActions: OperatingSystemActions,
) : GlobalProjectConfigService {

    private var cache: GlobalProjectConfig? = null

    override suspend fun readConfig(): GlobalProjectConfig? {
        cache?.let { return it }

        val content = try {
            if (!globalTomlFileActions.fileExists()) {
                globalTomlFileActions.createFile()
            }
            globalTomlFileActions.readFileContent()
        } catch (e: Exception) {
            // todo: report the error to a user
            return null
        }

        val config = globalTomlParser.parseConfig(content)

        cache = config
        return config
    }

    override suspend fun openConfigFile() {
        if (!globalTomlFileActions.fileExists()) {
            globalTomlFileActions.createFile()
        }
        operatingSystemActions.openFile(globalTomlFileActions.filePath)
    }
}
