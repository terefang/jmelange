package com.github.terefang.jmelange.data.util;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Vector;
import java.util.Collections;

public class XlsxUtil
{
    @SneakyThrows
    public static void toXlsx(File fout, List<Map<String,Object>> resultSet)
    {
        toXlsx(new BufferedOutputStream(new FileOutputStream(fout), 81920), resultSet);
    }

    @SneakyThrows
    public static void toXlsx(OutputStream out, List<Map<String,Object>> resultSet)
    {
        List<String> columns = new Vector<>();
        resultSet.get(0).keySet().forEach(x -> columns.add(x));
        toXlsx(out, resultSet, columns);
    }

    @SneakyThrows
    public static void toXlsx(File fout, List<Map<String,Object>> resultSet, List<String> columns)
    {
        toXlsx(new BufferedOutputStream(new FileOutputStream(fout), 81920), resultSet, columns);
    }

    @SneakyThrows
    public static void toXlsx(OutputStream out, List<Map<String,Object>> resultSet, List<String> columns)
    {
        toXlsx(out, resultSet, columns, "DEFAULT", true);
    }

    @SneakyThrows
    public static void toXlsx(File fout, List<Map<String,Object>> resultSet, List<String> columns, String sheetName)
    {
        toXlsx(new BufferedOutputStream(new FileOutputStream(fout), 81920), resultSet, columns, sheetName);
    }

    @SneakyThrows
    public static void toXlsx(OutputStream out, List<Map<String,Object>> resultSet, List<String> columns, String sheetName)
    {
        toXlsx(out, resultSet, columns, sheetName, true);
    }

    @SneakyThrows
    public static void toXlsx(File fout, Map<String,List<Map<String, Object>>> resultSet)
    {
        toXlsx(new BufferedOutputStream(new FileOutputStream(fout), 81920), resultSet);
    }

    @SneakyThrows
    public static void toXlsx(OutputStream out, Map<String,List<Map<String, Object>>> resultSet)
    {
        toXlsx(out, resultSet, true);
    }

    @SneakyThrows
    public static void toXlsx(File fout, Map<String,List<Map<String, Object>>> resultSet, boolean headerUpperCase)
    {
        toXlsx(new BufferedOutputStream(new FileOutputStream(fout), 81920), resultSet,headerUpperCase);
    }

    @SneakyThrows
    public static void toXlsx(OutputStream out, Map<String,List<Map<String, Object>>> resultSet, boolean headerUpperCase)
    {
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        try
        {
            for(Map.Entry<String, List<Map<String, Object>>> _entry : resultSet.entrySet())
            {
                List<String> columns = new Vector(_entry.getValue().get(0).keySet());
                toXlsx(wb, _entry.getValue(), columns, _entry.getKey(), headerUpperCase);
            }
            wb.write(out);
            out.flush();
        }
        finally
        {
            wb.dispose();
        }
    }

    @SneakyThrows
    public static void toXlsx(File fout, List<Map<String,Object>> resultSet, List<String> columns, String sheetName, boolean headerUpperCase)
    {
        toXlsx(new BufferedOutputStream(new FileOutputStream(fout), 81920), resultSet, columns, sheetName, headerUpperCase);
    }

    @SneakyThrows
    public static void toXlsx(OutputStream out, List<Map<String,Object>> resultSet, List<String> columns, String sheetName, boolean headerUpperCase)
    {
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        try
        {
            toXlsx(wb,resultSet, columns, sheetName, headerUpperCase);
            wb.write(out);
            out.flush();
        }
        finally
        {
            wb.dispose();
        }
    }
    public static void toXlsx(SXSSFWorkbook wb, List<Map<String,Object>> resultSet, List<String> columns, String sheetName, boolean headerUpperCase) throws Exception
    {
        Sheet sheet = wb.createSheet(sheetName);
        int rn = 0;
        int cn = 0;
        Row row = sheet.createRow(rn++);

        for(String field : columns)
        {
            Cell cell = row.createCell(cn++);
            cell.setCellValue(headerUpperCase ? field.toUpperCase() : field);
        }

        for(Map<String,Object> rowf : resultSet)
        {
            row = sheet.createRow(rn++);
            cn=0;
            for(String field : columns)
            {
                Cell cell = row.createCell(cn++);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(rowf.get(field) == null ? "" : rowf.get(field).toString());
            }
        }
    }

    @SneakyThrows
    public static List<Map<String, Object>> fromXlsx(File _file)
    {
        try (InputStream _fh = new BufferedInputStream(new FileInputStream(_file),81920))
        {
            return fromXlsx(_fh);
        }
    }

    @SneakyThrows
    public static Map<String,List<Map<String, Object>>> fromXlsxSheets(File _file)
    {
        try (InputStream _fh = new BufferedInputStream(new FileInputStream(_file),81920))
        {
            return fromXlsxSheets(_fh);
        }
    }

    @SneakyThrows
    public static Map<String, List<Map<String, Object>>> fromXlsxSheets(InputStream _fh)
    {
        XSSFWorkbook _workbook = new XSSFWorkbook(_fh);
        return fromXlsxSheets(_workbook);
    }

    @SneakyThrows
    public static List<Map<String, Object>> fromXlsx(InputStream _file)
    {
        XSSFWorkbook _workbook = new XSSFWorkbook(_file);
        return fromXlsxSheet(_workbook, 0);
    }

    @SneakyThrows
    public static Map<String,List<Map<String, Object>>> fromXlsxSheets(XSSFWorkbook _workbook)
    {
        Map<String,List<Map<String, Object>>> _ret = new LinkedHashMap<>();
        int _len = _workbook.getNumberOfSheets();
        for(int _i = 0; _i<_len; _i++)
        {
            String _name = _workbook.getSheetName(_i).toLowerCase();
            _ret.put(_name, fromXlsxSheet(_workbook, _i));
        }
        return _ret;
    }

    @SneakyThrows
    public static List<Map<String, Object>> fromXlsxSheet(XSSFWorkbook _workbook, String _index)
    {
        int _len = _workbook.getNumberOfSheets();
        for(int _i = 0; _i<_len; _i++)
        {
            if(!_index.equalsIgnoreCase(_workbook.getSheetName(_i))) continue;
            return fromXlsxSheet(_workbook, _i);
        }
        return Collections.emptyList();
    }

    public static List<Map<String, Object>> fromXlsxSheet(XSSFWorkbook _workbook, int _index)
    {
        Sheet _sheet = _workbook.getSheetAt(_index);

        List<Map<String, Object>> _ret = new Vector<>();
        boolean _first = true;
        List<String> _header = new Vector<>();
        for (Row _row : _sheet)
        {
            if(_first)
            {
                for (Cell _cell : _row) {
                    _header.add(_cell.getStringCellValue());
                }
                _first = false;
                continue;
            }

            Map<String, Object> _data = new LinkedHashMap<>();
            _ret.add(_data);

            for(int _i=0; _i< _header.size(); _i++)
            {
                Cell _cell = _row.getCell(_i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                switch (_cell.getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC: {
                        if (DateUtil.isCellDateFormatted(_cell)) {
                            _data.put(_header.get(_i), _cell.getDateCellValue());
                        } else {
                            _data.put(_header.get(_i), _cell.getNumericCellValue());
                        }
                        break;
                    }
                    case Cell.CELL_TYPE_BOOLEAN: {
                        _data.put(_header.get(_i), _cell.getBooleanCellValue());
                        break;
                    }
                    case Cell.CELL_TYPE_STRING:
                    default: {
                        _data.put(_header.get(_i), _cell.getStringCellValue());
                    }
                }
            }
        }
        return _ret;
    }
}
