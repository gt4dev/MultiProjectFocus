package gtr.mpfocus.domain.repository

import kotlinx.coroutines.flow.Flow

// todo: delete poc
data class Person(
    val id: Long,
    val name: String,
    val age: Int
)

interface PersonRepository {
    suspend fun addPerson(person: Person)
    suspend fun addTestPerson()
    fun getAllPeople(): Flow<List<Person>>
}
