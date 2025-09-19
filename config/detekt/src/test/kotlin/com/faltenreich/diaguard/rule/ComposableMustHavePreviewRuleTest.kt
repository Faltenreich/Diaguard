package com.faltenreich.diaguard.rule

import io.gitlab.arturbosch.detekt.test.compileAndLint
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposableMustHavePreviewRuleTest {

    @Test
    fun `should succeed for file with Composable and Preview`() {
        val code = """
            @Composable
            fun SomeScreen() {}

            @Preview
            @Composable
            private fun Preview() {}
        """.trimIndent()

        val findings = ComposableMustHavePreviewRule().compileAndLint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `should fail for file with Composable but without Preview`() {
        val code = """
            @Composable
            fun SomeScreen() {}
        """.trimIndent()

        val findings = ComposableMustHavePreviewRule().compileAndLint(code)
        assertEquals(1, findings.size)
    }
}