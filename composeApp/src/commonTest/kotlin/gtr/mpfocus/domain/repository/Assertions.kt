package gtr.mpfocus.domain.repository

import gtr.mpfocus.domain.model.core.Project
import gtr.mpfocus.domain.model.core.Models.Project as MProject
object Assertions {
    fun isTheSameProject(actual: Project?, expected: MProject?): Boolean {
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
