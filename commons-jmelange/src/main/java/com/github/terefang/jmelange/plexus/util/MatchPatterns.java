package com.github.terefang.jmelange.plexus.util;

import com.github.terefang.jmelange.plexus.util.MatchPattern;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of patterns to be matched
 *
 * @author Kristian Rosenvold
 */
public class MatchPatterns {
    private final com.github.terefang.jmelange.plexus.util.MatchPattern[] patterns;

    private MatchPatterns(com.github.terefang.jmelange.plexus.util.MatchPattern[] patterns) {
        this.patterns = patterns;
    }

    /**
     * <p>Checks these MatchPatterns against a specified string.</p>
     *
     * <p>Uses far less string tokenization than any of the alternatives.</p>
     *
     * @param name The name to look for
     * @param isCaseSensitive If the comparison is case sensitive
     * @return true if any of the supplied patterns match
     */
    public boolean matches(String name, boolean isCaseSensitive) {
        String[] tokenized = com.github.terefang.jmelange.plexus.util.MatchPattern.tokenizePathToString(name, File.separator);
        return matches(name, tokenized, isCaseSensitive);
    }

    public boolean matches(String name, String[] tokenizedName, boolean isCaseSensitive) {
        char[][] tokenizedNameChar = new char[tokenizedName.length][];
        for (int i = 0; i < tokenizedName.length; i++) {
            tokenizedNameChar[i] = tokenizedName[i].toCharArray();
        }
        return matches(name, tokenizedNameChar, isCaseSensitive);
    }

    public boolean matches(String name, char[][] tokenizedNameChar, boolean isCaseSensitive) {
        for (com.github.terefang.jmelange.plexus.util.MatchPattern pattern : patterns) {
            if (pattern.matchPath(name, tokenizedNameChar, isCaseSensitive)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesPatternStart(String name, boolean isCaseSensitive) {
        for (com.github.terefang.jmelange.plexus.util.MatchPattern includesPattern : patterns) {
            if (includesPattern.matchPatternStart(name, isCaseSensitive)) {
                return true;
            }
        }
        return false;
    }

    public static MatchPatterns from(String... sources) {
        final int                               length = sources.length;
        com.github.terefang.jmelange.plexus.util.MatchPattern[] result = new com.github.terefang.jmelange.plexus.util.MatchPattern[length];
        for (int i = 0; i < length; i++) {
            result[i] = com.github.terefang.jmelange.plexus.util.MatchPattern.fromString(sources[i]);
        }
        return new MatchPatterns(result);
    }

    public static MatchPatterns from(Iterable<String> strings) {
        return new MatchPatterns(getMatchPatterns(strings));
    }

    private static com.github.terefang.jmelange.plexus.util.MatchPattern[] getMatchPatterns(Iterable<String> items) {
        List<com.github.terefang.jmelange.plexus.util.MatchPattern> result = new ArrayList<com.github.terefang.jmelange.plexus.util.MatchPattern>();
        for (String string : items) {
            result.add(com.github.terefang.jmelange.plexus.util.MatchPattern.fromString(string));
        }
        return result.toArray(new MatchPattern[0]);
    }
}
