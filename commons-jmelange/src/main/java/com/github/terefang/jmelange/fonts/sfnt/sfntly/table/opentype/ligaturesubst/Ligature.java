package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.ligaturesubst;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.NumRecord;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.NumRecordList;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.RecordList;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.RecordsTable;

public class Ligature extends RecordsTable<NumRecord> {
  private static final int FIELD_COUNT = 1;

  public static final int LIG_GLYPH_INDEX = 0;
  private static final int LIG_GLYPH_DEFAULT = 0;

  Ligature(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
  }

  static class Builder extends RecordsTable.Builder<Ligature, NumRecord> {
    Builder(ReadableFontData data, boolean dataIsCanonical) {
      super(data, dataIsCanonical);
    }

    Builder() {
      super();
    }

    Builder(Ligature table) {
      super(table);
    }

    @Override
    protected Ligature readTable(ReadableFontData data, int base, boolean dataIsCanonical) {
      return new Ligature(data, base, dataIsCanonical);
    }

    @Override
    protected void initFields() {
      setField(LIG_GLYPH_INDEX, LIG_GLYPH_DEFAULT);
    }

    @Override
    protected int fieldCount() {
      return FIELD_COUNT;
    }

    @Override
    protected RecordList<NumRecord> readRecordList(ReadableFontData data, int base) {
      return new NumRecordList(data);
    }
  }

  @Override
  public int fieldCount() {
    return FIELD_COUNT;
  }

  @Override
  protected RecordList<NumRecord> createRecordList(ReadableFontData data) {
    return new NumRecordList(data, 1);
  }
}
