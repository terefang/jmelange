package com.github.terefang.jmelange;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.PdataUtil;
import com.github.terefang.jmelange.commons.util.StringUtil;
import com.github.terefang.jmelange.data.FilterCLI;
import com.github.terefang.jmelange.data.ToXlsxMain;
import com.github.terefang.jmelange.lang.TemplateCLI;
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

    @CommandLine.Option(names = { "-s", "--script" }, paramLabel = "SCRIPTFILE", description = "the script file", required = false)
    File scriptfile;

    @CommandLine.Option(names = "-D", description = "set option", required = false)
    Properties options = new Properties();

    @CommandLine.Option(names = { "-C", "--context" }, description = "contextfile", required = false)
    File contextFile;

    @CommandLine.Option(names = "-I", description = "include path", required = false)
    File includePath;

    @CommandLine.Parameters
    List<String> pargs = new Vector<>();
    
    static String[] _SUBS = { "lua","groovy", "jython", "jexl" };
    @SneakyThrows
    public static void main(String[] args)
    {
        if(args.length>1 && ("OBF".equalsIgnoreCase(args[0])
                ||"-obf".equalsIgnoreCase(args[0])
                ||"--obf".equalsIgnoreCase(args[0])))
        {
            System.out.println();
            System.out.println(CommonUtil.obfEncode(args[1]));
            System.out.println();
            System.exit(0);
        }
        else
        if(args.length>1 && ("XBF".equalsIgnoreCase(args[0])
                ||"-xbf".equalsIgnoreCase(args[0])
                ||"--xbf".equalsIgnoreCase(args[0])))
        {
            System.out.println();
            System.out.println(CommonUtil.obfDecode(args[1]));
            System.out.println();
            System.exit(0);
        }
        else
        if(args.length>1 && ("ABBR".equalsIgnoreCase(args[0])
                ||"-abbr".equalsIgnoreCase(args[0])
                ||"--abbr".equalsIgnoreCase(args[0])))
        {
            System.out.println();
            System.out.println(StringUtil.abbrshort(args[1]));
            System.out.println();
            System.exit(0);
        }
        else
        if(args.length>0 && "filter".equalsIgnoreCase(args[0]))
        {
            String[] pargs = new String[args.length-1];
            System.arraycopy(args,1,pargs,0,pargs.length);
            FilterCLI.main(pargs);
            System.exit(0);
        }
        else
        if(args.length>0 && "template".equalsIgnoreCase(args[0]))
        {
            String[] pargs = new String[args.length-1];
            System.arraycopy(args,1,pargs,0,pargs.length);
            TemplateCLI.main(pargs);
            System.exit(0);
        }
        else
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
        // (groovy|jython|luay|jexl
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
            printUsage(_cmd);
            System.exit(0);
        }

        boolean isSubCommand = false;
        
        for(String _sc : _SUBS)
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

        if(_main.scriptfile==null && _main.pargs.size()>0)
        {
            // the first argv must be the scriptfile
            _main.scriptfile = new File(_main.pargs.get(0));
            _main.pargs.remove(0);
        }
        
        runScript(_cmd, _main.scriptType,_main.scriptfile, _main.includePath, _main.options, _main.contextFile, _main.pargs);
    }
    
    public static void runScript(CommandLine _cmd, String _type, File _file, File _modpath, Properties _props, File _contextfile, List<String> argv)
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
        else
        {
            System.err.println("file not found.");
            printUsage(_cmd);
            System.exit(1);
        }
    }
    
    public static void printUsage(CommandLine _cmd)
    {
        _cmd.usage(System.err);
        System.err.println("\nSub-Commands:");
        System.err.println("\tluay ...");
        for(String _sc : _SUBS)
        {
            System.err.printf("\t%s ...\n", _sc);
        }
        System.err.println("\tpmltopdf ...");
        System.err.println("\tpwtool ...");
        System.err.println("\ttoxlsx ...");
        System.err.println("\tobf ...");
        System.err.println("\txbf ...");
        System.err.println("\tabbr ...");
    }

}
