package com.example.surface_pro_5.myapplication;

public class Translation {//接收服务器返回数据的模型类
    private int status;//请求成功时为1
    private content content;//返回的内容信息（以结构体的形式保存数据）
    private static class content {
        private String from;//原文语种
        private String to;//译文语种
        private String vendor;//翻译平台
        private String out;//译文内容
        private int errNo;//错误信息，请求成功时为0
    }

    private String w;//原文内容
    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public void show() {//定义 输出返回数据 的方法
        System.out.println(status);
        System.out.println(content.from);
        System.out.println(content.to);
        System.out.println(content.vendor);
        System.out.println(content.out);
        System.out.println(content.errNo);
    }
}
