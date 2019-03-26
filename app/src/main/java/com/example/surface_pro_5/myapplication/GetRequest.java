package com.example.surface_pro_5.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_request);
        request();
    }

    private void request() {
        Retrofit retrofit = new Retrofit.Builder()//创建Retrofit(网络适配器)对象
                .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖implementation 'com.squareup.retrofit2:converter-gson:2.5.0')
                .build();//构建
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class); //用 网络适配器 创建 网络请求接口 的实例对象
        Call<Translation> call = request.getCall("test"); //对 发送请求 进行封装
        call.enqueue(new Callback<Translation>() {//发送网络请求(异步)
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                // 步骤7：处理返回的数据结果
                response.body().show();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Translation> call, Throwable throwable) {
                System.out.println(call.toString()+"连接失败");
            }
        });
    }
}
