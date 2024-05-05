package com.github.terefang.jmelange.pdf.ml.text;

import java.util.HashMap;

public class PmTextMetricsCalculator {
    PmTextRunBreaker breaker; // Associated run breaker

    // Metrics
    float ascent = 0;
    float descent = 0;
    float leading = 0;
    float advance = 0;

    private float baselineOffsets[];
    int baselineIndex;

    public static PmTextMetricsCalculator from(PmTextRunBreaker breaker) {
        PmTextMetricsCalculator _that = new PmTextMetricsCalculator();
        _that.breaker = breaker;
        _that.checkBaselines();
        return _that;
    }

    /**
     * Returns either values cached by checkBaselines method or reasonable
     * values for the TOP and BOTTOM alignments.
     * @param baselineIndex - baseline index
     * @return baseline offset
     */
    float getBaselineOffset(int baselineIndex) {
        if (baselineIndex >= 0) {
            return baselineOffsets[baselineIndex];
        } else if (baselineIndex == PmTextAttribute.BOTTOM_ALIGNMENT) {
            return descent;
        } else if (baselineIndex == PmTextAttribute.TOP_ALIGNMENT) {
            return -ascent;
        } else {
            // awt.3F=Invalid baseline index
            throw new IllegalArgumentException(Messages.getString("awt.3F")); //$NON-NLS-1$
        }
    }

    public float[] getBaselineOffsets() {
        float ret[] = new float[baselineOffsets.length];
        System.arraycopy(baselineOffsets, 0, ret, 0, baselineOffsets.length);
        return ret;
    }

    /**
     * Take baseline offsets from the first font or graphic attribute
     * and normalizes them, than caches the results.
     */
    public void checkBaselines() {
        // Take baseline offsets of the first font and normalize them
        HashMap<Integer, Object> fonts = breaker.fonts;

        Object val = fonts.get(new Integer(0));

        if (val instanceof PmFont) {
            PmFont firstFont = (PmFont) val;
            PmLineMetrics lm = firstFont.getLineMetrics(breaker.text, 0, 1, breaker.frc);
            baselineOffsets = lm.getBaselineOffsets();
            baselineIndex = lm.getBaselineIndex();
        } else if (val instanceof PmTextAttribute) {
            // Get first graphic attribute and use it
            PmTextAttribute ga = (PmTextAttribute) val;

            int align = ga.getAlignment();

            if (
                    align == PmTextAttribute.TOP_ALIGNMENT ||
                            align == PmTextAttribute.BOTTOM_ALIGNMENT
            ) {
                baselineIndex = PmTextAttribute.ROMAN_BASELINE;
            } else {
                baselineIndex = align;
            }

            baselineOffsets = new float[3];
            baselineOffsets[0] = 0;
            baselineOffsets[1] = (ga.getDescent() - ga.getAscent()) / 2.f;
            baselineOffsets[2] = -ga.getAscent();
        } else { // Use defaults - Roman baseline and zero offsets
            baselineIndex = PmTextAttribute.ROMAN_BASELINE;
            baselineOffsets = new float[3];
        }

        // Normalize offsets if needed
        if (baselineOffsets[baselineIndex] != 0) {
            float baseOffset = baselineOffsets[baselineIndex];
            for (int i = 0; i < baselineOffsets.length; i++) {
                baselineOffsets[i] -= baseOffset;
            }
        }
    }

    /**
     * Computes metrics for the text managed by the associated TextRunBreaker
     */
    void computeMetrics() {

        ArrayList<PmTextRunSegment> segments = breaker.runSegments;

        float maxHeight = 0;
        float maxHeightLeading = 0;

        for (int i = 0; i < segments.size(); i++) {
            PmTextRunSegment segment = segments.get(i);
            PmBasicTextMetrics metrics = segment.metrics;
            int baseline = metrics.baseLineIndex;

            if (baseline >= 0) {
                float baselineOffset = baselineOffsets[metrics.baseLineIndex];
                float fixedDescent = metrics.descent + baselineOffset;

                ascent = Math.max(ascent, metrics.ascent - baselineOffset);
                descent = Math.max(descent, fixedDescent);
                leading = Math.max(leading, fixedDescent + metrics.leading);
            } else { // Position is not fixed by the baseline, need sum of ascent and descent
                float height = metrics.ascent + metrics.descent;

                maxHeight = Math.max(maxHeight, height);
                maxHeightLeading = Math.max(maxHeightLeading, height + metrics.leading);
            }
        }

        // Need to increase sizes for graphics?
        if (maxHeightLeading != 0) {
            descent = Math.max(descent, maxHeight - ascent);
            leading = Math.max(leading, maxHeightLeading - ascent);
        }

        // Normalize leading
        leading -= descent;

        PmBasicTextMetrics currMetrics;
        float currAdvance = 0;

        for (int i = 0; i < segments.size(); i++) {
            PmTextRunSegment segment = segments.get(breaker.getSegmentFromVisualOrder(i));
            currMetrics = segment.metrics;

            segment.y = getBaselineOffset(currMetrics.baseLineIndex)
                    + currMetrics.superScriptOffset;
            segment.x = currAdvance;

            currAdvance += segment.getAdvance();
        }

        advance = currAdvance;
    }

    /**
     * Computes metrics and creates BasicMetrics object from them
     * @return basic metrics
     */
    public PmBasicTextMetrics createMetrics() {
        computeMetrics();
        return PmBasicTextMetrics.from(this);
    }

    /**
     * Corrects advance after justification. Gets BasicMetrics object
     * and updates advance stored into it.
     * @param metrics - metrics with outdated advance which should be corrected
     */
    public void correctAdvance(PmBasicTextMetrics metrics) {
        ArrayList<PmTextRunSegment> segments = breaker.runSegments;
        PmTextRunSegment segment = segments.get(breaker
                .getSegmentFromVisualOrder(segments.size() - 1));

        advance = segment.x + segment.getAdvance();
        metrics.advance = advance;
    }
}
