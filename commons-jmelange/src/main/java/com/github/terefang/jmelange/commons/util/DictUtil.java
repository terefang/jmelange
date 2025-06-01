package com.github.terefang.jmelange.commons.util;

import java.util.*;

public class DictUtil 
{
    public static String dformat(String _text, Map<String,Object> _dict)
    {
        return dformat(_text, _dict, Collections.emptyMap());
    }

    public static String dformat(String _text, Map<String,Object> _dict, Map<String,Object> _objs)
    {
        String _v = dformatStatic(_text, _dict);
        _v = dformatVar(_v, _dict, _objs);
        _v = dformatFunc(_v, _dict, _objs);
        return _v;
    }

    private static String resolveString_(String _key, Properties _dict)
    {
        LinkedHashMap<String, Object> _h = new LinkedHashMap<>();
        _h.putAll((Map)_dict);
        return resolveString_(_key, _h);
    }

    private static String resolveString_(String _key, Map<String,Object> _dict)
    {
        Object _ret = resolve_(_key, _dict, Collections.emptyMap());
        return _ret == null ? null : _ret.toString();
    }

    private static Object resolve_(String _key, Map<String,Object> _dict)
    {
        return resolve_(_key, _dict, Collections.emptyMap());
    }

    private static String resolveString_(String _key, Map<String,Object> _dict, Map<String,Object> _objs)
    {
        Object _ret = resolve_(_key, _dict, _objs);
        return _ret == null ? null : _ret.toString();
    }

    private static Object resolve_(String _key, Map<String,Object> _dict, Map<String,Object> _objs)
    {
		
		if(_key.indexOf("->")>0 && _dict.get(_key)==null)
		{
            List<String> _list = ListMapUtil.toList(StringUtil.splitByWholeSeparator(_key, "->"));
            Object _v = ListMapUtil.getAsObject(_dict, _list);
            if(_v==null)
            {
                _v = ListMapUtil.getAsObject(_objs, _list);
            }
            return _v;
		}
        return ListMapUtil.getAsObject(_dict, _key);
    }

    public static String dformatVar(String text, Map<String,Object> _dict)
    {
        return dformatVar(text, _dict, Collections.emptyMap());
    }

    public static String dformatVar(String text, Map<String,Object> _dict, Map<String,Object> _objs)
    {
        return dformatVarBasic(text, "${", "}", "\\|", _dict, _objs);
    }

    public static String dformatVarBasic(String text, String _pref, String _suff, String _split, Map<String,Object> dict, Map<String,Object> objs)
    {
        String v = text.trim();

        while(v.indexOf(_pref)>=0)
        {
            int start = v.indexOf(_pref);
            int end = v.indexOf(_suff, start);
            String n = v.substring(start+_pref.length(), end);

            for(String nx : n.split(_split))
            {
                n = "?"+nx.toUpperCase()+"?";
                if(nx.startsWith(":"))
                {
                    n = nx.substring(1);
                }
                else if((n = resolveString_(nx, dict, objs))!=null)
                {
                    break;
                }
            }

            v = v.substring(0, start) + n + v.substring(end+_suff.length());
        }

        return v;
    }

    public static String dformatStatic(String text, Map<String,Object> _prop)
    {
        return dformatStaticBasic(text, "$[", "]", "\\|", _prop);
    }

    public static String dformatStaticBasic(String text, String _pref, String _suff, String _split, Map<String,Object> _prop)
    {
        String v = text.trim();

        int start = 0;
        while((start = v.indexOf(_pref))>=0)
        {
            int end = v.indexOf(_suff, start);
            String n = v.substring(start+_pref.length(), end);

            for(String nx : _split==null ? new String[]{ n } : n.split(_split))
            {
                n = "?"+nx.toUpperCase()+"?";
                if(nx.startsWith(":"))
                {
                    n = nx.substring(1);
                }
                else if((n = resolveString_(nx, _prop))!=null)
                {
                    break;
                }
                else if((n = resolveString_(nx, System.getProperties()))!=null)
                {
                    break;
                }
            }

            v = v.substring(0, start) + n + v.substring(end+_suff.length());
        }

        return v;
    }

    public static String dformatFunc(String text, Map<String,Object> _dict)
    {
        return dformatFunc(text, _dict, Collections.emptyMap());
    }

    public static String dformatFunc(String text, Map<String,Object> _dict, Map<String,Object> _objs)
    {
        return dformatFuncBasic(text, "%{", "}%", _dict, _objs);
    }

    public static String dformatFuncBasic(String text, String _pref, String _suff, Map<String,Object> _dict, Map<String,Object> _objs)
    {
        String v = text;
        int end=v.length()-1;
        int start = 0;
        while((start = v.lastIndexOf(_pref,end))>=0)
        {
            if((end = v.indexOf(_suff, start))>0)
            {
                String fn[] = v.substring(start+_pref.length(), end).split("\\:");
                String n = "?NULL?";

                if(fn.length>1) n = fn[1];

                try
                {
                    if(fn[0].equalsIgnoreCase("md5") && fn.length>1)
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append(fn[1]);
                        for(int i=2; i<fn.length; i++)
                        {
                            sb.append(':');
                            sb.append(fn[i]);
                        }
                        n = HashUtil.md5Hex(sb.toString());
                    }
                    else if(fn[0].equalsIgnoreCase("sha1") && fn.length>1)
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append(fn[1]);
                        for(int i=2; i<fn.length; i++)
                        {
                            sb.append(':');
                            sb.append(fn[i]);
                        }
                        n = HashUtil.sha1Hex(sb.toString());
                    }
                    else if(fn[0].equalsIgnoreCase("uc") && fn.length>1)
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append(fn[1]);
                        for(int i=2; i<fn.length; i++)
                        {
                            sb.append(':');
                            sb.append(fn[i]);
                        }
                        n = sb.toString().toUpperCase();
                    }
                    else if(fn[0].equalsIgnoreCase("lc") && fn.length>1)
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append(fn[1]);
                        for(int i=2; i<fn.length; i++)
                        {
                            sb.append(':');
                            sb.append(fn[i]);
                        }
                        n = sb.toString().toLowerCase();
                    }
                    else if(fn[0].equalsIgnoreCase("snmp") && fn[1].equalsIgnoreCase("*"))
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append("oid="+_dict.get("snmp_oid"));
                        int i = 1;
                        while(_dict.get("V_"+i)!=null)
                        {
                            sb.append(", ["+i+": ");
                            sb.append(_dict.get("V_"+i+"_oid")+", ");
                            if(_dict.get("V_"+i+"_type")!=null)
                            {
                                sb.append("("+_dict.get("V_"+i+"_type")+") ");
                            }
                            sb.append(_dict.get("V_"+i)+"]");
                            i++;
                        }
                        n = sb.toString();
                    }
                    else if(fn[0].equalsIgnoreCase("snmp") && fn[1].equalsIgnoreCase("$"))
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append("oid="+_dict.get("snmp_oid"));
                        int i = 1;
                        while(_dict.get("V_"+i)!=null)
                        {
                            sb.append(", ["+i+": ");
                            sb.append(_dict.get("V_"+i)+"]");
                            i++;
                        }
                        n = sb.toString();
                    }
                    else if(fn[0].equalsIgnoreCase("*"))
                    {
                        StringBuilder sb = new StringBuilder();
                        for(String _k : _dict.keySet())
                        {
                            sb.append("|"+_k+"=");
                            sb.append(_dict.get(_k).toString());
                        }
                        sb.append("|");
                        n = sb.toString();
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    n = "?Format-Error -- "+ex.getMessage()+"?";
                }
                v = v.substring(0, start) + n + v.substring(end+2);
            }
            else
            {
                System.err.println("FORMAT-ERROR: f=|"+v+"| start="+start+" end="+end);
                break;
            }
            end=start;
        }

        return v;
    }

    public static void slotMap(Map<String,Object> _m, Map<String,Object> _dict)
    {
        slotMap("", _m, _dict);
    }

    public static void slotMap(String pref, Map<String,Object> _m, Map<String,Object> _dict)
    {
        for(String k : _m.keySet())
        {
            String _v = dformatVar(_m.get(k).toString(), _dict);
            _v = dformatFunc(_v, _dict).trim();
            _dict.put(pref+k, _v);
        }
    }

    public static Map<String,Object> filterByPrefix(String pref, Map<String,Object> _dict)
    {
        return filterByPrefix(pref, _dict, false);
    }

    public static Map<String,Object> filterByPrefix(String pref, Map<String,Object> _dict, boolean doCut)
    {
        Map<String,Object> prop = new LinkedHashMap<>();

        for(String k : _dict.keySet())
        {
            String v = _dict.get(k).toString();

            if(k.startsWith(pref))
            {
                if(doCut)
                {
                    prop.put(k.substring(pref.length()), v);
                }
                else
                {
                    prop.put(k, v);
                }
            }
        }

        return prop;
    }
}
