package com.github.terefang.jmelange.commons.lang;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A Map that converts all keys to lowercase Strings for case insensitive
 * lookups.  This is needed for the toMap() implementation because
 * databases don't consistently handle the casing of column names.
 *
 * <p>The keys are stored as they are given [BUG #DBUTILS-34], so we maintain
 * an internal mapping from lowercase keys to the real keys in order to
 * achieve the case insensitive lookup.
 *
 * <p>Note: This implementation does not allow {@code null}
 * for key, whereas {@link LinkedHashMap} does, because of the code:
 * <pre>
 * key.toString().toLowerCase()
 * </pre>
 */
public class CaseInsensitiveHashMap extends LinkedHashMap<String, Object> {

    
    public static CaseInsensitiveHashMap create()
    {
        return new CaseInsensitiveHashMap();
    }
    
    public CaseInsensitiveHashMap() {
        super();
    }

    public CaseInsensitiveHashMap(final int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * The internal mapping from lowercase keys to the real keys.
     *
     * <p>
     * Any query operation using the key
     * ({@link #get(Object)}, {@link #containsKey(Object)})
     * is done in three steps:
     * <ul>
     * <li>convert the parameter key to lower case</li>
     * <li>get the actual key that corresponds to the lower case key</li>
     * <li>query the map with the actual key</li>
     * </ul>
     * </p>
     */
    private final Map<String, String> lowerCaseMap = new HashMap<>();

    /** {@inheritDoc} */
    @Override
    public boolean containsKey(final Object key) {
        final Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.containsKey(realKey);
        // Possible optimisation here:
        // Since the lowerCaseMap contains a mapping for all the keys,
        // we could just do this:
        // return lowerCaseMap.containsKey(key.toString().toLowerCase());
    }

    /** {@inheritDoc} */
    @Override
    public Object get(final Object key) {
        final Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
        return super.get(realKey);
    }

    /** {@inheritDoc} */
    @Override
    public Object put(final String key, final Object value) {
        /*
         * In order to keep the map and lowerCaseMap synchronized,
         * we have to remove the old mapping before putting the
         * new one. Indeed, oldKey and key are not necessaliry equals.
         * (That's why we call super.remove(oldKey) and not just
         * super.put(key, value))
         */
        final Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
        final Object oldValue = super.remove(oldKey);
        super.put(key, value);
        return oldValue;
    }

    /** {@inheritDoc} */
    @Override
    public void putAll(final Map<? extends String, ?> m) {
        for (final Map.Entry<? extends String, ?> entry : m.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            this.put(key, value);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object remove(final Object key) {
        final Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
        return super.remove(realKey);
    }
}
