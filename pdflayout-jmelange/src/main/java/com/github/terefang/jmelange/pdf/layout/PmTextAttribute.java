package com.github.terefang.jmelange.pdf.ml.text;

import java.text.AttributedCharacterIterator;

public class PmTextAttribute extends AttributedCharacterIterator.Attribute {
    public static final AttributedCharacterIterator.Attribute FONT = new PmTextAttribute("FONT");

    /**
     * Constructs an {@code Attribute} with the given name.
     *
     * @param name the name of {@code Attribute}
     */
    protected PmTextAttribute(String name) {
        super(name);
    }
}
