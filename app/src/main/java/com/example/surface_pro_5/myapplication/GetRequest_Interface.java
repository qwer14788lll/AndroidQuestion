package com.example.surface_pro_5.myapplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetRequest_Interface {//用于描述网络请求的接口
    //采用 注解 描述 网络请求参数
    //@GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")//从指定的Url请求数据
    @POST("ajax.php?a=fy&f=auto&t=auto&w=")
    Call<Translation> getCall();
    //Call<Translation> getCall(@Field("i") String targetSentence);
    // 注解里传入 网络请求 的部分URL地址
    // 网络适配器 把网络请求的URL分成了两部分：一部分放在 网络适配器 对象里，另一部分放在网络请求接口里
    // 如果接口里的url是一个完整的网址，那么放在 网络适配器 对象里的URL可以忽略
    // getCall()是网络请求数据的方法
    // 需要配合@Field 向服务器提交需要的字段
}
