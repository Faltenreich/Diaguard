package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.shared.validation.ValidationResult
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UniqueTagRuleTest : TestSuite {

    private val rule: UniqueTagRule by inject()
    private val tagRepository: TagRepository by inject()

    @Test
    fun `succeed if no tag with equal name can be found`() {
        val name = "name"
        val tag = Tag.User(name = name)

        val result = rule.check(tag)

        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `succeed if tag with equal name can be found for given local tag`() {
        val name = "name"
        val tagId = tagRepository.create(Tag.User(name = name))
        val tag = tagRepository.getById(tagId)
        assertNotNull(tag)

        val result = rule.check(tag)

        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun `fail if tag with equal name can be found for given user tag`() {
        val name = "name"
        val tagId = tagRepository.create(Tag.User(name = name))
        tagRepository.getById(tagId)

        val tag = Tag.User(name = name)

        val result = rule.check(tag)

        assertFalse(result is ValidationResult.Success)
    }
}