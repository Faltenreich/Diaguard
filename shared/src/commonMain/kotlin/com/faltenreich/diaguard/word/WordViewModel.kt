package com.faltenreich.diaguard.word

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.clipboard.Clipboard
import com.faltenreich.diaguard.shared.di.inject

class WordViewModel(
    private val word: Word,
    private val clipboard: Clipboard = inject(),
): ViewModel() {

    fun onClick() {
        clipboard.copyToClipboard(word.name)
    }
}