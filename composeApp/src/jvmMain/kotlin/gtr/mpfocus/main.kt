package gtr.mpfocus

import gtr.mpfocus.domain.domainModule
import gtr.mpfocus.domain.model.core.CoreActions
import gtr.mpfocus.domain.repository.Person
import gtr.mpfocus.domain.repository.PersonRepository
import gtr.mpfocus.infra.infraModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin

fun main(): Unit = runBlocking {
    val koinApp = startKoin {
        modules(
            infraModule(),
            domainModule(),
        )
    }

    val koin = koinApp.koin
    val coreActions: CoreActions = koin.get()
    val personRepository: PersonRepository = koin.get()

    println("CoreActions resolved: ${coreActions::class.simpleName}")
    println("PersonRepository resolved: ${personRepository::class.simpleName}")

    personRepository.addPerson(Person(id = 0, name = "Alice", age = 29))
    personRepository.addPerson(Person(id = 0, name = "Bob", age = 34))
    personRepository.addTestPerson()

    val people = personRepository.getAllPeople().first()
    println("People in DB (${people.size}):")
    people.forEach { person ->
        println(" - id=${person.id}, name=${person.name}, age=${person.age}")
    }
}

//fun main(): Unit = runBlocking {
//    val operatingSystemActions = OperatingSystemActionsImpl()
//    val fileSystemActions = FileSystemActionsImpl()
//    val projectsRepo = DevTimeProjectRepositoryImpl(true)
//    val configService = ConfigService.Basic
//
//    val m = CoreActionsImpl(
//        operatingSystemActions,
//        fileSystemActions,
//        projectsRepo,
//        configService,
//    )
//    val result = m.openCurrentProjectFolder(
//        ActionPreferences(),
//        UserNotifier.None
//    )
//    println(result)
//}

//fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "mpfocus",
//    ) {
//        App()
//    }
//}
