package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.BooleanUtil;
import com.github.terefang.jmelange.commons.util.NumberUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.github.terefang.jmelange.commons.match.basic.AbstractFilter.resolveParam;
import static com.github.terefang.jmelange.commons.match.basic.InFilter.EPSILON;

public interface IVariable {
    public static final IVariable _TRUE = IVariable.make(true);
    public static final IVariable _FALSE = IVariable.make(false);
    public static final int TYPE_RESOLVED = 0;
    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_LONG = 2;
    public static final int TYPE_DOUBLE = 3;
    public static final int TYPE_CHAR = 4;
    public static final int TYPE_STRING = 5;
    //public static final int TYPE_DATE = 6;
    //public static final int TYPE_TIME = 7;
    //public static final int TYPE_TIMESTAMP = 8;
    public static final int TYPE_MAP = 9;

    public boolean asBoolean(Object _context);
    public long asLong(Object _context);
    public double asDouble(Object _context);
    public char asChar(Object _context);
    public String asString(Object _context);
    default public Map asMap(Object _context) { return Collections.EMPTY_MAP; }
    default public boolean asBoolean() { return asBoolean(null); }
    default public long asLong() { return asLong(null); }
    default public double asDouble() { return asDouble(null); }
    default public char asChar() { return asChar(null); }
    default public String asString() { return asString(null); }
    default public Map asMap() { return asMap(null); }
    public int type();
    default IVariable resolve(Object object)
    {
        return this;
    }

    public int compareTo(IVariable v, Object _context);

    public static IVariable make(Object _val)
    {
        if(_val instanceof Map)
        {
            return make((Map)_val);
        }
        else
        if(_val instanceof Boolean)
        {
            return make(((Boolean)_val).booleanValue());
        }
        else
        if(_val instanceof Character)
        {
            return make(((Character)_val).charValue());
        }
        else
        if(_val instanceof Long)
        {
            return make(((Long)_val));
        }
        else
        if(_val instanceof Integer)
        {
            return make(((Integer)_val).longValue());
        }
        else
        if(_val instanceof Double)
        {
            return make(((Double)_val));
        }
        else
        if(_val instanceof Float)
        {
            return make(((Float)_val));
        }
        else
        if(_val instanceof String)
        {
            return make((String)_val);
        }
        return make(CommonUtil.toString(_val));
    }

    public static IVariable make(boolean _val)
    {
        BooleanVariable _v = new BooleanVariable();
        _v._var = _val;
        return _v;
    }

    static class BooleanVariable implements IVariable
    {
        boolean _var = false;
        @Override
        public boolean asBoolean(Object _context) {
            return _var;
        }

        @Override
        public long asLong(Object _context) {
            return _var ? 1L : 0L;
        }

        @Override
        public double asDouble(Object _context) {
            return _var ? 1.0 : 0.0;
        }

        @Override
        public char asChar(Object _context) {
            return _var ? 't' : 'f';
        }

        @Override
        public String asString(Object _context) {
            return _var ? "true" : "false";
        }

        @Override
        public int type() {
            return TYPE_BOOLEAN;
        }

        @Override
        public int compareTo(IVariable v, Object _context)
        {
            if(v==null) return -2;
            v = v.resolve(_context);
            if(v==null) return -2;
            if(!(v instanceof BooleanVariable)) return -2;
            if(this._var == ((BooleanVariable)v)._var) return 0;
            if(this._var == true) return -1;
            return 1;
        }
    }

    public static IVariable make(Long _val)
    {
        LongVariable _v = new LongVariable();
        _v._var = _val;
        return _v;
    }

    static class LongVariable implements IVariable
    {
        long _var = 0;
        @Override
        public boolean asBoolean(Object _context) {
            return _var!=0L;
        }

        @Override
        public long asLong(Object _context) {
            return _var;
        }

        @Override
        public double asDouble(Object _context) {
            return _var;
        }

        @Override
        public char asChar(Object _context) {
            return (char)_var;
        }

        @Override
        public String asString(Object _context) {
            return Long.toString(_var);
        }

        @Override
        public int type() {
            return TYPE_LONG;
        }

        @Override
        public int compareTo(IVariable v, Object _context)
        {
            if(v==null) return -2;
            v = v.resolve(_context);
            if(v==null) return -2;
            if(v instanceof DoubleVariable)
            {
                double _r = this._var - ((DoubleVariable)v)._var;
                if(Math.abs(_r) < EPSILON) return 0;
                if(_r < 0.0) return -1;
                return 1;
            }
            if(!(v instanceof LongVariable)) return -2;
            if(this._var == ((LongVariable)v)._var) return 0;
            if(this._var < ((LongVariable)v)._var) return -1;
            return 1;
        }
    }

    public static IVariable make(Double _val)
    {
        DoubleVariable _v = new DoubleVariable();
        _v._var = _val;
        return _v;
    }

    static class DoubleVariable implements IVariable
    {
        double _var = 0;
        @Override
        public boolean asBoolean(Object _context) {
            return _var!=0.0;
        }

        @Override
        public long asLong(Object _context) {
            return (long)_var;
        }

        @Override
        public double asDouble(Object _context) {
            return _var;
        }

        @Override
        public char asChar(Object _context) {
            return (char)_var;
        }

        @Override
        public String asString(Object _context) {
            return Double.toString(_var);
        }

        @Override
        public int type() {
            return TYPE_DOUBLE;
        }

        @Override
        public int compareTo(IVariable v, Object _context)
        {
            if(v==null) return -2;
            v = v.resolve(_context);
            if(v==null) return -2;
            if(v instanceof LongVariable)
            {
                double _r = this._var - ((LongVariable)v)._var;
                if(Math.abs(_r) < EPSILON) return 0;
                if(_r < 0.0) return -1;
                return 1;
            }
            if(!(v instanceof DoubleVariable)) return -2;
            if(Math.abs(this._var - ((DoubleVariable)v)._var) < EPSILON) return 0;
            if(this._var < ((DoubleVariable)v)._var) return -1;
            return 1;
        }
    }

    public static IVariable make(char _val)
    {
        CharVariable _v = new CharVariable();
        _v._var = _val;
        return _v;
    }

    static class CharVariable implements IVariable
    {
        char _var = 0;
        @Override
        public boolean asBoolean(Object _context) {
            return _var!=0;
        }

        @Override
        public long asLong(Object _context) {
            return (long)_var;
        }

        @Override
        public double asDouble(Object _context) {
            return (double)_var;
        }

        @Override
        public char asChar(Object _context) {
            return _var;
        }

        @Override
        public String asString(Object _context) {
            return Character.toString(_var);
        }

        @Override
        public int type() {
            return TYPE_CHAR;
        }

        @Override
        public int compareTo(IVariable v, Object _context)
        {
            if(v==null) return -2;
            v = v.resolve(_context);
            if(v==null) return -2;
            if(!(v instanceof CharVariable)) return -2;
            if(this._var == ((CharVariable)v)._var) return 0;
            if(this._var < ((CharVariable)v)._var) return -1;
            return 1;
        }
    }

    public static IVariable make(String _val)
    {
        StringVariable _v = new StringVariable();
        _v._var = _val;
        return _v;
    }

    static class StringVariable implements IVariable
    {
        String _var = "";
        @Override
        public boolean asBoolean(Object _context) {
            return BooleanUtil.checkBoolean(_var);
        }

        @Override
        public long asLong(Object _context) {
            return NumberUtil.checkLong(_var);
        }

        @Override
        public double asDouble(Object _context) {
            return NumberUtil.checkDouble(_var);
        }

        @Override
        public char asChar(Object _context) {
            return _var.charAt(0);
        }

        @Override
        public String asString(Object _context) {
            return _var;
        }

        @Override
        public int type() {
            return TYPE_STRING;
        }

        @Override
        public int compareTo(IVariable v, Object _context)
        {
            if(v==null) return -2;
            v = v.resolve(_context);
            if(v==null) return -2;
            if(!(v instanceof StringVariable)) return -2;
            return this._var.compareTo(((StringVariable)v)._var);
        }
    }

    public static IVariable makeResolved(String _param)
    {
        ResolvedVariable _v = new ResolvedVariable();
        _v._param = _param;
        return _v;
    }

    static class ResolvedVariable implements IVariable
    {
        String _param = "";
        @Override
        public boolean asBoolean(Object _context)
        {
            return resolve(_context).asBoolean(_context);
        }

        @Override
        public long asLong(Object _context) {
            return  resolve(_context).asLong(_context);
        }

        @Override
        public double asDouble(Object _context) {
            return  resolve(_context).asDouble(_context);
        }

        @Override
        public char asChar(Object _context) {
            return  resolve(_context).asChar(_context);
        }

        @Override
        public String asString(Object _context) {
            return  resolve(_context).asString(_context);
        }

        @Override
        public Map asMap(Object _context) {
            return  resolve(_context).asMap(_context);
        }

        @Override
        public int type() {
            return TYPE_RESOLVED;
        }

        @Override
        public IVariable resolve(Object _context)
        {
            return IVariable.make(resolveParam(_context, _param));
        }

        @Override
        public int compareTo(IVariable v, Object _context)
        {
            return this.resolve(_context).compareTo(v.resolve(_context), _context);
        }
    }

    public static IVariable makeResolved(String _param, String _param2)
    {
        ComplexResolvedVariable _v = new ComplexResolvedVariable();
        _v._param = _param;
        _v._param2 = _param2;
        return _v;
    }

    static class ComplexResolvedVariable extends ResolvedVariable implements IVariable
    {
        String _param2 = "";

        @Override
        public IVariable resolve(Object _context)
        {
            IVariable _var = super.resolve(_context);
            return IVariable.make(_var.asMap().get(_param2));
        }
    }

    public static IVariable make(Map _map)
    {
        MapVariable _mv = new MapVariable();
        _mv._map = _map;
        return _mv;
    }

    static class MapVariable implements IVariable
    {
        Map _map = null;

        @Override
        public boolean asBoolean(Object _context) {
            return this._map.size()>0;
        }

        @Override
        public long asLong(Object _context) {
            return 0;
        }

        @Override
        public double asDouble(Object _context) {
            return 0;
        }

        @Override
        public char asChar(Object _context) {
            return 'm';
        }

        @Override
        public String asString(Object _context) {
            return "map";
        }

        @Override
        public Map asMap(Object _context) {
            return this._map;
        }

        @Override
        public Map asMap() {
            return this._map;
        }

        @Override
        public int type() {
            return 0;
        }

        @Override
        public IVariable resolve(Object object) {
            return this;
        }

        @Override
        public int compareTo(IVariable v, Object _context) {
            return -1;
        }
    }

    public static IVariable make(String _fn, List<IVariable> _args)
    {
        FunctionVariable _fv = new FunctionVariable();
        _fv._fn = _fn;;
        _fv._args=_args;
        return _fv;
    }

    static class FunctionVariable implements IVariable
    {
        String _fn;
        List<IVariable> _args;
        @Override
        public boolean asBoolean(Object _context)
        {
            return resolve(_context).asBoolean(_context);
        }

        @Override
        public long asLong(Object _context) {
            return  resolve(_context).asLong(_context);
        }

        @Override
        public double asDouble(Object _context) {
            return  resolve(_context).asDouble(_context);
        }

        @Override
        public char asChar(Object _context) {
            return  resolve(_context).asChar(_context);
        }

        @Override
        public String asString(Object _context) {
            return  resolve(_context).asString(_context);
        }

        @Override
        public int type() {
            return TYPE_RESOLVED;
        }

        @Override
        public IVariable resolve(Object _context)
        {
            IVariable _var = resolveParam(_context, _fn, _args);
            while(_var.type()==TYPE_RESOLVED)
            {
                _var = resolveParam(_context, _fn, _args);
            }
            return _var;
        }

        @Override
        public int compareTo(IVariable v, Object _context)
        {
            return this.resolve(_context).compareTo(v.resolve(_context), _context);
        }
    }

}
