package com.faltenreich.diaguard

import ComposableMustContainPreviewRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

internal class DiaguardDetektRuleSetProvider : RuleSetProvider {

    override val ruleSetId: String = "diaguard"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            id = ruleSetId,
            rules = listOf(
                ComposableMustContainPreviewRule(config),
            ),
        )
    }
}