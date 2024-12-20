/*
 * Copyright 2010 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.terefang.jmelange.fonts.sfnt.sfntly.sample.sflint;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.Font;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.FontFactory;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Tag;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.HorizontalHeaderTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.HorizontalMetricsTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.NameTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.OS2Table;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.truetype.CompositeGlyph;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.truetype.Glyph;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.truetype.GlyphTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.truetype.LocaTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/** @author Raph Levien */
public class SFLint {

  private final FontFactory fontFactory;
  private int problemCount;

  public static void main(String[] args) throws IOException {
    SFLint dumper = new SFLint();
    File fontFile = null;

    for (String arg : args) {
      String option = null;
      if (arg.charAt(0) == '-') {
        option = arg.substring(1);
      }

      if (option != null) {
        if (option.equals("h") || option.equals("help") || option.equals("?")) {
          printUsage();
          System.exit(0);
        }
      } else {
        fontFile = new File(arg);
        break;
      }
    }

    if (fontFile != null) {
      dumper.lintFontFile(fontFile);
    }
  }

  private static void printUsage() {
    System.out.println("SFLint [-?|-h|-help] fontfile");
    System.out.println("find problems with the font file");
    System.out.println("\t-?,-h,-help\tprint this help information");
  }

  public SFLint() {
    fontFactory = FontFactory.getInstance();
  }

  public void lintFontFile(File fontFile) throws IOException {
    try (FileInputStream fis = new FileInputStream(fontFile)) {
      Font[] fontArray = fontFactory.loadFonts(fis);
      for (Font font : fontArray) {
        lintFont(font);
      }
    }
  }

  private void lintNameTable(Font font) {
    // Test if name entries are consistent. Logic is adapted from fix_full_font_name in
    // font_optimizer.
    NameTable name = font.getTable(Tag.name);
    for (NameTable.NameEntry entry : name) {
      // System.out.println(entry);
      if (entry.nameId() == NameTable.NameId.FontFamilyName.value()) {
        for (NameTable.NameEntry entry2 : name) {
          if (entry2.nameId() == NameTable.NameId.FullFontName.value()
              && entry.platformId() == entry2.platformId()
              && entry.encodingId() == entry2.encodingId()
              && entry.languageId() == entry2.languageId()) {
            if (!entry2.name().startsWith(entry.name())) {
              reportProblem(
                  "Full font name doesn't begin with family name: "
                      + "FontFamilyName = "
                      + entry.name()
                      + "; FullFontName = "
                      + entry2.name());
            }
          }
        }
      }
    }
  }

  private void lintWindowsClipping(Font font) {
    LocaTable  loca       = font.getTable(Tag.loca);
    int        nGlyphs    = loca.numGlyphs();
    GlyphTable glyphTable = font.getTable(Tag.glyf);
    int        bbox_xMin  = 0;
    int bbox_yMin = 0;
    int bbox_xMax = 0;
    int bbox_yMax = 0;
    for (int glyphId = 0; glyphId < nGlyphs; glyphId++) {
      int offset = loca.glyphOffset(glyphId);
      int   length = loca.glyphLength(glyphId);
      Glyph glyph  = glyphTable.glyph(offset, length);
      if (glyph != null && glyph.numberOfContours() != 0) {
        int xMin = glyph.xMin();
        int yMin = glyph.yMin();
        int xMax = glyph.xMax();
        int yMax = glyph.yMax();
        if (glyphId == 0 || xMin < bbox_xMin) {
          bbox_xMin = xMin;
        }
        if (glyphId == 0 || yMin < bbox_yMin) {
          bbox_yMin = yMin;
        }
        if (glyphId == 0 || xMax > bbox_xMax) {
          bbox_xMax = xMax;
        }
        if (glyphId == 0 || yMax > bbox_yMax) {
          bbox_yMax = yMax;
        }
      }
    }
    OS2Table os2 = font.getTable(Tag.OS_2);
    if (os2.usWinAscent() < bbox_yMax) {
      reportProblem("font is clipped on top by " + (bbox_yMax - os2.usWinAscent()) + " units");
    }
    if (os2.usWinDescent() < -bbox_yMin) {
      reportProblem("font is clipped on bottom by " + (-bbox_yMin - os2.usWinDescent()) + " units");
    }
  }

  private void lintAdvanceWidths(Font font) {
    int                    maxAdvanceWidth = 0;
    HorizontalMetricsTable hmtx            = font.getTable(Tag.hmtx);
    for (int i = 0; i < hmtx.numberOfHMetrics(); i++) {
      int advanceWidth = hmtx.hMetricAdvanceWidth(i);
      if (i == 0 || advanceWidth > maxAdvanceWidth) {
        maxAdvanceWidth = advanceWidth;
      }
    }
    HorizontalHeaderTable hhea    = font.getTable(Tag.hhea);
    int                   hheaMax = hhea.advanceWidthMax();
    if (maxAdvanceWidth != hhea.advanceWidthMax()) {
      reportProblem("advanceWidthMax mismatch, expected " + maxAdvanceWidth + " got " + hheaMax);
    }
  }

  private void lintCompositeGlyph(Font font, CompositeGlyph glyph, int glyphId) {
    int VAR_FLAGS =
        CompositeGlyph.FLAG_WE_HAVE_A_SCALE
            | CompositeGlyph.FLAG_WE_HAVE_AN_X_AND_Y_SCALE
            | CompositeGlyph.FLAG_WE_HAVE_A_TWO_BY_TWO;
    int MASK =
        ~(CompositeGlyph.FLAG_MORE_COMPONENTS
            | CompositeGlyph.FLAG_WE_HAVE_INSTRUCTIONS
            | CompositeGlyph.FLAG_USE_MY_METRICS);
    for (int i = 0; i < glyph.numGlyphs(); i++) {
      if ((glyph.flags(i) & VAR_FLAGS) == 0) {
        // check for duplicate occurrences of same reference
        for (int j = 0; j < i; j++) {
          if ((glyph.flags(i) & MASK) == (glyph.flags(j) & MASK)
              && glyph.glyphIndex(i) == glyph.glyphIndex(j)
              && glyph.argument1(i) == glyph.argument1(j)
              && glyph.argument2(i) == glyph.argument2(j)) {
            reportProblem("glyph " + glyphId + " contains duplicate references");
          }
        }
      }
    }
  }

  private void lintAllGlyphs(Font font) {
    LocaTable loca = font.getTable(Tag.loca);
    GlyphTable glyphTable = font.getTable(Tag.glyf);
    int nGlyphs = loca.numGlyphs();
    for (int glyphId = 0; glyphId < nGlyphs; glyphId++) {
      int offset = loca.glyphOffset(glyphId);
      int length = loca.glyphLength(glyphId);
      Glyph glyph = glyphTable.glyph(offset, length);
      if (glyph != null) {
        if (glyph.glyphType() == Glyph.GlyphType.Composite) {
          lintCompositeGlyph(font, (CompositeGlyph) glyph, glyphId);
        }
      }
    }
  }

  private void lintOS2Misc(Font font) {
    OS2Table os2 = font.getTable(Tag.OS_2);
    int widthClass = os2.usWidthClass();
    if (widthClass < 1 || widthClass > 9) {
      reportProblem("widthClass must be [1..9] inclusive, was " + widthClass + "; IE9 fail");
    }
    int weightClass = os2.usWeightClass();
    if (weightClass < 100 || weightClass > 900) {
      reportProblem("weightClass must be [100..900] inclusive, was " + weightClass);
    } else if ((weightClass % 100) != 0) {
      reportProblem("weightClass must be multiple of 100, was " + weightClass);
    }
  }

  private void lintFont(Font font) {
    problemCount = 0;

    lintNameTable(font);
    lintWindowsClipping(font);
    lintAdvanceWidths(font);
    lintAllGlyphs(font);
    lintOS2Misc(font);

    if (problemCount == 0) {
      System.out.println("No problems found");
    }
  }

  /**
   * Report a problem. Right now this just prints to stdout, but we'll probably want a more
   * sophisticated reporting approach soon.
   */
  private void reportProblem(String description) {
    problemCount++;
    System.out.println(description);
  }
}
