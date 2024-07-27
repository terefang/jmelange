package com.github.terefang.jmelange.data.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
public class CsvUtil {

    @SneakyThrows
    public static void writeAsCsv(Writer _out, String _fmt, boolean _printHeader, List<Map<String, Object>> _res)
    {
        if(_res.size()>0)
        {
            CSVPrinter _cp = null;
            if("scsv".equalsIgnoreCase(_fmt))
            {
                _cp = new CSVPrinter(_out, _SCSV);
            }
            else
            if("sqlite-csv".equalsIgnoreCase(_fmt))
            {
                _cp = new CSVPrinter(_out, _SQLITECSV);
            }
            else
            if("sqlite-list".equalsIgnoreCase(_fmt))
            {
                _cp = new CSVPrinter(_out, _SQLITELIST);
            }
            else
            {
                for(CSVFormat.Predefined _p : CSVFormat.Predefined.values())
                {
                    if(_p.name().equalsIgnoreCase(_fmt))
                    {
                        _cp = new CSVPrinter(_out, _p.getFormat());
                        break;
                    }
                }
            }

            if(_cp == null)
            {
                _cp = new CSVPrinter(_out, CSVFormat.valueOf(_fmt));
            }

            boolean _first = true;
            for (Map<String, Object> _row : _res)
            {
                if (_first && _printHeader) _cp.printRecord(_row.keySet());
                _cp.printRecord(_row.values());
                _first = false;
            }
        }
        else
        {
            _out.append("no records found...\n");
        }
    }

    @SneakyThrows
    public static List<Map<String, Object>> readFileCsv(String _infmt, InputStream _in)
    {
        return readFileCsv(_infmt, _in, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static List<Map<String, Object>> readFileCsv(String _infmt, InputStream _in, Charset _cs) {
        BufferedReader _inr = new BufferedReader(new InputStreamReader(_in, _cs), 65536);
        return readFileCsv(_infmt, _inr);
    }

    @SneakyThrows
    public static List<Map<String, Object>> readFileCsv(String _infmt, File _in, Charset _cs) {
        BufferedReader _inr = new BufferedReader(new FileReader(_in, _cs), 65536);
        return readFileCsv(_infmt, _inr);
    }

    @SneakyThrows
    public static List<Map<String, Object>> readFileCsv(String _infmt, Reader _inr)
    {
        List<Map<String, Object>> _res = new Vector<>();
        List<String> _hs = new Vector<>();

        try {
            CSVParser parser = null;
            if("scsv".equalsIgnoreCase(_infmt))
            {
                parser = new CSVParser(_inr, _SCSV);
            }
            else
            if("sqlite-csv".equalsIgnoreCase(_infmt))
            {
                parser = new CSVParser(_inr, _SQLITECSV);
            }
            else
            if("sqlite-list".equalsIgnoreCase(_infmt))
            {
                parser = new CSVParser(_inr, _SQLITELIST);
            }
            else
            {
                for(CSVFormat.Predefined _p : CSVFormat.Predefined.values())
                {
                    if(_p.name().equalsIgnoreCase(_infmt))
                    {
                        parser = new CSVParser(_inr, _p.getFormat());
                        break;
                    }
                }
            }

            if(parser == null)
            {
                parser = new CSVParser(_inr, CSVFormat.valueOf(_infmt));
            }

            boolean _first = true;
            for(CSVRecord _row : parser.getRecords())
            {
                if(_first)
                {
                    for(int _i = 0; _i<_row.size(); _i++)
                    {
                        String _k = _row.get(_i);
                        if(StringUtils.isNotEmpty(_k) && StringUtils.isNotBlank(_k))
                        {
                            _hs.add(_k);
                        }
                        else
                        {
                            _hs.add(String.format("_%04d", _i));
                        }
                    }
                    _first = false;
                }
                else
                {
                    Map<String,Object> _map = new LinkedHashMap<>();
                    for(int _i = 0; _i<_hs.size(); _i++)
                    {
                        try
                        {
                            String _v = _row.get(_i);
                            String _h = _hs.get(_i);
                            if(_v!=null) _map.put(_h, _v);
                        }
                        catch (Exception _xe)
                        {
                            log.error(_xe.getMessage()+" on row="+_row.toString());
                        }
                    }
                    if(_map.size()==0) {
                        log.error(_row.toString());
                    }
                    else
                    {
                        _res.add(_map);
                    }
                }
            }
        }
        finally {
            IOUtil.close(_inr);
        }

        return _res;
    }

    public static final CSVFormat _SCSV = CSVFormat.newFormat(';')
            .withAllowDuplicateHeaderNames()
            .withEscape('\\')
            .withQuote('"')
            .withQuoteMode(QuoteMode.MINIMAL)
            .withRecordSeparator('\n')
            .withTrim();

    public static final CSVFormat _SQLITECSV = CSVFormat.newFormat(',')
            .withAllowDuplicateHeaderNames()
            .withEscape('\\')
            .withQuote('"')
            .withQuoteMode(QuoteMode.ALL_NON_NULL)
            .withRecordSeparator('\n')
            .withTrim();

    public static final CSVFormat _SQLITELIST = CSVFormat.newFormat('|')
            .withAllowDuplicateHeaderNames()
            .withEscape('\\')
            .withRecordSeparator('\n')
            .withTrim();

    @SneakyThrows
    public static void writeAsKeyValue(Appendable _out, List<Map<String, Object>> _res, String _Fs, String _Rs)
    {
        if(_res.size()>0)
        {
            String[] _h = _res.get(0).keySet().toArray(new String[0]);

            for (Map<String, Object> _row : _res) {
                Object[] _arr = _row.values().toArray();
                boolean _first = true;
                for(int _j = 0; _j<_h.length; _j++)
                {
                    String _k = Objects.toString(_h[_j]);
                    String _v = Objects.toString(_arr[_j]);
                    _k = (_k==null ? "<null>" : _k.replace('"','\''));
                    _v = (_v==null ? "<null>" : _v.replace('"','\''));
                    if(_first)
                    {
                        _out.append(MessageFormat.format("\"{0}\"=\"{1}\"", _k, _v));
                        _first = false;
                    }
                    else
                    {
                        _out.append(MessageFormat.format("{2}\"{0}\"=\"{1}\"", _k, _v, _Fs));
                    }
                }
                _out.append(_Rs);
            }
        }
        else
        {
            _out.append("no records found..."+_Rs);
        }
    }
}
