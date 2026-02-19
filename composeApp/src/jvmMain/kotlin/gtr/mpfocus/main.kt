package gtr.mpfocus

import com.akuleshov7.ktoml.Toml
import gtr.mpfocus.domain.domainModule
import gtr.mpfocus.domain.model.init_data.InitDataLoader
import gtr.mpfocus.domain.model.init_data.ProjectToml
import gtr.mpfocus.domain.repository.ProjectRepository
import gtr.mpfocus.infra.infraModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit = runBlocking {
    val koinApp = startKoin {
        modules(
            infraModule(),
            domainModule(),
        )
    }

    val koin = koinApp.koin
    val initDataLoader: InitDataLoader = koin.get()
    val projectRepository: ProjectRepository = koin.get()

    val sampleToml = """
        currentProject = "alias2"
        
        [pinned-projects]
        pin0 = "alias3"
        pin1 = "alias2"
        pin2 = "alias1"
        
        [projects]
        alias1 = "sample path to the project 1"
        alias2 = "sample path to the project 2"
        alias3 = "sample path to the project 3"
    """.trimIndent()

    val projectToml = Toml.decodeFromString<ProjectToml>(sampleToml)
    initDataLoader.loadData(projectToml)

    val currentProject = projectRepository.getCurrentProject().first()
    val pinnedProjects = projectRepository.getPinnedProjects().first()

    println("current project: $currentProject")
    println("pinned projects: $pinnedProjects")
}


//fun main(args: Array<String>): Unit = runBlocking {
//    val koinApp = startKoin {
//        modules(
//            infraModule(),
//            domainModule(),
//        )
//    }
//
//    val koin = koinApp.koin
//    val coreActions: CoreActions = koin.get()
//
//    require(args.isNotEmpty()) { "Must provide command to execute" }
//    val command = CommandParser.parse(args.first())
//
//    val commandHandler = CommandHandler(coreActions)
//    when (val result = commandHandler.handle(command)) {
//        ActionResult.Success -> println("Command executed successfully")
//        is ActionResult.Error -> println("Command failed: ${result.msg}")
//    }
//}

//fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "mpfocus",
//    ) {
//        App()
//    }
//}
