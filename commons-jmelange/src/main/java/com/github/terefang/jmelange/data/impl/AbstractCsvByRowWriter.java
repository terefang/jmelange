package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.SneakyThrows;
import com.github.terefang.jmelange.apache.csv.CSVPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class AbstractCsvByRowWriter implements Appendable
{

	private Writer _out;
	private Vector<String> _columns;
	private CSVPrinter _cp;
	private boolean _isFirst;

	public Appendable append(CharSequence csq) throws IOException {
		this._out.write(csq.toString());
		return this;
	}

	public Appendable append(CharSequence csq, int start, int end) throws IOException {
		this._out.write(csq.subSequence(start, end).toString());
		return this;
	}

	public Appendable append(char c) throws IOException {
		this._out.write(c);
		return this;
	}

	@SneakyThrows
	public void open(OutputStream _out) {
		this._out = new OutputStreamWriter(_out, StandardCharsets.UTF_8);
		this._cp = this.createCsvPrinter();
		this._isFirst = true;
	}

	@SneakyThrows
	public void open(Writer _out) {
		this._out = _out;
		this._cp = this.createCsvPrinter();
		this._isFirst = true;
	}

	public abstract CSVPrinter createCsvPrinter();

	@SneakyThrows
	public void newSheet(String _name, List<String> _cols, List<String> _colNames, boolean _headerUppercase)
	{
		if(this._isFirst)
		{
			this._isFirst = false;
		}
		else
		{
			this._cp.println();
			this._cp.println();
			this._cp.printRecord(new Object[] { _name });
			this._cp.println();
		}

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
		this._out.flush();
	}

	@SneakyThrows
	public void write(List _cols)
	{
		this._cp.printRecord(_cols);
		this._out.flush();
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
		this._out.flush();
		CommonUtil.close(this._cp);
		CommonUtil.close(this._out);
	}

}
