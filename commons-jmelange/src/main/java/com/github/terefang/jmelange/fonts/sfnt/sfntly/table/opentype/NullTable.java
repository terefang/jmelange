package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.WritableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.VisibleSubTable;

public final class NullTable extends SubstSubtable {
  private static final int RECORD_SIZE = 0;

  NullTable(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
  }

  private NullTable(ReadableFontData data) {
    super(data, 0, false);
  }

  private NullTable() {
    super(null, 0, false);
  }

  public static final class Builder extends VisibleSubTable.Builder<NullTable>
  {
    private Builder() {}

    private Builder(ReadableFontData data, boolean dataIsCanonical) {}

    private Builder(NullTable table) {}

    @Override
    public int subDataSizeToSerialize() {
      return RECORD_SIZE;
    }

    @Override
    public int subSerialize(WritableFontData newData) {
      return RECORD_SIZE;
    }

    @Override
    public NullTable subBuildTable(ReadableFontData data) {
      return new NullTable(data);
    }

    @Override
    public void subDataSet() {}

    @Override
    protected boolean subReadyToSerialize() {
      return true;
    }
  }
}
