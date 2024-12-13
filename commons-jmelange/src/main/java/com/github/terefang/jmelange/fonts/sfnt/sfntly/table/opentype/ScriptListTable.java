package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.TagOffsetsTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.VisibleSubTable;

import java.util.EnumMap;
import java.util.Map;

public class ScriptListTable extends TagOffsetsTable<ScriptTable> {

  ScriptListTable(ReadableFontData data, boolean dataIsCanonical) {
    super(data, dataIsCanonical);
  }

  @Override
  protected ScriptTable readSubTable(ReadableFontData data, boolean dataIsCanonical) {
    return new ScriptTable(data, 0, dataIsCanonical);
  }

  public ScriptTag scriptAt(int index) {
    return ScriptTag.fromTag(tagAt(index));
  }

  public Map<ScriptTag, ScriptTable> map() {
    Map<ScriptTag, ScriptTable> map = new EnumMap<>(ScriptTag.class);
    for (int i = 0; i < count(); i++) {
      ScriptTag script;
      try {
        script = scriptAt(i);
      } catch (IllegalArgumentException e) {
        System.err.println("Invalid Script tag found: " + e.getMessage());
        continue;
      }
      map.put(script, subTableAt(i));
    }
    return map;
  }

  static class Builder extends TagOffsetsTable.Builder<ScriptListTable, ScriptTable> {

    @Override
    protected VisibleSubTable.Builder<ScriptTable> createSubTableBuilder(
        ReadableFontData data, int tag, boolean dataIsCanonical) {
      return new ScriptTable.Builder(data, 0, dataIsCanonical);
    }

    @Override
    protected VisibleSubTable.Builder<ScriptTable> createSubTableBuilder() {
      return new ScriptTable.Builder();
    }

    @Override
    protected ScriptListTable readTable(
        ReadableFontData data, int baseUnused, boolean dataIsCanonical) {
      return new ScriptListTable(data, dataIsCanonical);
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
