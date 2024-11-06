package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.ZipUtil;
import com.github.terefang.jmelange.commons.zip.ByFileArchiver;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.ByRowDataWriter;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;
import com.github.terefang.jmelange.apache.csv.CSVPrinter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvZipByRowWriter implements AbstractDataExchange, ByRowDataWriter
{
	static String DATANAME = "csv-zip";
	static List<String> DATANAMES = Collections.unmodifiableList(Arrays.asList("csv-zip"));
	static List<String> DATAEXTS = Collections.unmodifiableList(Arrays.asList(".csv.zip"));

	@Override
	public String getName() {
		return DATANAME;
	}

	@Override
	public List<String> getNames() {
		return DATANAMES;
	}

	@Override
	public List<String> getExts() {
		return DATAEXTS;
	}

	private ByFileArchiver _out;
	private Vector<String> _columns;
	private CSVPrinter _cp;
	private String _sheet;
	private StringBuilder _csv;

	@SneakyThrows
	public static CsvZipByRowWriter from(File _file)
	{
		CsvZipByRowWriter _csv = new CsvZipByRowWriter();
		_csv.open(_file);
		return _csv;
	}

	@SneakyThrows
	public static CsvZipByRowWriter from(OutputStream _file)
	{
		CsvZipByRowWriter _csv = new CsvZipByRowWriter();
		_csv.open(_file);
		return _csv;
	}

	@SneakyThrows
	public void open(File _out) {
		this.open(new FileOutputStream(_out));
	}

	@SneakyThrows
	public void open(OutputStream _out) {
		this._out = ZipUtil.newZipArchiver(_out);
	}

	@SneakyThrows
	public CSVPrinter createCsvPrinter()
	{
		this._csv = new StringBuilder();
		return new CSVPrinter(this._csv, CsvUtil._SCSV);
	}

	@SneakyThrows
	public void newSheet(String _name, List<String> _cols, List<String> _colNames, boolean _headerUppercase)
	{
		if(this._cp!=null)
		{
			this._cp.flush();
			_out.add(this._sheet+".csv", _csv.toString().getBytes(StandardCharsets.UTF_8));
		}

		this._sheet = _name;
		this._cp = this.createCsvPrinter();

		this._columns = new Vector<String>(_cols);
		if(_headerUppercase)
		{
			List _l = new Vector();
			for(String _h : _colNames)
			{
				_l.add(_h.toUpperCase());
			}
			this._cp.printRecord(_l);
		}
		else
		{
			this._cp.printRecord(_colNames);
		}
	}

	@SneakyThrows
	public void write(List _cols)
	{
		if(this._cp!=null)
		{
			this._cp.printRecord(_cols);
			this._cp.flush();
		}
	}

	@SneakyThrows
	public void write(Map _cols)
	{
		List _l = new Vector();
		for(String field : this._columns)
		{
			_l.add(_cols.get(field) == null ? "" : _cols.get(field).toString());
		}
		this.write(_l);
	}

	@SneakyThrows
	public void close()
	{
		if(this._cp!=null)
		{
			this._cp.flush();
			_out.add(this._sheet+".csv", _csv.toString().getBytes(StandardCharsets.UTF_8));
		}
		CommonUtil.close(this._cp);
		this._out.close();
	}

}
