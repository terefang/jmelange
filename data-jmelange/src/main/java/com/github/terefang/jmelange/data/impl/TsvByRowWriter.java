package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.ByRowDataWriter;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

public class TsvByRowWriter extends AbstractCsvByRowWriter implements AbstractDataExchange, ByRowDataWriter
{
	@SneakyThrows
	public static TsvByRowWriter from(File _out)
	{
		TsvByRowWriter _wr = new TsvByRowWriter();
		_wr.open(_out);
		return _wr;
	}

	@SneakyThrows
	public static TsvByRowWriter from(OutputStream _out)
	{
		TsvByRowWriter _wr = new TsvByRowWriter();
		_wr.open(_out);
		return _wr;
	}

	static String DATANAME = "tsv";
	static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("tsv"));
	static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".tsv"));

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
		return new CSVPrinter(this, CSVFormat.TDF);
	}
}
