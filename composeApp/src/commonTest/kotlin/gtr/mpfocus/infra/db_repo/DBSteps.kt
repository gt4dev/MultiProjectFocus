package gtr.mpfocus.infra.db_repo

import gtr.hotest.HOTestCtx
import gtr.mpfocus.domain.repository.Models

object DBSteps {
    fun HOTestCtx.`'test database' sets up`() {
        val db = createInMemoryTestDb()
        val dao = db.projectDao()
        koinAdd {
            single { db }
            single { dao }
        }
    }

    fun HOTestCtx.`'test database' tears down`() {
        val db: MPFDatabase = koin.get()
        db.close()
    }

    suspend fun HOTestCtx.`given 'project dao' has data`(
        vararg data: Models.Project,
    ) {
        val dao: ProjectDao = koin.get()
        for (d in data) {
            dao.insert(
                ProjectEntity(
                    id = d.id,
                    folderPath = d.path,
                    isCurrent = false,
                    pinPosition = null
                )
            )
        }
    }

}
