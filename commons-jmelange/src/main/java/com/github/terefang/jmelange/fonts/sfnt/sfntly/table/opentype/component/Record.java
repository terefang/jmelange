package com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.data.WritableFontData;

interface Record {
  int writeTo(WritableFontData newData, int base);
}
