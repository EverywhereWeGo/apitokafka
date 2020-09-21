package com.bfd;

import com.bfd.apitype.ApiType1;
import com.bfd.apitype.ApiType2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.bfd.tools.ReadExcel.readexcel;
import static com.bfd.tools.ReadExcel.stringtoInt;

/**
 * @author everywherewego
 * @date 2020-09-03 18:42
 */

public class apitokafka {
    private static Logger logger = LoggerFactory.getLogger(apitokafka.class);

    public static void run() {

        int startrow = 1;
        int endrow = 3;
        int startcol = stringtoInt("A");
        int endcol = stringtoInt("G");

        String[][] readexcel = new String[0][];
        try {
            readexcel = readexcel("/Users/everywherewego/Desktop/api接数.xlsx", 0, startrow, endrow, startcol, endcol);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        String urltype;
        String url;
        String httptype;
        String time;
        String json;

        //一个url创建一个对应类型的实例
        for (int i = 1; i <= endrow - startrow; i++) {
            urltype = readexcel[i][0];
            url = readexcel[i][1];
            httptype = readexcel[i][2];

            logger.info("urltype:" + urltype);
            if ("1".equals(urltype)) {
                ApiType1 api = new ApiType1(httptype, url);
                api.run();
            } else if ("2".equals(urltype)) {
                ApiType2 api = new ApiType2(httptype, url);
                api.run();
            }
        }
    }


    public static void main(String[] args) {
        run();
    }
}



