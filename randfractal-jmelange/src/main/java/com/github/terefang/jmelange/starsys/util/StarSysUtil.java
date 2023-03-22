package com.github.terefang.jmelange.starsys.util;

import lombok.SneakyThrows;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang3.math.NumberUtils;
import org.hjson.JsonValue;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class StarSysUtil {
    static Map<String,Object> _data;
    static JexlEngine _jexl = new JexlBuilder().create();
    private static MapContext _ctx;

    public static double calcRocheLimit(double _diameter /* km */, double _dp, double _dm /* densties */)
    {
        return 2.44 * (_diameter/2.) * Math.pow(_dp/_dm, 1./3.);
    }

    public static int calcOrbitalZone(double _orbit /* AU */, double _lum /* sol */)
    {
        if(_orbit < (0.8f*Math.sqrt(_lum)))
        {
            return 1;
        }
        else if(_orbit < (1.6f*Math.sqrt(_lum)))
        {
            return 2;
        }
        return 3;
    }

    public static double calcPlanetaryEmpiricalDensity(double _mass, double _radius, double _lum)
    { // in earth_masses, AUs and solar_luminosity, returns kg/m3
        return  (Math.pow(_mass,0.125)*Math.pow((Math.sqrt(_lum)/_radius),0.25)*1200);
    }

    public static double calcPlanetaryDiameter(double _mass, double _density)
    { // in earth_masses and kg/m3, returns km
        return  (Math.pow((((_mass*5.9742E24)/_density)/(3.14159265359/6)),0.333333333333)/1000f);
    }

    public static double calcMoonDiameter(double _mass, double _density)
    { // in moon_masses and kg/m3, returns km
        return  (Math.pow((((_mass*7.34E22)/_density)/(3.14159265359/6)),0.333333333333)/1000f);
    }

    public static double calcKothariDiameter(double _mass, double  _zone)
    { //# in kg and ?, returns km
        _mass=_mass/1.989E30f;
        double _atomic_weight=10.f*_zone;
        double _atomic_num=7.f*_zone;
        double _temp=_atomic_weight*_atomic_num;
        _temp=  ((2.f*5.71E12*Math.pow(1.989E33,(1/3)))/(6.485E12*Math.pow(_temp,(1/3))));
        double _temp2=  ((4.0032E-8)*Math.pow(_atomic_weight,(4/3))*Math.pow(1.989E33,(2/3)));
        _temp2*=Math.pow(_mass,(2/3));
        _temp2=  (_temp2 / (6.485E12 * Math.pow(_atomic_num,2)));
        _temp2+=1.0;
        _temp=_temp/_temp2;
        _temp=  ((_temp*Math.pow(_mass,(1/3)))/1.0E5);
        return _temp*2;
    }

    public static double calcVolumeDensity(double _mass, double _diameter)
    { //# in kg and km, returns kg/m3
        return  (_mass/((4.f*3.14159265359f*Math.pow((_diameter*1000/2),3))/3.f));
    }

    public static double calcEscapeVelocity(double _mass, double _diameter)
    { //# in kg and km, returns km/sec
        return  (Math.sqrt(4*6.67259e-11*_mass/(_diameter*1000))/1000);
    }

    public static double calcSurfaceAccel(double _mass, double _diameter)
    { //# in kg and km, returns m/sec2
        return  (6.67259e-11*(_mass/Math.pow((_diameter*1000/2),2)));
        //# G = 6.67259 (± 0.00030) × 10-11 kg-1 m3 s-2
    }

    public static double calcRmsVelocity(double _mol, double _lum, double _orbit)
    { //# in molar_weight, solar_luminance, AU, returns m/sec
        double _exospheric_temp = (1273*_lum/Math.pow(_orbit,2));
        return Math.sqrt((3.0 * 8314.41 * _exospheric_temp)/_mol);
    }

    public static double calcMoleculeLimit(double _mass, double _diameter, double _lum)
    { //# in kg and km, returns mol
        return ((3*Math.pow(5,2)*8.31441*1273)/Math.pow((calcEscapeVelocity(_mass,_diameter)*1000),2)*1000);
        //#return (3*(5**2)*8.31441*1273*$lum)/((escape_velo($mass,$diameter)*1000)**2)*1000;
    }

    public static double calcGasInventory(double _mass, double _escape, double _rms, double _smass, int _zone, boolean _green)
    {
        double _velocity_ratio = _escape / _rms;
        double _const = 1;
        double _gasinventory = 0;
        if (_velocity_ratio >= 5) {
            if (_zone==1) { _const=1000; }
            if (_zone==2) { _const=750; }
            if (_zone==3) { _const=2.5; }
            _gasinventory=_const*_mass/_smass;
        }
        if (_green) { _gasinventory*=100; }
        return _gasinventory;
    }

    public static double calcOpacity(double _mol, double _pressure)
    {
        double _opticaldepth = 0.0;
        if (_mol < 10) {
            _opticaldepth+=3.0;
        } else if (_mol < 20) {
            _opticaldepth+=2.34;
        } else if (_mol < 30) {
            _opticaldepth+=1.0;
        } else if (_mol < 45) {
            _opticaldepth+=0.15;
        } else if (_mol < 100) {
            _opticaldepth+=0.05;
        }
        if (_pressure > 70) {
            _opticaldepth*=8.3333;
        } else if (_pressure > 50) {
            _opticaldepth*=6.6666;
        } else if (_pressure > 30) {
            _opticaldepth*=3.3333;
        } else if (_pressure > 10) {
            _opticaldepth*=2.0;
        } else if (_pressure > 5) {
            _opticaldepth*=1.5;
        } else if (_pressure > 1.2) {
            _opticaldepth*=1.1111;
        } else if (_pressure > 0.8) {
            _opticaldepth*=1;
        } else if (_pressure > 0.5) {
            _opticaldepth*=0.5;
        }
        return _opticaldepth;
    }

    public static double calcHydroFraction(double _gas, double _diameter)
    {
        double _temp = (0.6*_gas/1000.0)*Math.pow((2*6378/_diameter),2);
        return (_temp<1) ? _temp : 1;
    }

    public static double calcCloudFraction (double _temp, double _mol, double _diameter, double _hydro, double _htype)
    {
        if (_mol<18) {
            double _surf_area=Math.PI*Math.pow(_diameter,2);
            double _hydro_mass=_surf_area*_hydro*_htype;
            double _fraction=1.839e-8*(0.00000001*_hydro_mass)*Math.exp(0.0698*(_temp-288))/_surf_area;
            return (_fraction<1) ? _fraction : 1;
        } else {
            return 0;
        }
    }

    public static double calcIceFraction(double _temp, double _hydro)
    {
        if (_temp>328) _temp=328;
        double _fract=Math.pow(((328-_temp)/90.0),5);
        if (_fract>(1.5*_hydro)) { _fract=1.5*_hydro; }
        return (_fract<1) ? _fract : 1;
    }

    public static double calcAlbedo(double _hydro, double _cloud, double _ice, double _pressure)
    {
        double _rock_fract=1-_hydro-_ice;
        double _components = 0.0;
        if (_hydro > 0.0) _components++;
        if (_ice > 0.0) _components++ ;
        if (_rock_fract > 0.0) _components++ ;
        double _cloud_adjustment = _cloud/_components;
        _rock_fract= (_rock_fract>=_cloud_adjustment) ? (_rock_fract-_cloud_adjustment) : 0;
        _hydro= (_hydro>_cloud_adjustment) ? (_hydro-_cloud_adjustment) : 0;
        _ice= (_ice>_cloud_adjustment) ? (_ice-_cloud_adjustment) : 0;
       double _cloud_part = _cloud * 0.52; // rand_about(0.52,0.2);
        //my $rock_part = ($pressure<0.00000001) ? ($rock_fract*rand_about(0.07,0.3)) : ($rock_fract*rand_about(0.15,0.1)) ;
        double _rock_part = (_pressure<0.00000001) ? (_rock_fract*0.07) : (_rock_fract*0.15) ;
        //my $water_part = $water_fract * rand_about(0.04,0.2);
        double _water_part = _hydro * 0.04;
        //my $ice_part = ($pressure<0.00000001) ? ($ice_fract*rand_about(0.5,0.3)) : ($ice_fract*rand_about(0.7,0.1)) ;
        double _ice_part = (_pressure<0.00000001) ? (_ice*0.5) : (_ice*0.7) ;
        return (_cloud_part + _rock_part + _water_part + _ice_part);
    }

    public static double calcGreenRise(double _opt,double _temp,double _pressure)
    {
        return ((Math.pow((1+(_opt*0.75)),0.25)-1)*0.43*Math.pow((_pressure),0.25));
    }

    public static double calcEffectiveTemperature(double _lum,double _orbit,double _albedo)
    {
        return Math.sqrt(Math.sqrt(_lum)/_orbit)*Math.pow(((1-_albedo)/0.7),0.25)*300;
    }

    public static double calcSurfPressure(double _gas, double _diameter, double _gravity)
    {
        return _gas*_gravity/Math.pow((2*6378/_diameter),2);
    }

    public static List<String> getList(String _expr)
    {
        Object _obj = get(_expr);
        if(_obj == null) return null;
        if(_obj instanceof List) return ((List)_obj);
        return null;
    }

    public static List<String> getKeys(String _expr)
    {
        Object _obj = get(_expr);
        if(_obj == null) return null;
        if(_obj instanceof Map) return (List) Arrays.asList(((Map)_obj).keySet().toArray());
        return null;
    }

    public static String getString(String _expr)
    {
        Object _obj = get(_expr);
        if(_obj == null) return null;
        if(_obj instanceof String) return ((String)_obj);
        return _obj.toString();
    }

    public static Float getFloat(String _expr)
    {
        Object _obj = get(_expr);
        if(_obj == null) return null;
        if(_obj instanceof Double) return ((Double)_obj).floatValue();
        if(_obj instanceof Float) return ((Float)_obj).floatValue();
        if(_obj instanceof Long) return ((Long)_obj).floatValue();
        if(_obj instanceof Integer) return ((Integer)_obj).floatValue();
        return NumberUtils.toFloat(_obj.toString());
    }

    public static Long getLong(String _expr)
    {
        Object _obj = get(_expr);
        if(_obj == null) return null;
        if(_obj instanceof Double) return ((Double)_obj).longValue();
        if(_obj instanceof Float) return ((Float)_obj).longValue();
        if(_obj instanceof Long) return ((Long)_obj).longValue();
        if(_obj instanceof Integer) return ((Integer)_obj).longValue();
        return NumberUtils.toLong(_obj.toString());
    }

    public static Integer getInt(String _expr)
    {
        Object _obj = get(_expr);
        if(_obj == null) return null;
        if(_obj instanceof Double) return ((Double)_obj).intValue();
        if(_obj instanceof Float) return ((Float)_obj).intValue();
        if(_obj instanceof Long) return ((Long)_obj).intValue();
        if(_obj instanceof Integer) return ((Integer)_obj).intValue();
        return NumberUtils.toInt(_obj.toString());
    }

    public static synchronized Object get(String _expr)
    {
        if(_data == null)
        {
            _data = loadContextFromHjson(new InputStreamReader(ClasspathResourceLoader.of("starsys.hson").getInputStream()));
            _ctx = new MapContext(_data);
        }
        Object _obj = _jexl.createExpression(_expr).evaluate(_ctx);
        return _obj;
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromHjson(Reader _source)
    {
        HashMap<String, Object> _obj = new HashMap<>();
        JsonValue _hson = JsonValue.readHjson(_source);
        for(Map.Entry<String, Object> _entry : hjsonToMap(_hson).entrySet())
        {
            _obj.put(_entry.getKey(), _entry.getValue());
        }
        return _obj;
    }

    static Map<String, Object> hjsonToMap(JsonValue _v)
    {
        Map<String, Object> _ret = new HashMap<>();
        _v.asObject().forEach(m -> _ret.put(m.getName(), hjsonToValue(m.getValue())));
        return _ret;
    }

    static Object hjsonToValue(JsonValue value)
    {
        if(value.isObject())
        {
            return hjsonToMap(value);
        }
        else
        if(value.isArray())
        {
            return hjsonToArray(value);
        }
        else
        if(value.isString())
        {
            return value.asString();
        }
        else
        if(value.isNumber())
        {
            return Double.valueOf(value.asDouble());
        }
        else
        if(value.isBoolean())
        {
            return Boolean.valueOf(value.asBoolean());
        }
        else
        if(value.isNull())
        {
            return null;
        }
        else
        {
            return value.toString();
        }
    }

    static List hjsonToArray(JsonValue value)
    {
        List _ret = new Vector();
        value.asArray().forEach(m -> _ret.add(hjsonToValue(m)));
        return _ret;
    }

    public static String lookupTabled(ArcRand _rng, String _base, String _lookup)
    {
        List<String> _list = new Vector<>();
        for(String _k : getKeys(_base+"['"+_lookup+"']"))
        {
            int _c = getInt(_base+"['"+_lookup+"']['"+_k+"']");
            for(int _i=_c; _i>0; _i--)
            {
                _list.add(_k);
            }
        }
        return _list.get((int) _rng.next(_list.size()));
    }

    public static String lookupTabled(double _rng, String _base, String _lookup)
    {
        if(_rng > 1.) _rng = (1./_rng);
        List<String> _list = new Vector<>();
        for(String _k : getKeys(_base+"['"+_lookup+"']"))
        {
            int _c = getInt(_base+"['"+_lookup+"']['"+_k+"']");
            for(int _i=_c; _i>0; _i--)
            {
                _list.add(_k);
            }
        }
        return _list.get((int) (_list.size()*_rng));
    }
}
