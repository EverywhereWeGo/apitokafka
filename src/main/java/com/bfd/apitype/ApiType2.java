package com.bfd.apitype;

/**
 * @author everywherewego
 * @date 2020-09-21 23:20
 */

//该类型接口为3s更新一次,每次都是最新的数据
public class ApiType2 {
    private String httptype;
    private String url;

    public ApiType2(String httptype, String url) {
        this.httptype = httptype;
        this.url = url;
    }

    public void run() {
        System.out.println(httptype + ":" + url);
    }

}