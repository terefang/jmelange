package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.classdef;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.NumRecord;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.NumRecordList;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.RecordList;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.RecordsTable;

public class InnerArrayFmt1 extends RecordsTable<NumRecord>
{
  private static final int FIELD_COUNT = 1;

  public static final int START_GLYPH_INDEX = 0;
  private static final int START_GLYPH_CONST = 0;

  public InnerArrayFmt1(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
  }

  @Override
  protected RecordList<NumRecord> createRecordList(ReadableFontData data) {
    return new NumRecordList(data);
  }

  @Override
  public int fieldCount() {
    return FIELD_COUNT;
  }

  public static class Builder extends RecordsTable.Builder<InnerArrayFmt1, NumRecord> {
    public Builder(ReadableFontData data, int base, boolean dataIsCanonical) {
      super(data, base, dataIsCanonical);
    }

    @Override
    protected void initFields() {
      setField(START_GLYPH_INDEX, START_GLYPH_CONST);
    }

    @Override
    protected InnerArrayFmt1 readTable(ReadableFontData data, int base, boolean dataIsCanonical) {
      return new InnerArrayFmt1(data, base, dataIsCanonical);
    }

    @Override
    protected RecordList<NumRecord> readRecordList(ReadableFontData data, int base) {
      if (base != 0) {
        throw new UnsupportedOperationException();
      }
      return new NumRecordList(data);
    }

    @Override
    public int fieldCount() {
      return FIELD_COUNT;
    }
  }
}
