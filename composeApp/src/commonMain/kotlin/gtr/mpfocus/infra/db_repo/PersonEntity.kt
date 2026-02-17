package gtr.mpfocus.infra.db_repo

import androidx.room.Entity
import androidx.room.PrimaryKey
import gtr.mpfocus.domain.repository.Person

@Entity(tableName = "people")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val age: Int
)

internal fun PersonEntity.toDomain(): Person = Person(
    id = id,
    name = name,
    age = age
)

internal fun Person.toEntity(): PersonEntity = PersonEntity(
    id = if (id == 0L) 0 else id,
    name = name,
    age = age
)
