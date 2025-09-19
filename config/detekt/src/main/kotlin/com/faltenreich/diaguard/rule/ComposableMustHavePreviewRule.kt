package com.faltenreich.diaguard.rule

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.hasAnnotation
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction

internal class ComposableMustHavePreviewRule(config: Config = Config.empty) : Rule(config) {

    private var hasComposable: Boolean = false
    private var hasPreview: Boolean = false

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Maintainability,
        description = DESCRIPTION,
        debt = Debt.FIVE_MINS,
    )

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)

        if (hasComposable && !hasPreview) {
            report(
                CodeSmell(
                    issue = issue,
                    entity = Entity.from(file),
                    message = DESCRIPTION,
                )
            )
        }

        hasComposable = false
        hasPreview = false
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        // Ignore inner Composable, e.g. Screen
        if (!function.isTopLevel) return

        // Ignore Composable that start with lowercase, e.g. remember functions
        if (function.name?.getOrNull(0)?.isUpperCase() != true) return

        if (function.hasAnnotation("Composable")) {
            hasComposable = true
        }

        if (function.hasAnnotation("Preview")) {
            hasPreview = true
        }
    }

    companion object {

        private const val DESCRIPTION = "File with Composable must contain at least one Preview"
    }
}