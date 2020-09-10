package com.bfd;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

import static com.bfd.tools.ReadExcel.readexcel;
import static com.bfd.tools.ReadExcel.stringtoInt;

/**
 * @author everywherewego
 * @date 2020-09-03 18:42
 */

public class apitokafka {
    public static void main(String[] args) throws IOException, InvalidFormatException {

        int startrow = 1;
        int endrow = 2;
        int startcol = stringtoInt("A");
        int endcol = stringtoInt("D");

        String[][] readexcel = readexcel("/Users/everywherewego/Desktop/api接数.xlsx", 0, startrow, endrow, startcol, endcol);
        for (int i = 1; i <= endrow - startrow; i++) {
            if ("get".equals(readexcel[i][1])) {
                System.out.println("发送get请求");
            } else if ("post".equals(readexcel[i][1])) {
                System.out.println("发送post请求");

            }
            System.out.println();
        }
    }
}
