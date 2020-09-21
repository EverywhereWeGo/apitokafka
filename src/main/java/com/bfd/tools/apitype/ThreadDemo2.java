package com.bfd.tools.apitype;

/**
 * @author everywherewego
 * @date 2020-09-21 23:20
 */

//该类型接口为3s更新一次,每次都是最新的数据
public class ThreadDemo2 extends Thread {
    private String httptype;
    private String url;

    public ThreadDemo2(String httptype, String url) {
        this.httptype = httptype;
        this.url = url;
    }

    @Override
    public void run() {
        System.out.println(httptype + ":" + url);
    }

}