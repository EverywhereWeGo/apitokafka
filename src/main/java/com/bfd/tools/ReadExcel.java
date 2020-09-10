package com.bfd.tools;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ReadExcel {
    public static void main(String[] args) throws IOException, InvalidFormatException {

        int startrow = 4;
        int endrow = 330;
        int startcol = stringtoInt("A");
        int endcol = stringtoInt("J");

        String[][] readexcel = readexcel("/Users/everywherewego/Desktop/副本GPS对应表.xlsx", 1, startrow, endrow, startcol, endcol);
        for (int i = 0; i <= endrow - startrow; i++) {
            for (int j = 0; j <= endcol - startcol; j++) {
                System.out.print(readexcel[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static List<String> readexcel(String path, int sheetPage) throws IOException, InvalidFormatException {
        List<String> list = new LinkedList<>();
        Workbook wb = readFile(path);
        //读取sheet页
        Sheet sheet = wb.getSheetAt(sheetPage);
        //起始行
        int firstRowIndex = sheet.getFirstRowNum() + 1;
        //结束行
        int lastRowIndex = sheet.getLastRowNum();

        //遍历行
        for (int i = firstRowIndex; i <= lastRowIndex; i++) {
            Row row = sheet.getRow(i);
            list.add(row.getCell(1).toString());
        }
        return list;
    }

    public static String[][] readexcel(String path, int sheetPage, int startrow, int endrow, int startcol, int endcol) throws IOException, InvalidFormatException {
        String[][] exceldata = new String[endrow - startrow + 1][endcol - startcol + 1];
        List<String> list = new LinkedList<>();
        Workbook wb = readFile(path);
        //读取sheet页
        Sheet sheet = wb.getSheetAt(sheetPage);
        //遍历行
        for (int i = startrow - 1, line = 0; i <= endrow - 1; i++, line++) {
            Row row = sheet.getRow(i);

            int col = 0;
            for (int j = startcol - 1; j <= endcol - 1; j++) {
                Cell cell = row.getCell(j);
                String cellstring;
                if (null != cell && "NUMERIC".equals(cell.getCellType().toString())) {
                    DecimalFormat df = new DecimalFormat("0");
                    cellstring = df.format(cell.getNumericCellValue()).trim();
                } else {
                    cellstring = row.getCell(j) == null ? "null" : row.getCell(j).toString().trim();
                }
                exceldata[line][col++] = cellstring;
            }

        }
        return exceldata;
    }


    public static Map<String, String> readexcel2(String path, int sheetPage) throws IOException, InvalidFormatException {
        Map<String, String> map = new HashMap<>();
        Workbook wb = readFile(path);
        //读取sheet页
        Sheet sheet = wb.getSheetAt(sheetPage);
        //起始行
        int firstRowIndex = sheet.getFirstRowNum() + 1;
        //结束行
        int lastRowIndex = sheet.getLastRowNum();

        //遍历行
        for (int i = firstRowIndex; i <= lastRowIndex; i++) {
            Row row = sheet.getRow(i);
            map.put(row.getCell(1).toString(), row.getCell(3).toString());
        }
        return map;
    }


    public static Workbook readFile(String excelPath) throws IOException, InvalidFormatException {
        File excel = new File(excelPath);
        //.是特殊字符，需要转义！！！！！
        String[] split = excel.getName().split("\\.");
        Workbook wb;
        //根据文件后缀（xls/xlsx）进行判断
        if ("xls".equals(split[1])) {
            //文件流对象
            FileInputStream fis = new FileInputStream(excel);
            wb = new HSSFWorkbook(fis);
            fis.close();
        } else if ("xlsx".equals(split[1])) {
            wb = new XSSFWorkbook(excel);
        } else {
            System.out.println("文件类型错误!");
            return null;
        }
        wb.close();
        return wb;
    }

    //把字母排序转换成数字
    public static int stringtoInt(String string) {
        int result = 0;
        string = string.toUpperCase();
        byte[] bytes = string.getBytes();

        for (int i = bytes.length - 1, j = 0; i >= 0; i--, j++) {
            double pow = Math.pow(26, i);
            int aByte = bytes[j] - 'A' + 1;
            double v = pow * aByte;
            result += v;
        }
        return result;

    }
}
