import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiComment

internal class ComposableMustContainPreviewRule(config: Config) : Rule(config) {

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Maintainability,
        description = "Composable must contain at least one Preview",
        debt = Debt.FIVE_MINS,
    )

    override fun visitComment(comment: PsiComment) {
        super.visitComment(comment)
        TODO()
    }
}