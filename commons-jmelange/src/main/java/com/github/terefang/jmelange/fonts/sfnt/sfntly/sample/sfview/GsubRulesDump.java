package com.github.terefang.jmelange.fonts.sfnt.sfntly.sample.sfview;

import com.github.terefang.jmelange.fonts.sfnt.sfntly.Font;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.FontFactory;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Tag;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.PostScriptTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.GlyphGroup;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.opentype.component.Rule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GsubRulesDump {
  public static void main(String[] args) throws IOException {
    String fontName = args[0];
    String txt = args[1];

    System.out.println("Rules from font: " + fontName);
    Font[] fonts = loadFont(new File(fontName));
    if (fonts == null) {
      throw new IllegalArgumentException("No font found");
    }

    Font            font        = fonts[0];
    GlyphGroup      ruleClosure = Rule.charGlyphClosure(font, txt);
    PostScriptTable post        = font.getTable(Tag.post);
    Rule.dumpLookups(font);
    System.out.println("Closure: " + ruleClosure.toString(post));
  }

  private static Font[] loadFont(File file) throws IOException {
    FontFactory fontFactory = FontFactory.getInstance();
    fontFactory.fingerprintFont(true);
    try (FileInputStream is = new FileInputStream(file)) {
      return fontFactory.loadFonts(is);
    } catch (FileNotFoundException e) {
      System.err.println("Could not load the font: " + file.getName());
      return null;
    }
  }
}
