package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.ByRowDataWriter;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;
import com.github.terefang.jmelange.apache.csv.CSVPrinter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

public class CsvByRowWriter extends AbstractCsvByRowWriter implements AbstractDataExchange, ByRowDataWriter
{
	@SneakyThrows
	public static CsvByRowWriter from(File _out, Charset _cs)
	{
		CsvByRowWriter _wr = new CsvByRowWriter();
		_wr.open(new FileWriter(_out, _cs));
		return _wr;
	}

	@SneakyThrows
	public static CsvByRowWriter from(File _out)
	{
		CsvByRowWriter _wr = new CsvByRowWriter();
		_wr.open(_out);
		return _wr;
	}

	@SneakyThrows
	public static CsvByRowWriter from(OutputStream _out)
	{
		CsvByRowWriter _wr = new CsvByRowWriter();
		_wr.open(_out);
		return _wr;
	}

	static String DATANAME = "csv";
	static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("csv"));
	static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".csv"));

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


	@Override
	@SneakyThrows
	public void open(File _out) {
		this.open(new FileOutputStream(_out));
	}

	@Override
	@SneakyThrows
	public CSVPrinter createCsvPrinter() {
		return new CSVPrinter(this, CsvUtil._SCSV);
	}
}
