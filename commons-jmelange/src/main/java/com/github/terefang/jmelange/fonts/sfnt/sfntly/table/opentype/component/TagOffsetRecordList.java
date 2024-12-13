package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.WritableFontData;

final class TagOffsetRecordList extends RecordList<TagOffsetRecord> {
  TagOffsetRecordList(WritableFontData data) {
    super(data);
  }

  TagOffsetRecordList(ReadableFontData data) {
    super(data);
  }

  static int sizeOfListOfCount(int count) {
    return DATA_OFFSET + count * TagOffsetRecord.RECORD_SIZE;
  }

  TagOffsetRecord getRecordForTag(int tag) {
    for (TagOffsetRecord record : this) {
      if (record.tag == tag) {
        return record;
      }
    }
    return null;
  }

  @Override
  protected TagOffsetRecord getRecordAt(ReadableFontData data, int offset) {
    return new TagOffsetRecord(data, offset);
  }

  @Override
  protected int recordSize() {
    return TagOffsetRecord.RECORD_SIZE;
  }
}
