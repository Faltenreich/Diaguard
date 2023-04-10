package com.faltenreich.rhyme.word

import com.faltenreich.rhyme.shared.architecture.ViewModel
import com.faltenreich.rhyme.shared.clipboard.Clipboard
import com.faltenreich.rhyme.shared.di.inject

class WordViewModel(
    private val word: Word,
    private val clipboard: Clipboard = inject(),
): ViewModel() {

    fun onClick() {
        clipboard.copyToClipboard(word.name)
    }
}