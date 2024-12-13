package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.chaincontextsubst;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.ReadableFontData;

public class ChainSubClassRule extends ChainSubGenericRule {
  ChainSubClassRule(ReadableFontData data, int base, boolean dataIsCanonical) {
    super(data, base, dataIsCanonical);
  }

  static class Builder extends ChainSubGenericRule.Builder<ChainSubClassRule> {
    Builder() {
      super();
    }

    Builder(ChainSubClassRule table) {
      super(table);
    }

    Builder(ReadableFontData data, int base, boolean dataIsCanonical) {
      super(data, base, dataIsCanonical);
    }

    @Override
    public ChainSubClassRule subBuildTable(ReadableFontData data) {
      return new ChainSubClassRule(data, 0, true);
    }
  }
}
