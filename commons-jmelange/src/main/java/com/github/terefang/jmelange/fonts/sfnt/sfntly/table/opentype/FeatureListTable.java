package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.TagOffsetsTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.VisibleSubTable;

public class FeatureListTable extends TagOffsetsTable<FeatureTable>
{

  FeatureListTable(ReadableFontData data, boolean dataIsCanonical) {
    super(data, dataIsCanonical);
  }

  @Override
  protected FeatureTable readSubTable(ReadableFontData data, boolean dataIsCanonical) {
    return new FeatureTable(data, dataIsCanonical);
  }

  static class Builder extends TagOffsetsTable.Builder<FeatureListTable, FeatureTable> {

    protected Builder() {
      super();
    }

    protected Builder(ReadableFontData data, int base, boolean dataIsCanonical) {
      super(data, 0, false);
    }

    @Override
    protected VisibleSubTable.Builder<FeatureTable> createSubTableBuilder(
        ReadableFontData data, int tag, boolean dataIsCanonical) {
      return new FeatureTable.Builder(data, dataIsCanonical);
    }

    @Override
    protected VisibleSubTable.Builder<FeatureTable> createSubTableBuilder() {
      return new FeatureTable.Builder();
    }

    @Override
    protected FeatureListTable readTable(
        ReadableFontData data, int baseUnused, boolean dataIsCanonical) {
      return new FeatureListTable(data, dataIsCanonical);
    }

    @Override
    protected void initFields() {}

    @Override
    public int fieldCount() {
      return 0;
    }
  }

  @Override
  public int fieldCount() {
    return 0;
  }
}
