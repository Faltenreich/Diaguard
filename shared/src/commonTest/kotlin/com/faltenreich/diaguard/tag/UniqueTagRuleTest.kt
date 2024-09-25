package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.shared.validation.ValidationResult
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class UniqueTagRuleTest : TestSuite {

    private val rule: UniqueTagRule by inject()

    @Test
    fun `succeeds if no tag with equal name can be found`() {
        val name = "name"
        val tag: Tag = Tag.User(name = name)
        
        val result = rule.check(tag)

        assertEquals(
            expected = ValidationResult.Success(tag),
            actual = result,
        )
    }
}