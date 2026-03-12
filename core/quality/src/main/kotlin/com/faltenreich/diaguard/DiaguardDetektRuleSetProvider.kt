package com.faltenreich.diaguard

import com.faltenreich.diaguard.rule.ComposableMustHavePreviewRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

internal class DiaguardDetektRuleSetProvider : RuleSetProvider {

    override val ruleSetId: String = "diaguard"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            id = ruleSetId,
            rules = listOf(
                ComposableMustHavePreviewRule(config),
            ),
        )
    }
}