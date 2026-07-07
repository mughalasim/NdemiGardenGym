package com.ndemi.garden.gym

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class KonsistTest {
    @Test
    fun `view models should have ViewModel suffix`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("ViewModel")
            .assertTrue { it.name.endsWith("ViewModel") }
    }

    @Test
    fun `view models should reside in ui package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("ViewModel")
            .assertTrue { it.resideInPackage("..ui..") }
    }

    @Test
    fun `use cases should reside in usecase package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { it.resideInPackage("..usecase..") }
    }

    @Test
    fun `repositories should reside in repository or repositories package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Repository")
            .assertTrue { it.resideInPackage("..repository..") || it.resideInPackage("..repositories..") }
    }

    @Test
    fun `interfaces should not have I prefix`() {
        Konsist
            .scopeFromProject()
            .interfaces()
            .assertFalse { it.name.startsWith("I") && it.name.getOrNull(1)?.isUpperCase() == true }
    }

    @Test
    fun `domain layer should not depend on data layer`() {
        Konsist
            .scopeFromPackage("cv.domain..")
            .files
            .assertFalse { it.hasImport { import -> import.name.contains("cv.data") } }
    }

    @Test
    fun `domain layer should not depend on app layer`() {
        Konsist
            .scopeFromPackage("cv.domain..")
            .files
            .assertFalse { it.hasImport { import -> import.name.contains("com.ndemi.garden.gym") } }
    }

    @Test
    fun `data layer should not depend on app layer`() {
        Konsist
            .scopeFromPackage("cv.data..")
            .files
            .assertFalse { it.hasImport { import -> import.name.contains("com.ndemi.garden.gym") } }
    }

    @Test
    fun `no class should use Android Log class except AppLoggerRepositoryImp`() {
        Konsist
            .scopeFromProject()
            .files
            .assertFalse {
                it.hasImport { import -> import.name == "android.util.Log" } &&
                    !it.name.contains("AppLoggerRepositoryImp") &&
                    !it.name.contains("KonsistTest")
            }
    }

    @Test
    fun `no class should handle date time creation apart from DateProviderRepositoryImp`() {
        val nowCreationRegex =
            Regex(
                """\b(DateTime\.now\(\)|Date\(\)|Calendar\.getInstance\(\)|LocalDateTime\.now\(\)|LocalDate\.now\(\))""",
            )
        Konsist
            .scopeFromProject()
            .files
            .filterNot {
                it.name.contains("DateProviderRepositoryImp") ||
                    it.name.contains("Test") ||
                    it.name.contains("KonsistTest")
            }.assertFalse {
                nowCreationRegex.containsMatchIn(it.text)
            }
    }
}
