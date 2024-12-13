package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.WritableFontData;

final class TagOffsetRecord implements Record {
  static final int RECORD_SIZE = 6;
  private static final int TAG_POS = 0;
  private static final int OFFSET_POS = 4;
  final int tag;
  final int offset;

  TagOffsetRecord(ReadableFontData data, int base) {
    this.tag = data.readULongAsInt(base + TAG_POS);
    this.offset = data.readUShort(base + OFFSET_POS);
  }

  TagOffsetRecord(int tag, int offset) {
    this.tag = tag;
    this.offset = offset;
  }

  @Override
  public int writeTo(WritableFontData newData, int base) {
    newData.writeULong(base + TAG_POS, tag);
    newData.writeUShort(base + OFFSET_POS, offset);
    return RECORD_SIZE;
  }
}
