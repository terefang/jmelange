package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.ByRowDataWriter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

public class XlsxSheetByRowWriter implements AbstractDataExchange, ByRowDataWriter
{
	@SneakyThrows
	public static XlsxSheetByRowWriter from(File _out)
	{
		XlsxSheetByRowWriter _wr = new XlsxSheetByRowWriter();
		_wr.open(_out);
		return _wr;
	}

	@SneakyThrows
	public static XlsxSheetByRowWriter from(OutputStream _out)
	{
		XlsxSheetByRowWriter _wr = new XlsxSheetByRowWriter();
		_wr.open(_out);
		return _wr;
	}

	static String DATANAME = "xlsx";
	static List<String> DATANAMES = Collections.unmodifiableList(Collections.singletonList("xlsx"));
	static List<String> DATAEXTS = Collections.unmodifiableList(Collections.singletonList(".xlsx"));

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
		this.open(new BufferedOutputStream(new FileOutputStream(_out), 1<<16));
	}

	@Override
	public void open(OutputStream _out) {
		this._out = _out;
	}

	private OutputStream _out;
	private SXSSFWorkbook _wb = new SXSSFWorkbook(100);
	private SXSSFSheet _sheet;
	private Vector<String> _columns;
	private int _rn;


	@Override
	public void newSheet(String _name, List<String> _cols, List<String> _colNames, boolean _headerUppercase)
	{
		this._sheet = this._wb.createSheet(_name);
		this._columns = new Vector<String>(_cols);

		this._rn = 0;
		int _cn = 0;
		Row _row = this._sheet.createRow(this._rn++);
		for(String field : _colNames)
		{
			Cell _cell = _row.createCell(_cn++);
			_cell.setCellValue(_headerUppercase ? field.toUpperCase() : field);
		}
	}

	@Override
	public void write(List _cols)
	{
		Row _row = this._sheet.createRow(this._rn++);
		int _cn = 0;
		for(Object field : _cols)
		{
			Cell _cell = _row.createCell(_cn++);
			_cell.setCellValue(field == null ? "" : field.toString());
		}
	}

	@Override
	public void write(Map _cols)
	{
		Row _row = this._sheet.createRow(this._rn++);
		int _cn = 0;
		for(String field : this._columns)
		{
			Cell _cell = _row.createCell(_cn++);
			_cell.setCellValue(_cols.get(field) == null ? "" : _cols.get(field).toString());
		}
	}

	public void writeAsText(Map _cols)
	{
		Row _row = this._sheet.createRow(this._rn++);
		int _cn = 0;
		for(String field : this._columns)
		{
			Cell _cell = _row.createCell(_cn++);
			_cell.setCellValue(_cols.get(field) == null ? "" : _cols.get(field).toString());
		}
	}

	@SneakyThrows
	public void close()
	{
		this._wb.write(this._out);
		this._wb.close();
		this._out.flush();
		IOUtil.close(this._out);
	}
}
