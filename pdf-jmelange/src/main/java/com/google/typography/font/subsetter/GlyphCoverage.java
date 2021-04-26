/*
 * Copyright (c) 2020. terefang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.typography.font.subsetter;

import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMap;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.sfntly.table.truetype.CompositeGlyph;
import com.google.typography.font.sfntly.table.truetype.Glyph;
import com.google.typography.font.sfntly.table.truetype.GlyphTable;
import com.google.typography.font.sfntly.table.truetype.LocaTable;

import java.util.*;

/**
 * A class for computing which glyphs are needed to render a given string. Currently this class is
 * quite simplistic, only using the cmap, not taking into account any ligature or complex layout.
 *
 * @author Raph Levien
 */
public class GlyphCoverage {

  public static List<Integer> getGlyphCoverage(Font font, Character[] _string) {
    CMapTable cmapTable = font.getTable(Tag.cmap);
    CMap cmap = getBestCMap(cmapTable);
    Set<Integer> coverage = new HashSet<>();
    coverage.add(0); // Always include notdef
    for (Character codepoint : _string) {
      int glyphId = cmap.glyphId(codepoint.charValue());
      touchGlyph(font, coverage, glyphId);
    }
    List<Integer> sortedCoverage = new ArrayList<>(coverage);
    Collections.sort(sortedCoverage);
    return sortedCoverage;
  }

  private static void touchGlyph(Font font, Set<Integer> coverage, int glyphId) {
    if (!coverage.contains(glyphId)) {
      coverage.add(glyphId);
      Glyph glyph = getGlyph(font, glyphId);
      if (glyph != null && glyph.glyphType() == Glyph.GlyphType.Composite) {
        CompositeGlyph composite = (CompositeGlyph) glyph;
        for (int i = 0; i < composite.numGlyphs(); i++) {
          touchGlyph(font, coverage, composite.glyphIndex(i));
        }
      }
    }
  }

  private static CMap getBestCMap(CMapTable cmapTable) {
    for (CMap cmap : cmapTable) {
      if (cmap.format() == CMap.CMapFormat.Format12.value()) {
        return cmap;
      }
    }
    for (CMap cmap : cmapTable) {
      if (cmap.format() == CMap.CMapFormat.Format4.value()) {
        return cmap;
      }
    }
    return null;
  }

  private static Glyph getGlyph(Font font, int glyphId) {
    LocaTable locaTable = font.getTable(Tag.loca);
    GlyphTable glyfTable = font.getTable(Tag.glyf);
    int offset = locaTable.glyphOffset(glyphId);
    int length = locaTable.glyphLength(glyphId);
    return glyfTable.glyph(offset, length);
  }
}
