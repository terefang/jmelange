package com.github.terefang.jmelange.pdf.ml.io;

import java.io.IOException;
import java.io.InputStream;

public interface PmlResourceLoader {
    public InputStream getInputStream() throws IOException;
    public String getName();
}
