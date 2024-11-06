package com.github.terefang.jmelange.commons.lang;

import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.random.ArcRand;


import java.util.*;

public class MarkovChain<T,S>
{
    public Map<T, FollowUp<S>> getTable()
    {
        return table;
    }
    
    public void setTable(Map<T, FollowUp<S>> _table)
    {
        table = _table;
    }
    
    public void clear()
    {
        this.table.clear();
    }
    
    Map<T,FollowUp<S>> table = new HashMap<>();
    
    public void add(T _a, double _w, S _b)
    {
        if(!table.containsKey(_a))
        {
            table.put(_a, new FollowUp<S>());
        }
        table.get(_a).add(_b, _w);
    }
    
    public void add(T _a, double _w, S... _b)
    {
        if(!table.containsKey(_a))
        {
            table.put(_a, new FollowUp<S>());
        }
        
        for(S _c : _b)
        {
            table.get(_a).add(_c, _w);
        }
    }
    
    public void add(T _a, S _b)
    {
        add(_a,1.,_b);
    }
    
    public void add(T _a, S... _b)
    {
        add(_a,1.,_b);
    }
    
    public T get(ArcRand _rng)
    {
        int _index = _rng.nextInt(this.table.size());
        List<T> _lookup = new Vector<T>(this.table.keySet());
        return _lookup.get(_index);
    }

    public S get(ArcRand _rng, T _seed)
    {
        FollowUp<S> _entry = this.table.get(_seed);
        if(_entry==null) return null;
        return _entry.get(_rng);
    }
    
    public static class FollowUp<S>
    {
        List<S> states = new Vector<>();
        List<Double> weights = new Vector<>();
        double[] _weights;
        
        public List<S> list() { return states; }
        public void add(S _v)
        {
            add(_v, 1.);
        }
        
        public void add(S _v, double _w)
        {
            int _index = states.indexOf(_v);
            if(_index<0)
            {
                states.add(_v);
                weights.add(_w);
            }
            else
            {
                _w += weights.get(_index);
                weights.set(_index, _w);
            }
            _weights=null;
        }
        
        public int size()
        {
            return states.size();
        }
        
        public S get(int _i)
        {
            return states.get(_i);
        }

        public double getW(int _i)
        {
            return weights.get(_i);
        }
        
        public S get(double _factor)
        {
            if(_weights==null)
            {
                _weights = new double[weights.size()];
                for(int _i=0; _i< _weights.length;_i++)
                {
                    _weights[_i]=weights.get(_i);
                }
            }
            return ListMapUtil.pickWeighted(states,_weights,_factor);
        }

        public S get(ArcRand _rng)
        {
            if(_weights==null)
            {
                _weights = new double[weights.size()];
                for(int _i=0; _i< _weights.length;_i++)
                {
                    _weights[_i]= weights.get(_i);
                }
            }
            return ListMapUtil.pickWeighted(states,_weights,_rng);
        }
    }
}
