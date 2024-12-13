package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.chaincontextsubst;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.CoverageTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.NumRecordList;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.OffsetRecordTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.VisibleSubTable;

public class CoverageArray extends OffsetRecordTable<CoverageTable> {
  private static final int FIELD_COUNT = 0;

  private CoverageArray(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
  }

  public CoverageArray(NumRecordList records) {
    super(records);
  }

  @Override
  protected CoverageTable readSubTable(ReadableFontData data, boolean dataIsCanonical) {
    return new CoverageTable(data, 0, dataIsCanonical);
  }

  public static class Builder extends OffsetRecordTable.Builder<CoverageArray, CoverageTable> {

    public Builder(NumRecordList records) {
      super(records);
    }

    @Override
    protected CoverageArray readTable(ReadableFontData data, int base, boolean dataIsCanonical) {
      return new CoverageArray(data, base, dataIsCanonical);
    }

    @Override
    protected VisibleSubTable.Builder<CoverageTable> createSubTableBuilder() {
      return new CoverageTable.Builder();
    }

    @Override
    protected VisibleSubTable.Builder<CoverageTable> createSubTableBuilder(
        ReadableFontData data, boolean dataIsCanonical) {
      return new CoverageTable.Builder(data, dataIsCanonical);
    }

    @Override
    protected VisibleSubTable.Builder<CoverageTable> createSubTableBuilder(CoverageTable subTable) {
      return new CoverageTable.Builder(subTable);
    }

    @Override
    protected void initFields() {}

    @Override
    public int fieldCount() {
      return FIELD_COUNT;
    }
  }

  @Override
  public int fieldCount() {
    return FIELD_COUNT;
  }
}
