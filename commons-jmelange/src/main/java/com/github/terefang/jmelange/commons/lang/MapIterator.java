package com.github.terefang.jmelange.commons.lang;

import java.util.*;
import java.util.stream.Collectors;

public final class MapIterator<T> implements Iterator<T>, Iterable<T> {
    private final Vector<T> vector = new Vector<T>();
    private int index=0;
    private final boolean reuse;
    
    private Comparator<T> comparator;
    
    public Comparator<T> getComparator()
    {
        return comparator;
    }
    
    public void setComparator(Comparator<T> _comparator)
    {
        comparator = _comparator;
    }
    
    public MapIterator(boolean _reuse)
    {
        this.reuse = _reuse;
    }
    /**
     * This guards against possible concurrent modification exceptions that can happen when building from a weak
     * hash map.
     * @param m
     */
    public MapIterator(Map<T,?> m, Comparator<T> _c, boolean _reuse ) {
        this.reuse = _reuse;
        this.comparator = _c;
        while( true ) {
            try {
                reset( m ) ;
                break;
            } catch (ConcurrentModificationException cme) {
                System.out.println( "WARNING: ConcurrentModificationException Occurred. Retrying..." );
            }
        }
    }
    /**
     * Sets up the iterator with the given data. Note that this can throw a concurrent mod exception
     * with weak hash maps.
     * @param m
     */
    void reset( Map<T,?> m ) {
        vector.clear();
        vector.ensureCapacity( m.size() );
        Iterator<T> it = null;
        if(this.comparator!=null)
        {
            it = m.keySet().stream()
                    .sorted(this.comparator)
                    .collect(Collectors.toSet())
                    .iterator();
        }
        else
        {
            it = m.keySet()
                    .iterator();
        }
        
        while(it.hasNext())
        {
            T obj;
            try {
                obj = it.next();
            } catch( Exception e ) {
                throw new ConcurrentModificationException( "Failed to get next element." );
            }
            if( obj != null )
                vector.add( obj );
        }
        if( vector.capacity() > vector.size() * 4 )
            vector.trimToSize();//Don't let size get out of control just because it got big once!
        index = 0;
    }
    public boolean hasNext() {
        return index < vector.size();
    }
    public T next() {
        ++index;
        T ret = vector.get(index-1);
        vector.set(index-1, null); //gc
        if( index == vector.size() && !reuse)
            vector.clear();
        return ret;
    }
    public void remove() {
        throw new UnsupportedOperationException();
    }
    public Iterator<T> iterator() {
        return this;
    }
}