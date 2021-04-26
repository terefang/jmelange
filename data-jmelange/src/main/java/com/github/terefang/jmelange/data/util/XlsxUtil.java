package com.github.terefang.jmelange.data.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;
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
            CreationHelper createHelper = wb.getCreationHelper();

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
}
