package com.github.terefang.jmelange.data;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.ListMapUtil;

import com.github.terefang.jmelange.script.IScriptContext;
import com.github.terefang.jmelange.script.IScriptContextFactory;
import lombok.SneakyThrows;
import picocli.CommandLine;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

public class FilterCLI {
    @CommandLine.Option(names = {"-L", "--language"}, paramLabel = "SCRIPTLNAGUAGE", description = "the script language (groovy|luay|jexl)", required = false)
    String scriptType = "luay";

    @CommandLine.Option(names = {"-s", "--script"}, paramLabel = "SCRIPTFILE", description = "the script file", required = false)
    File scriptfile;

    @CommandLine.Option(names = {"-D", "--define"}, description = "set option", required = false)
    Properties options = new Properties();

    @CommandLine.Option(names = {"-C", "--context"}, description = "contextfile", required = false)
    File contextFile;

    @CommandLine.Option(names = "-I", description = "include path", required = false)
    List<File> includePath;

    @CommandLine.Option(names = {"-f", "--from"}, paramLabel = "FROM-FORMAT", required = false)
    String infmt = "csv";
    @CommandLine.Option(names = {"-t", "--to"}, paramLabel = "TO-FORMAT", required = false)
    String outfmt = "csv";

    @CommandLine.Option(names = {"-F", "--from-charset"}, paramLabel = "FROM-CHARSET", required = false)
    String incs = "utf8";
    @CommandLine.Option(names = {"-T", "--to-charset"}, paramLabel = "TO-FORMAT", required = false)
    String outcs = "utf8";

    @CommandLine.Option(names = {"-i", "--input"}, paramLabel = "INFILE", required = false)
    File infile;
    @CommandLine.Option(names = {"-o", "--output"}, paramLabel = "OUTFILE", required = false)
    File outfile;

    @CommandLine.Parameters
    List<String> pargs = new Vector<>();

    @SneakyThrows
    public static void main(String[] args)
    {
        FilterCLI _main = new FilterCLI();
        CommandLine _cmd = new CommandLine(_main);

        if (args.length == 0
                || "--help".equalsIgnoreCase(args[0])
                || "-help".equalsIgnoreCase(args[0])
                || "help".equalsIgnoreCase(args[0])
                || "-h".equalsIgnoreCase(args[0])) {
            _cmd.usage(System.err);
            System.err.println("Input Formats:");
            System.err.println("    "+CommonUtil.join(DataUtil.listRowDataReader(), " "));
            System.err.println("Output Formats:");
            System.err.println("    "+CommonUtil.join(DataUtil.listRowDataWriter(), " "));
            System.err.print("Charsets:");
            int _i = 0;
            for(String _key : Charset.availableCharsets().keySet())
            {
                System.err.print(((_i%10==0) ? "\n    ":" ")+_key);
                _i++;
            }
            System.err.println();
            System.exit(0);

        }

        _cmd.parseArgs(args);

        for(int _i=_main.pargs.size()-1; _i>=0; _i--)
        {
            if(_main.outfile==null)
            {
                _main.outfile=new File(_main.pargs.get(_i));
            }
            else
            if(_main.infile==null)
            {
                _main.infile=new File(_main.pargs.get(_i));
            }
            else
            if(_main.scriptfile==null)
            {
                _main.scriptfile=new File(_main.pargs.get(_i));
            }
            else
            {
                throw new IllegalArgumentException(_main.pargs.get(_i));
            }
        }

        if(_main.outfile==null)
        {
            throw new IllegalArgumentException("output file not given");
        }

        if(_main.infile==null)
        {
            throw new IllegalArgumentException("input file not given");
        }

        // if(_main.scriptfile==null)
        // {
        //   throw new IllegalArgumentException("script file not given");
        // }

        if(_main.scriptfile!=null)
        {
            if(_main.scriptfile.getName().endsWith(".lua")
                || _main.scriptfile.getName().endsWith(".luay"))
            {
                _main.scriptType="luay";
            }
            else
            if(_main.scriptfile.getName().endsWith(".jx")
                || _main.scriptfile.getName().endsWith(".jexl"))
            {
                _main.scriptType="jexl";
            }
            else
            if(_main.scriptfile.getName().endsWith(".gy")
                || _main.scriptfile.getName().endsWith(".groovy"))
            {
                _main.scriptType="groovy";
            }
        }


        runScript(_main.scriptType, _main.scriptfile, _main.contextFile, _main.options, _main.includePath, _main.infmt, _main.incs, _main.infile, _main.outfmt, _main.outcs, _main.outfile);
    }

    @SneakyThrows
    public static void runScript(String _type, File _sfile, File _context1, Properties _context2, List<File> _inc, String _infmt, String _incs, File _infile, String _outfmt, String _oucs, File _outfile)
    {
        List<String> _inclist = new Vector<>();
        if(_inc!=null)
        {
            for(File _f : _inc)
            {
                _inclist.add(_f.getAbsolutePath());
            }
        }

        Map<String, Object> _ctx = null;

        if(_context1!=null)
        {
            _ctx = DataUtil.loadContextFrom(_context1);
        }
        else
        {
            _ctx = new HashMap<>();
        }

        for(String _key : _context2.stringPropertyNames())
        {
            _ctx.put(_key, _context2.getProperty(_key));
        }


        List<Map<String, Object>> _data = DataUtil.loadRowsFromType(_infmt, _infile, Charset.forName(_incs));
        List<Map<String, Object>> _odata = null;
        if(_sfile!=null)
        {
            IScriptContextFactory _iscf = IScriptContextFactory.findByName(_type);
            IScriptContext _sc = _iscf.newInstance(_sfile.getAbsolutePath(), _type, _inclist);
            _sc.setIncludes(_inclist);
            _sc.compile(_sfile);

            _odata = new ArrayList<>(_data.size());
            for(Map<String, Object> _row : _data)
            {
                List<Map<String, Object>> _res = runMapping(_sc, _ctx, _row);
                if(_res!=null && _res.size()>0)
                {
                    for(Map<String, Object> _e : _res)
                    {
                        _odata.add(_e);
                    }
                }
            }
        }
        else
        {
            _odata = _data;
        }

        if(_odata.size()>0) DataUtil.writeRowsAsType(_outfmt, _odata, _outfile);
    }
    
    private static List<Map<String, Object>> runMapping(IScriptContext _sc, Map<String, Object> _ctx, Map<String, Object> _row)
    {
        _sc.setAll(_ctx);
        Map _out = new LinkedHashMap();
        Object _res = _sc.run(ListMapUtil.toMap("_IN", _row, "_OUT", _out));
        if(_res.getClass().isArray())
        {
            return (List)Arrays.asList((Object[])_res);
        }
        else
        if(_res instanceof List)
        {
            return (List)_res;
        }
        else
        if(_res instanceof Map)
        {
            return (List)ListMapUtil.toList(_res);
        }
        else
        if(_res instanceof Boolean && ((Boolean)_res).booleanValue()==false)
        {
            return (List)null;
        }
        else
        if(CommonUtil.checkBooleanDefaultIfNull(_res, false)==false)
        {
            return (List)null;
        }
        return (List)ListMapUtil.toList(_out);
    }
}