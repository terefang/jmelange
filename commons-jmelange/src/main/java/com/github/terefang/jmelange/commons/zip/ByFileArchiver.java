package com.github.terefang.jmelange.commons.zip;

import com.github.terefang.jmelange.commons.util.IOUtil;
import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ByFileArchiver {
	private OutputStream _bos;
	private ArchiveOutputStream _out;
	private ArchiveEnum _type;

	@SneakyThrows
	public static ByFileArchiver createArchive(File _file)
	{
		ByFileArchiver _arc = new ByFileArchiver();

		_arc._bos = new BufferedOutputStream(new FileOutputStream(_file), 1 << 10);
		_arc._type = ArchiveEnum.tar;
		_arc._out = ArchiveEnum.createStream(_arc._type, CompressionEnum.createStream(_file.getName(), _arc._bos));
		return _arc;
	}

	@SneakyThrows
	public static ByFileArchiver createArchive(OutputStream _bos, CompressionEnum _ctype)
	{
		ByFileArchiver _arc = new ByFileArchiver();

		_arc._bos = _bos;
		_arc._type = ArchiveEnum.tar;
		_arc._out = ArchiveEnum.createStream(_arc._type, CompressionEnum.createStream(_ctype, _arc._bos));
		return _arc;
	}

	@SneakyThrows
	public static ByFileArchiver createZip(File _file)
	{
		ByFileArchiver _arc = new ByFileArchiver();

		_arc._bos = new BufferedOutputStream(new FileOutputStream(_file), 1 << 10);
		_arc._type = ArchiveEnum.zip;
		_arc._out = ArchiveEnum.createStream(_arc._type, _arc._bos);
		return _arc;
	}

	@SneakyThrows
	public static ByFileArchiver createZip(OutputStream _bos)
	{
		ByFileArchiver _arc = new ByFileArchiver();

		_arc._bos = _bos;
		_arc._type = ArchiveEnum.zip;
		_arc._out = ArchiveEnum.createStream(_arc._type, _arc._bos);
		return _arc;
	}

	@SneakyThrows
	public void add(String _name, File _f)
	{
		InputStream _in = new FileInputStream(_f);
		this._out.putArchiveEntry(ArchiveEnum.createEntry(this._type, _name, false, false, _f.length(), 1000, 1000, 0600, 0));
		byte[] _buf = IOUtil.toByteArray(_in);
		this._out.write(_buf);
		this._out.closeArchiveEntry();
		this._out.flush();
		IOUtil.close(_in);
	}

	@SneakyThrows
	public void add(String _name, InputStream _in)
	{
		byte[] _buf = IOUtil.toByteArray(_in);
		this._out.putArchiveEntry(ArchiveEnum.createEntry(this._type, _name, false, false, _buf.length, 1000, 1000, 0600, 0));
		this._out.write(_buf);
		this._out.closeArchiveEntry();
		this._out.flush();
		IOUtil.close(_in);
	}

	@SneakyThrows
	public void add(String _name, byte[] _buf)
	{
		this._out.putArchiveEntry(ArchiveEnum.createEntry(this._type, _name, false, false, _buf.length, 1000, 1000, 0600, 0));
		this._out.write(_buf);
		this._out.closeArchiveEntry();
		this._out.flush();
	}

	@SneakyThrows
	public void add(Map<String,byte[]> _list)
	{
		for(Map.Entry<String, byte[]> _entry : _list.entrySet())
		{
			this.add(_entry.getKey(), _entry.getValue());
		}
	}

	@SneakyThrows
	public void add(List<File> _list)
	{
		for(File _entry : _list)
		{
			this.add(_entry);
		}
	}

	@SneakyThrows
	public void add(File[] _list)
	{
		for(File _entry : _list)
		{
			this.add(_entry);
		}
	}

	public void add(File _f)
	{
		this.add(_f.getName(), _f);
	}

	@SneakyThrows
	public void close()
	{
		this._out.flush();
		this._bos.flush();
		IOUtil.close(this._out);
		IOUtil.close(this._bos);
	}
}
