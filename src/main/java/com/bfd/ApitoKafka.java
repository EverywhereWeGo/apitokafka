package com.bfd;

import com.bfd.apitype.ApiType1;
import com.bfd.apitype.ApiType2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.bfd.tools.Kafkautils.createTopic;
import static com.bfd.tools.ReadExcel.readexcel;
import static com.bfd.tools.ReadExcel.stringtoInt;

/**
 * @author everywherewego
 * @date 2020-09-03 18:42
 */

public class ApitoKafka {
    private static Logger logger = LoggerFactory.getLogger(ApitoKafka.class);

    public static void run() {

        int sheet = 0;
        int startrow = 1;
        int endrow = 4;
        int startcol = stringtoInt("A");
        int endcol = stringtoInt("I");

        String[][] readexcel = new String[0][];
        try {
            readexcel = readexcel("/Users/everywherewego/IdeaProjects/apitokafka/src/main/resources/api接数.xlsx", sheet, startrow, endrow, startcol, endcol);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        //一个url创建一个对应类型的实例
        for (int i = 1; i <= endrow - startrow; i++) {
            createTopic(readexcel[i][8]);
            String urltype = readexcel[i][0];

            if ("1".equals(urltype)) {
                ApiType1 api = new ApiType1();
                api.run(readexcel[i]);
            } else if ("2".equals(urltype)) {
                ApiType2 api = new ApiType2();
                api.run(readexcel[i]);
            }
        }
    }


    public static void main(String[] args) {
        run();
    }
}



