package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.OffsetRecordTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.VisibleSubTable;

public class LookupListTable extends OffsetRecordTable<LookupTable> {
  private static final int FIELD_COUNT = 0;

  LookupListTable(ReadableFontData data, boolean dataIsCanonical) {
    super(data, dataIsCanonical);
  }

  @Override
  protected LookupTable readSubTable(ReadableFontData data, boolean dataIsCanonical) {
    return new LookupTable(data, base, dataIsCanonical);
  }

  static class Builder extends OffsetRecordTable.Builder<LookupListTable, LookupTable> {

    @Override
    protected LookupListTable readTable(
        ReadableFontData data, int baseUnused, boolean dataIsCanonical) {
      return new LookupListTable(data, dataIsCanonical);
    }

    @Override
    protected VisibleSubTable.Builder<LookupTable> createSubTableBuilder() {
      return new LookupTable.Builder();
    }

    @Override
    protected VisibleSubTable.Builder<LookupTable> createSubTableBuilder(
        ReadableFontData data, boolean dataIsCanonical) {
      return new LookupTable.Builder(data, dataIsCanonical);
    }

    @Override
    protected VisibleSubTable.Builder<LookupTable> createSubTableBuilder(LookupTable subTable) {
      return new LookupTable.Builder(subTable);
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
