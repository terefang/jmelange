package com.github.terefang.jmelange.hyphen;

import java.io.Serializable;

public class TrieNode implements Serializable {

    private static final long serialVersionUID = 1L;
    
    IntTrieNodeArrayMap codePoint = new IntTrieNodeArrayMap();
    int[] points;
    
    public TrieNode() {
    }

}
