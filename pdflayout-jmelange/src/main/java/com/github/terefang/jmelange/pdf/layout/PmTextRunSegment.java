package com.github.terefang.jmelange.pdf.ml.text;

import java.awt.geom.Rectangle2D;

public abstract class PmTextRunSegment
{
    float x; // Calculated x location of this segment on the screen
    float y; // Calculated y location of this segment on the screen

    PmBasicTextMetrics metrics; // Metrics of this text run segment
    PmTextDecorator.Decoration decoration; // Underline, srikethrough, etc.
    Rectangle2D logicalBounds = null; // Logical bounding box for the segment
    Rectangle2D visualBounds = null; // Visual bounding box for the segment

    /**
     * Returns start index of the segment
     * @return start index
     */
    abstract int getStart();

    /**
     * Returns end index of the segment
     * @return end index
     */
    abstract int getEnd();

    /**
     * Returns the number of characters in the segment
     * @return number of characters
     */
    abstract int getLength();

    /**
     * Renders this text run segment
     * @param g2d - graphics to render to
     * @param xOffset - X offset from the graphics origin to the
     * origin of the text layout
     * @param yOffset - Y offset from the graphics origin to the
     * origin of the text layout
     */
    abstract void draw(/*TODO*/Object g2d, float xOffset, float yOffset);

    /**
     * Creates black box bounds shape for the specified range
     * @param start - range sart
     * @param limit - range end
     * @return black box bounds shape
     */
    abstract Rectangle2D getCharsBlackBoxBounds(int start, int limit);

    /**
     * Returns the outline shape
     * @return outline
     */
    abstract Rectangle2D getOutline();

    /**
     * Returns visual bounds of this segment
     * @return visual bounds
     */
    abstract Rectangle2D getVisualBounds();

    /**
     * Returns logical bounds of this segment
     * @return logical bounds
     */
    abstract Rectangle2D getLogicalBounds();

    /**
     * Calculates advance of the segment
     * @return advance
     */
    abstract float getAdvance();

    /**
     * Calculates advance delta between two characters
     * @param start - 1st position
     * @param end - 2nd position
     * @return advance increment between specified positions
     */
    abstract float getAdvanceDelta(int start, int end);

    /**
     * Calculates index of the character which advance is equal to
     * the given. If the given advance is greater then the segment
     * advance it returns the position after the last character.
     * @param advance - given advance
     * @param start - character, from which to start measuring advance
     * @return character index
     */
    abstract int getCharIndexFromAdvance(float advance, int start);

    /**
     * Checks if the character doesn't contribute to the text advance
     * @param index - character index
     * @return true if the character has zero advance
     */
    abstract boolean charHasZeroAdvance(int index);

    /**
     * Calculates position of the character on the screen
     * @param index - character index
     * @return X coordinate of the character position
     */
    abstract float getCharPosition(int index);

    /**
     * Returns the advance of the individual character
     * @param index - character index
     * @return character advance
     */
    abstract float getCharAdvance(int index);

    /**
     * Collects justification information into JustificationInfo object
     * @param jInfo - JustificationInfo object
     */
    abstract void updateJustificationInfo(PmTextRunBreaker.JustificationInfo jInfo);

    /**
     * Performs justification of the segment.
     * Updates positions of individual characters.
     * @param jInfos - justification information, gathered by the previous passes
     * @return amount of growth or shrink of the segment
     */
    abstract float doJustification(PmTextRunBreaker.JustificationInfo jInfos[]);

    @Override
    public abstract Object clone();
}
