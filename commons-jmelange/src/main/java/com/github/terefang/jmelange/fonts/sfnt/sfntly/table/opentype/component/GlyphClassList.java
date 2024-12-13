package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.WritableFontData;

public class GlyphClassList extends NumRecordList {
  private GlyphClassList(WritableFontData data) {
    super(data);
  }

  private GlyphClassList(ReadableFontData data) {
    super(data);
  }

  private GlyphClassList(ReadableFontData data, int countDecrement) {
    super(data, countDecrement);
  }

  private GlyphClassList(
      ReadableFontData data, int countDecrement, int countOffset, int valuesOffset) {
    super(data, countDecrement, countOffset, valuesOffset);
  }

  public GlyphClassList(NumRecordList other) {
    super(other);
  }

  public static int sizeOfListOfCount(int count) {
    return DATA_OFFSET + count * NumRecord.RECORD_SIZE;
  }
}
