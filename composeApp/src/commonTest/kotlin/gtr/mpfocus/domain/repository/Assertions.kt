package gtr.mpfocus.domain.repository

import gtr.mpfocus.domain.model.core.Project

object Assertions {
    fun isTheSameProject(actual: Project?, expected: Models.Project?): Boolean {
        if (actual == null && expected == null) {
            return true
        }

        if (actual != null && expected != null) {
            return actual.projectId == expected.id &&
                    actual.folderPath.path.toString() == expected.path
        }

        return false
    }
}
