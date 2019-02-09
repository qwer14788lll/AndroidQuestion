package com.example.surface_pro_5.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {

    private TextView mAnswerTextView;
    //private static final String EXTRA_ANSWER="answer";
    private static final String EXTRA_ANSWER_SHOWN="answer_shown";//返回代码

    /*public static Intent newIntent(Context packageContext, boolean answerIsTrue)//封装逻辑
    {
        Intent intent = new Intent(packageContext, AnswerActivity.class);
        intent.putExtra(EXTRA_ANSWER, answerIsTrue);
        return intent;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        mAnswerTextView=findViewById(R.id.Answer_text_View);
        Intent data=getIntent();//获取传过来的Intent对象
        String answer=data.getStringExtra("mes");//获取键为mes的值
        mAnswerTextView.setText(answer);//显示到组件
        data.putExtra(EXTRA_ANSWER_SHOWN, "您已经查看了答案");
        //data.putExtra("answer_shown", "您已经查看了答案");
        setResult(RESULT_OK, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//兼容性判断
            //Build.VERSION.SDK_INT常量代表了Android设备的版本号
        }
    }
}
