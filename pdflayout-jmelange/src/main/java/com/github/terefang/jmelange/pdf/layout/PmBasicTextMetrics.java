package com.github.terefang.jmelange.pdf.ml.text;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PmBasicTextMetrics
{
    int baseLineIndex;

    float ascent;   // Ascent of the font
    float descent;  // Descent of the font
    float leading;  // External leading
    float advance;

    float italicAngle;
    float superScriptOffset;

    float underlineOffset;
    float underlineThickness;

    float strikethroughOffset;
    float strikethroughThickness;
}
