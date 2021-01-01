package com.faltenreich.diaguard.shared.view.reveal;

public interface Revealable {
    void reveal(Reveal.Callback callback);
    void unreveal(Reveal.Callback callback);
}
