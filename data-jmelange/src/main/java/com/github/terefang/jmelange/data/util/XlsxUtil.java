package com.github.terefang.jmelange.data.util;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class XlsxUtil
{
    public static void toXlsx(OutputStream out, List<Map<String,Object>> resultSet) throws Exception
    {
        List<String> columns = new Vector<>();
        resultSet.get(0).keySet().forEach(x -> columns.add(x));
        toXlsx(out, resultSet, columns);
    }

    public static void toXlsx(OutputStream out, List<Map<String,Object>> resultSet, List<String> columns) throws Exception
    {
        toXlsx(out, resultSet, columns, "DEFAULT", true);
    }

    public static void toXlsx(OutputStream out, List<Map<String,Object>> resultSet, List<String> columns, String sheetName) throws Exception
    {
        toXlsx(out, resultSet, columns, sheetName, true);
    }

    public static void toXlsx(OutputStream out, List<Map<String,Object>> resultSet, List<String> columns, String sheetName, boolean headerUpperCase) throws Exception
    {
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        try
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
            wb.write(out);
            out.flush();
        }
        finally
        {
            wb.dispose();
        }
    }

    @SneakyThrows
    public static List<Map<String, Object>> fromXlsx(File _file)
    {
        try (FileInputStream _fh = new FileInputStream(_file))
        {
            return fromXlsx(_fh);
        }
    }

    @SneakyThrows
    public static List<Map<String, Object>> fromXlsx(InputStream _file)
    {
        XSSFWorkbook _workbook = new XSSFWorkbook(_file);
        Sheet _sheet = _workbook.getSheetAt(0);

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

            Map<String, Object> _data = new HashMap<>();
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
