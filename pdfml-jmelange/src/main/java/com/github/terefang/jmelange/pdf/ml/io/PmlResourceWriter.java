package com.github.terefang.jmelange.pdf.ml.io;

import java.io.OutputStream;

public interface PmlResourceWriter {
    public OutputStream getOutputStream();
    public String getName();
}
