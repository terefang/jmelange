package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.zip.ByFileArchiver;
import com.github.terefang.jmelange.commons.zip.CompressionEnum;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;

public class ZipUtil {
	@SneakyThrows
	public static ByFileArchiver newZipArchiver(File zipFile) throws IOException
	{
		return ByFileArchiver.createZip(zipFile);
	}

	@SneakyThrows
	public static ByFileArchiver newZipArchiver(String zipFile) throws IOException
	{
		return ByFileArchiver.createZip(new File(zipFile));
	}

	@SneakyThrows
	public static ByFileArchiver newZipArchiver(OutputStream _out) throws IOException
	{
		return ByFileArchiver.createZip(_out);
	}

	@SneakyThrows
	public static ByFileArchiver newArchiver(File zipFile) throws IOException
	{
		return ByFileArchiver.createArchive(zipFile);
	}

	@SneakyThrows
	public static ByFileArchiver newArchiver(String zipFile) throws IOException
	{
		return ByFileArchiver.createArchive(new File(zipFile));
	}

	@SneakyThrows
	public static ByFileArchiver newArchiver(OutputStream _out, CompressionEnum _ctype) throws IOException
	{
		return ByFileArchiver.createArchive(_out, _ctype);
	}
	
	@SneakyThrows
	public static void fileToZip(String zipFile, String file) throws IOException
	{
		fileToZip(new File(zipFile), new File(file));
	}
	
	@SneakyThrows
	public static void fileToZip(File zipFile, File file) throws IOException
	{
		filesToZip(zipFile, Collections.singletonList(file));
	}
	
	@SneakyThrows
	public static void filesToZip(String zipFile, String[] files) throws IOException
	{
		filesToZip(zipFile, Arrays.asList(files));
	}
	
	@SneakyThrows
	public static void filesToZip(String zipFile, List<String> files) throws IOException
	{
		File _zf = new File(zipFile);
		List<File> _fl = new Vector<>();
		for(String _f : files)
		{
			_fl.add(new File(_f));
		}
		filesToZip(_zf, _fl);
	}

	@SneakyThrows
	public static void filesToZip(File zipFile, File[] files) throws IOException
	{
		filesToZip(zipFile, Arrays.asList(files), false);
	}

	@SneakyThrows
	public static void filesToZip(File zipFile, File[] files, boolean _moveInto) throws IOException
	{
		filesToZip(zipFile, Arrays.asList(files), _moveInto);
	}

	@SneakyThrows
	public static void filesToZip(File zipFile, List<File> files) throws IOException
	{
		filesToZip(zipFile, files, false);
	}

	@SneakyThrows
	public static void filesToZip(File zipFile, List<File> files, boolean _moveInto) throws IOException
	{
		ByFileArchiver _arc = ByFileArchiver.createZip(zipFile);
		_arc.addFiles(files);
		_arc.close();

		if(_moveInto)
		{
			for (File _f : files)
			{
				_f.delete();
			}
		}
	}

	@SneakyThrows
	public static void filesToZip(String zipFile, Map<String, byte[]> files) throws IOException
	{
		filesToZip(new File(zipFile), files);
	}

	@SneakyThrows
	public static void filesToZip(File zipFile, Map<String, byte[]> files) throws IOException
	{
		ByFileArchiver _arc = ByFileArchiver.createZip(zipFile);
		_arc.add(files);
		_arc.close();
	}
	
	@SneakyThrows
	public static void fileToArchive(String zipFile, String file) throws IOException
	{
		fileToArchive(new File(zipFile), new File(file));
	}
	
	@SneakyThrows
	public static void fileToArchive(File zipFile, File file) throws IOException
	{
		filesToArchive(zipFile, Collections.singletonList(file));
	}
	

	@SneakyThrows
	public static void filesToArchive(File zipFile, List<File> files, boolean _moveInto) throws IOException
	{
		ByFileArchiver _arc = ByFileArchiver.createArchive(zipFile);
		_arc.addFiles(files);
		_arc.close();

		if(_moveInto)
		{
			for (File _f : files)
			{
				_f.delete();
			}
		}
	}

	@SneakyThrows
	public static void filesToArchive(String zipFile, Map<String, byte[]> files) throws IOException
	{
		filesToArchive(new File(zipFile), files);
	}

	@SneakyThrows
	public static void filesToArchive(File zipFile, Map<String, byte[]> files) throws IOException
	{
		ByFileArchiver _arc = ByFileArchiver.createArchive(zipFile);
		_arc.add(files);
		_arc.close();
	}

	@SneakyThrows
	public static void filesToArchive(String zipFile, String[] files) throws IOException
	{
		filesToArchive(zipFile, Arrays.asList(files));
	}

	@SneakyThrows
	public static void filesToArchive(String zipFile, List<String> files) throws IOException
	{
		File _zf = new File(zipFile);
		List<File> _fl = new Vector<>();
		for(String _f : files)
		{
			_fl.add(new File(_f));
		}
		filesToArchive(_zf, _fl);
	}

	@SneakyThrows
	public static void filesToArchive(File zipFile, File[] files) throws IOException
	{
		filesToArchive(zipFile, Arrays.asList(files), false);
	}

	@SneakyThrows
	public static void filesToArchive(File zipFile, File[] files, boolean _moveInto) throws IOException
	{
		filesToArchive(zipFile, Arrays.asList(files), _moveInto);
	}

	@SneakyThrows
	public static void filesToArchive(File zipFile, List<File> files) throws IOException
	{
		filesToArchive(zipFile, files, false);
	}

}
