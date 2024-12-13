package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.WritableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.SubTable;

public abstract class VisibleSubTable extends SubTable
{
  private VisibleSubTable(ReadableFontData data) {
    super(data);
  }

  public abstract static class Builder<T extends SubTable> extends SubTable.Builder<T> {
    protected int serializedLength;

    protected Builder() {
      super(null);
    }

    protected Builder(ReadableFontData data) {
      super(data);
    }

    @Override
    public abstract int subSerialize(WritableFontData newData);

    /**
     * Even though public, not to be used by the end users. Made public only make it available to
     * packages under {@code com.google.typography.font.sfntly.table.opentype}.
     */
    @Override
    public abstract int subDataSizeToSerialize();

    @Override
    protected abstract void subDataSet();

    @Override
    protected abstract T subBuildTable(ReadableFontData data);
  }
}
