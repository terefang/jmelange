package com.github.terefang.jmelange.pdf.ml.text;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;

public class PmTextLayout {
    private PmTextRunBreaker breaker;
    private boolean metricsValid = false;
    private PmTextMetricsCalculator tmc;
    private PmBasicTextMetrics metrics;
    float justificationWidth = -1;

    public static PmTextLayout from(String string, PmFont font, PmFontRenderContext frc)
    {
        PmTextLayout _that = new PmTextLayout();
        AttributedString as = new AttributedString(string);
        as.addAttribute(PmTextAttribute.FONT, font);
        _that.breaker = PmTextRunBreaker.from(as.getIterator(), frc);
        return _that;
    }

    public static PmTextLayout from(String string, Map<? extends AttributedCharacterIterator.Attribute, ?> attributes, PmFontRenderContext frc)
    {
        PmTextLayout _that = new PmTextLayout();
        AttributedString as = new AttributedString(string);
        as.addAttributes(attributes, 0, string.length());
        _that.breaker = PmTextRunBreaker.from(as.getIterator(), frc);
        return _that;
    }

    public static PmTextLayout from(AttributedCharacterIterator text, PmFontRenderContext frc)
    {
        PmTextLayout _that = new PmTextLayout();
        _that.breaker = PmTextRunBreaker.from(text, frc);
        return _that;
    }

    public static PmTextLayout from(PmTextRunBreaker text)
    {
        PmTextLayout _that = new PmTextLayout();
        _that.breaker = text;
        return _that;
    }

    @Override
    public int hashCode() {
        return breaker.hashCode();
    }

    @Override
    protected Object clone() {
        PmTextLayout res = PmTextLayout.from((PmTextRunBreaker) breaker.clone());

        if (justificationWidth >= 0) {
            res.handleJustify(justificationWidth);
        }

        return res;
    }

    public boolean equals(PmTextLayout layout) {
        if (layout == null) {
            return false;
        }
        return this.breaker.equals(layout.breaker);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PmTextLayout ? equals((PmTextLayout) obj) : false;
    }

    @Override
    public String toString() { // what for?
        return super.toString();
    }

    private void updateMetrics() {
        if (!metricsValid) {
            breaker.createAllSegments();
            tmc = new TextMetricsCalculator(breaker);
            metrics = tmc.createMetrics();
            metricsValid = true;
        }
    }

    public float getAdvance() {
        updateMetrics();
        return metrics.getAdvance();
    }

    public float getAscent() {
        updateMetrics();
        return metrics.getAscent();
    }

    public byte getBaseline() {
        updateMetrics();
        return (byte) metrics.getBaseLineIndex();
    }

    public float[] getBaselineOffsets() {
        updateMetrics();
        return tmc.getBaselineOffsets();
    }

    public Shape getBlackBoxBounds(int firstEndpoint, int secondEndpoint) {
        updateMetrics();
        if (firstEndpoint < secondEndpoint) {
            return breaker.getBlackBoxBounds(firstEndpoint, secondEndpoint);
        }
        return breaker.getBlackBoxBounds(secondEndpoint, firstEndpoint);
    }

    public Rectangle2D getBounds() {
        updateMetrics();
        return breaker.getVisualBounds();
    }

    public int getCharacterCount() {
        return breaker.getCharCount();
    }

    public byte getCharacterLevel(int index) {
        if (index == -1 || index == getCharacterCount()) {
            return (byte) breaker.getBaseLevel();
        }
        return breaker.getLevel(index);
    }

    public float getDescent() {
        updateMetrics();
        return metrics.getDescent();
    }

    public PmTextLayout getJustifiedLayout(float justificationWidth) throws Error {
        float justification = breaker.getJustification();

        if (justification < 0) {
            // awt.196=Justification impossible, layout already justified
            throw new Error(Messages.getString("awt.196")); //$NON-NLS-1$
        } else if (justification == 0) {
            return this;
        }

        PmTextLayout justifiedLayout = PmTextLayout.from((PmTextRunBreaker) breaker.clone());
        justifiedLayout.handleJustify(justificationWidth);
        return justifiedLayout;
    }

    public float getLeading() {
        updateMetrics();
        return metrics.getLeading();
    }

    public Shape getOutline(AffineTransform xform) {
        breaker.createAllSegments();

        GeneralPath outline = breaker.getOutline();

        if (outline != null && xform != null) {
            outline.transform(xform);
        }

        return outline;
    }

    public float getVisibleAdvance() {
        updateMetrics();

        // Trailing whitespace _SHOULD_ be reordered (Unicode spec) to
        // base direction, so it is also trailing
        // in logical representation. We use this fact.
        int lastNonWhitespace = breaker.getLastNonWhitespace();

        if (lastNonWhitespace < 0) {
            return 0;
        } else if (lastNonWhitespace == getCharacterCount()-1) {
            return getAdvance();
        } else if (justificationWidth >= 0) { // Layout is justified
            return justificationWidth;
        } else {
            breaker.pushSegments(
                    breaker.getACI().getBeginIndex(),
                    lastNonWhitespace + breaker.getACI().getBeginIndex() + 1
            );

            breaker.createAllSegments();

            float visAdvance = tmc.createMetrics().getAdvance();

            breaker.popSegments();
            return visAdvance;
        }
    }

    protected void handleJustify(float justificationWidth) {
        float justification = breaker.getJustification();

        if (justification < 0) {
            // awt.196=Justification impossible, layout already justified
            throw new IllegalStateException(Messages.getString("awt.196")); //$NON-NLS-1$
        } else if (justification == 0) {
            return;
        }

        float gap = (justificationWidth - getVisibleAdvance()) * justification;
        breaker.justify(gap);
        this.justificationWidth = justificationWidth;

        // Correct metrics
        tmc = PmTextMetricsCalculator.from(breaker);
        tmc.correctAdvance(metrics);
    }

    public boolean isLeftToRight() {
        return breaker.isLTR();
    }

    public boolean isVertical() {
        return false;
    }
}
