package com.github.terefang.jmelange.data;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.FileUtil;
import com.github.terefang.jmelange.data.impl.XlsxSheetByRowWriter;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;
import picocli.CommandLine;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ToXlsxMain implements Runnable{
    @CommandLine.Option(names = { "-o", "--out" }, paramLabel = "outfile.xlsx", required = true)
    File outFile;

    @CommandLine.Option(names = { "-T", "--type" }, paramLabel = "CSVTYPE", description = "the csv type (scsv|sqlite-csv|sqlite-list|excel|tdf|mysql|rfc4180|default)", required = false)
    String fileType = "default";

    @CommandLine.Option(names = { "-C", "--charset" }, paramLabel = "CHARSET", description = "the charset encoding (utf-8, ...)", required = false)
    Charset charSet = StandardCharsets.UTF_8;

    @CommandLine.Option(names = "-N", description = "named sheet file", required = false)
    LinkedHashMap<String,File> namedFiles = new LinkedHashMap();

    @CommandLine.Parameters
    List<File> pargs = new Vector<>();

    public static void main(String[] args)
    {
        ToXlsxMain _main = new ToXlsxMain();
        CommandLine _cmd = new CommandLine(_main);

        if(args.length == 0
                || "--help".equalsIgnoreCase(args[0])
                || "-help".equalsIgnoreCase(args[0])
                || "-h".equalsIgnoreCase(args[0]))
        {
            _cmd.usage(System.err);
            System.exit(0);
        }

        _cmd.parseArgs(args);

        if(_main.namedFiles.isEmpty() && _main.pargs.isEmpty())
        {
            _cmd.usage(System.err);
            System.err.println("No Input Files.");
            System.exit(1);
        }

        _main.run();
    }

    @SneakyThrows
    @Override
    public void run() {
        XlsxSheetByRowWriter _xl = XlsxSheetByRowWriter.from(this.outFile);
        // named files have priority
        for(Map.Entry<String, File> _nf : this.namedFiles.entrySet())
        {
            if(!writeSheet(_xl,_nf.getKey(),_nf.getValue()))
            {
                _xl.close();
                System.exit(1);
            }
        }

        for(File _f : this.pargs)
        {
            if(!writeSheet(_xl,FileUtil.removeExtension(_f.getName()),_f))
            {
                _xl.close();
                System.exit(1);
            }
        }
        _xl.close();
    }

    public boolean writeSheet(XlsxSheetByRowWriter _xl, String _name, File _csv)
    {
        try
        {
            List<Map<String, Object>> _data = CsvUtil.readFileCsv(this.fileType, new FileInputStream(_csv), this.charSet);
            _xl.newSheet(_name,new Vector<>(_data.get(0).keySet()));
            for(Map<String, Object> _line : _data)
            {
                _xl.write(_line);
            }
            System.err.printf("OK: %s: %d rows written.\n",_csv.getName(),_data.size());
            return true;
        }
        catch (Exception _xe)
        {
            System.err.printf("ERROR: %s: %s\n",_csv.getName(),_xe.getMessage());
            return false;
        }
    }
}
