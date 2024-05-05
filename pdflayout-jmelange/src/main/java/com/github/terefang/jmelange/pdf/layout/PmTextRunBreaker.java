package com.github.terefang.jmelange.pdf.ml.text;

import java.text.AttributedCharacterIterator;

public class PmTextRunBreaker {
    public static PmTextRunBreaker from(AttributedCharacterIterator iterator, PmFontRenderContext frc) {
        return new PmTextRunBreaker();
    }
}
