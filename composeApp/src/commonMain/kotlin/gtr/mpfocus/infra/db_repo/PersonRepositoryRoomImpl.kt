package gtr.mpfocus.infra.db_repo

import gtr.mpfocus.domain.repository.Person
import gtr.mpfocus.domain.repository.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.Clock

internal class PersonRepositoryRoomImpl(
    private val personDao: PersonDao
) : PersonRepository {
    override suspend fun addPerson(person: Person) =
        withContext(Dispatchers.IO) {
            personDao.insert(person.toEntity())
            Unit
        }

    override suspend fun addTestPerson() =
        withContext(Dispatchers.IO) {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            val placeholderPerson = Person(
                id = 0,
                name = "Test Person $timestamp",
                age = (timestamp % 50).toInt() + 18
            )
            personDao.insert(placeholderPerson.toEntity())
            Unit
        }

    override fun getAllPeople(): Flow<List<Person>> =
        personDao.getAll().map { entities -> entities.map { it.toDomain() } }
}