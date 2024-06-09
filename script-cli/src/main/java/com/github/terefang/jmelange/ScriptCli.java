package com.github.terefang.jmelange;

import com.github.terefang.jmelange.commons.util.PdataUtil;
import com.github.terefang.jmelange.data.ToXlsxMain;
import com.github.terefang.jmelange.passwd.PwTool;
import com.github.terefang.jmelange.pdf.ml.PmlToPdf;
import com.github.terefang.jmelange.script.IScriptContext;
import com.github.terefang.jmelange.script.IScriptContextFactory;
import lombok.SneakyThrows;
import luaycli.MainCLI;
import picocli.CommandLine;

import java.io.File;
import java.util.*;

public class ScriptCli
{
    @CommandLine.Option(names = { "-L", "--language" }, paramLabel = "SCRIPTLNAGUAGE", description = "the script language (groovy|jython|luay|jexl)", required = false)
    String scriptType = "groovy";

    @CommandLine.Option(names = { "-s", "--script" }, paramLabel = "SCRIPTFILE", description = "the script file", required = true)
    File scriptfile;

    @CommandLine.Option(names = "-D", description = "set option", required = false)
    Properties options = new Properties();

    @CommandLine.Option(names = { "-C", "--context" }, description = "contextfile", required = false)
    File contextFile;

    @CommandLine.Option(names = "-I", description = "include path", required = false)
    File includePath;

    @CommandLine.Parameters
    List<String> pargs = new Vector<>();

    @SneakyThrows
    public static void main(String[] args)
    {
        if(args.length>0 && "toxlsx".equalsIgnoreCase(args[0]))
        {
            String[] pargs = new String[args.length-1];
            System.arraycopy(args,1,pargs,0,pargs.length);
            ToXlsxMain.main(pargs);
            System.exit(0);
        }
        else
        if(args.length>0 && "pwtool".equalsIgnoreCase(args[0]))
        {
            String[] pargs = new String[args.length-1];
            System.arraycopy(args,1,pargs,0,pargs.length);
            PwTool.main(pargs);
            System.exit(0);
        }
        else
        if(args.length>0 && "pmltopdf".equalsIgnoreCase(args[0]))
        {
            String[] pargs = new String[args.length-1];
            System.arraycopy(args,1,pargs,0,pargs.length);
            PmlToPdf.main(pargs);
            System.exit(0);
        }
        else
        if(args.length>0 && "luay".equalsIgnoreCase(args[0]))
        {
            String[] pargs = new String[args.length-1];
            System.arraycopy(args,1,pargs,0,pargs.length);
            MainCLI.main(pargs);
            System.exit(0);
        }
        else
        if(args.length>0 && args[0].endsWith(".lua"))
        {
            String[] pargs = new String[args.length+1];
            System.arraycopy(args,0,pargs,1,args.length);
            pargs[0] = "-f";
            MainCLI.main(pargs);
            System.exit(0);
        }

        ScriptCli _main = new ScriptCli();
        CommandLine _cmd = new CommandLine(_main);

        if(args.length == 0
                || "--help".equalsIgnoreCase(args[0])
                || "-help".equalsIgnoreCase(args[0])
                || "-h".equalsIgnoreCase(args[0]))
        {
            _cmd.usage(System.err);
            System.err.println("\nSub-Commands:");
            System.err.println("\tluay ...");
            System.err.println("\tpmltopdf ...");
            System.err.println("\tpwtool ...");
            System.err.println("\ttoxlsx ...");
            System.exit(0);
        }

        boolean isSubCommand = false;
        String[] _subs = { "lua","groovy", "jython", "jexl" };
        for(String _sc : _subs)
        {
            if(args.length>0 && (_sc.equalsIgnoreCase(args[0])
                    || ("-"+_sc).equalsIgnoreCase(args[0])
                    || ("--"+_sc).equalsIgnoreCase(args[0])))
            {
                String[] pargs = new String[args.length-1];
                System.arraycopy(args,1,pargs,0,pargs.length);
                _cmd.parseArgs(pargs);
                _main.scriptType = _sc;
                isSubCommand = true;
                break;
            }
        }

        if(!isSubCommand)
        {
            _cmd.parseArgs(args);
        }

        runScript(_main.scriptType,_main.scriptfile, _main.includePath, _main.options, _main.contextFile, _main.pargs);
    }

    public static void runScript(String _type, File _file, File _modpath, Properties _props, File _contextfile, List<String> argv)
    {
        if(_file!=null && _file.exists())
        {
            IScriptContextFactory _btf = IScriptContextFactory.findByName(_type);
            IScriptContext _bt = _btf.newInstance();

            if(_modpath!=null  && _modpath.exists())
            {
                _bt.setIncludes(Collections.singletonList(_modpath.getAbsolutePath()));;
            }

            if(argv!=null  && argv.size()>0)
            {
                _bt.setArgs(argv);
            }

            _bt.compile(_file);

            Map<String, Object> _ctx = new HashMap<>();
            if(_contextfile!=null && _contextfile.exists())
            {
                _ctx.putAll(PdataUtil.loadFrom(_contextfile));
            }

            for(String _k : _props.stringPropertyNames())
            {
                _ctx.put(_k, _props.getProperty(_k));
            }

            Object _ret = _bt.run(_ctx);
            // System.err.println(_ret);
        }
    }
}
