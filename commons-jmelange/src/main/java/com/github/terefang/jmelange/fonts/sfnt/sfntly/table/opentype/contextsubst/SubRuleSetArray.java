package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.contextsubst;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.CoverageTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.OffsetRecordTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.VisibleSubTable;

public class SubRuleSetArray extends OffsetRecordTable<SubRuleSet> {
  private static final int FIELD_COUNT = 1;

  private static final int COVERAGE_INDEX = 0;
  private static final int COVERAGE_DEFAULT = 0;

  public final CoverageTable coverage;

  public SubRuleSetArray(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
    int coverageOffset = getField(COVERAGE_INDEX);
    coverage = new CoverageTable(data.slice(coverageOffset), 0, dataIsCanonical);
  }

  @Override
  public SubRuleSet readSubTable(ReadableFontData data, boolean dataIsCanonical) {
    return new SubRuleSet(data, 0, dataIsCanonical);
  }

  @Override
  public int fieldCount() {
    return FIELD_COUNT;
  }

  public static class Builder extends OffsetRecordTable.Builder<SubRuleSetArray, SubRuleSet> {
    public Builder() {
      super();
    }

    public Builder(ReadableFontData data, boolean dataIsCanonical) {
      super(data, dataIsCanonical);
    }

    public Builder(SubRuleSetArray table) {
      super(table);
    }

    @Override
    protected SubRuleSetArray readTable(ReadableFontData data, int base, boolean dataIsCanonical) {
      return new SubRuleSetArray(data, base, dataIsCanonical);
    }

    @Override
    protected VisibleSubTable.Builder<SubRuleSet> createSubTableBuilder() {
      return new SubRuleSet.Builder();
    }

    @Override
    protected VisibleSubTable.Builder<SubRuleSet> createSubTableBuilder(
        ReadableFontData data, boolean dataIsCanonical) {
      return new SubRuleSet.Builder(data, dataIsCanonical);
    }

    @Override
    protected VisibleSubTable.Builder<SubRuleSet> createSubTableBuilder(SubRuleSet subTable) {
      return new SubRuleSet.Builder(subTable);
    }

    @Override
    protected void initFields() {
      setField(COVERAGE_INDEX, COVERAGE_DEFAULT);
    }

    @Override
    protected int fieldCount() {
      return FIELD_COUNT;
    }
  }
}
