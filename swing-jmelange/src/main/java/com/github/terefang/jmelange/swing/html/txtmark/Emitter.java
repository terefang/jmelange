package com.github.terefang.jmelange.swing.html.txtmark;

/**
 * Emitter interface responsible for generating output.
 *
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */

public interface Emitter
{
    void setUseExtensions(boolean useExtensions);
    
    void addLinkRef(final String key, final LinkRef linkRef);
    
    void emit(final StringBuilder out, final Block root);
}
