package com.github.terefang.jmelange.lang;

import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.commons.util.LdataUtil;
import com.github.terefang.jmelange.data.DataUtil;
import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import lombok.SneakyThrows;
import picocli.CommandLine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

public class TemplateCLI
{
    public static String _TYPE_BA = "basic";
    public static String _TYPE_FM = "freemarker";
    public static String _TYPE_JJ = "jinjava";
    public static String _TYPE_TL = "thymeleaf";
    public static String _TYPE_TR = "trimou";
    public static String _TYPE_GS = "gsimple";
    public static String _TYPE_JE = "ejxl";
    
    
    
    public static String[] _TYPE = {_TYPE_BA, _TYPE_FM, _TYPE_JJ, _TYPE_TL, _TYPE_TR, _TYPE_GS, _TYPE_JE};
    @CommandLine.Option(names = { "-F", "--format" }, paramLabel = "TEMPLATEFORMAT", description = "the template language (freemarker|jinjava|thymeleaf|trimou|...)", required = false)
    String formatType;
    
    @CommandLine.Option(names = { "-t", "--template" }, paramLabel = "TEMPLATEFILE", description = "the template file", required = false)
    File scriptfile;
    
    @CommandLine.Option(names = { "-o", "--outfile" }, paramLabel = "OUTFILE", description = "the output file", required = false)
    File outfile;
    
    @CommandLine.Option(names = "-D", description = "set option", required = false)
    Properties options = new Properties();
    
    @CommandLine.Option(names = { "-C", "--context" }, description = "CONTEXTFILE", required = false)
    File contextFile;
    @CommandLine.Option(names = { "-N", "--cname" }, description = "CONTEXTNAME", required = false)
    String contextName = null;
    
    @CommandLine.Parameters
    List<String> pargs = new Vector<>();
    
    @SneakyThrows
    public static void main(String[] args)
    {
        TemplateCLI   _main = new TemplateCLI();
        CommandLine _cmd  = new CommandLine(_main);
        
        if(args.length == 0
                || "--help".equalsIgnoreCase(args[0])
                || "-help".equalsIgnoreCase(args[0])
                || "-h".equalsIgnoreCase(args[0]))
        {
            printUsage(_cmd);
            System.exit(0);
        }
        
        boolean isSubCommand = false;

        for(String _tt :_TYPE)
        {
            if(args.length>0 && (_tt.equalsIgnoreCase(args[0])
                    || ("-"+_tt).equalsIgnoreCase(args[0])
                    || ("--"+_tt).equalsIgnoreCase(args[0])))
            {
                String[] pargs = new String[args.length-1];
                System.arraycopy(args,1,pargs,0,pargs.length);
                _cmd.parseArgs(pargs);
                _main.formatType = _tt;
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
        
        if(_main.outfile==null && _main.pargs.size()>0)
        {
            // the first argv must be the scriptfile
            _main.outfile = new File(_main.pargs.get(0));
            _main.pargs.remove(0);
        }
        
        runScript(_cmd, _main.formatType,_main.scriptfile, _main.outfile, _main.options, _main.contextFile, _main.contextName, _main.pargs);
    }
    
    @SneakyThrows
    public static void runScript(CommandLine _cmd, String _type, File _file, File _outfile, Properties _props, File _contextfile, String _contextname, List<String> argv)
    {
        if(_file!=null && _file.exists())
        {
            ITemplateContextFactory _btf = ITemplateContextFactory.findByName(_type);
            ITemplateContext        _bt  = _btf.newInstance();

            if(_outfile!=null && argv!=null  && argv.size()>0)
            {
                _outfile = new File(argv.get(0));
                argv.remove(0);
            }
            
            if(argv!=null  && argv.size()>0)
            {
                _bt.setArgs(argv);
            }
            
            _bt.compile(_file);
            
            Map<String, Object> _ctx = new HashMap<>();
            if(_contextfile!=null && _contextfile.exists())
            {
                Map<String,Object> _map = null;
                if(!_contextfile.getName().endsWith(".ldata"))
                {
                    _map = DataUtil.loadContextFrom(_contextfile);
                }
                else
                {
                    _map = LdataUtil.loadFrom(_contextfile);
                }
                
                if(_contextname!=null)
                {
                    _ctx.put(_contextname, _map);
                }
                else
                {
                    _ctx.putAll(_map);
                }
            }
            
            for(String _k : _props.stringPropertyNames())
            {
                _ctx.put(_k, _props.getProperty(_k));
            }
            
            OutputStream _out = null;
            if(_outfile!=null)
            {
                _out = new BufferedOutputStream(new FileOutputStream(_outfile), 1<<16);
            }
            else
            {
                _out = System.out;
            }
            Object _ret = _bt.run(_ctx, _out);
            IOUtil.flush(_out);
            IOUtil.close(_out);
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
        for(String _sc : _TYPE)
        {
            System.err.printf("\t%s ...\n", _sc);
        }
    }
}
