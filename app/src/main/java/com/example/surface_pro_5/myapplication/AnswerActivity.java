package com.example.surface_pro_5.myapplication;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

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
    private ImageView mImageView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//去掉标题栏
        setContentView(R.layout.activity_answer);

        mAnswerTextView=findViewById(R.id.Answer_text_View);
        Intent data=getIntent();//获取传过来的Intent对象
        String answer=data.getStringExtra("mes");//获取键为mes的值
        mAnswerTextView.setText(answer);//显示到组件
        data.putExtra(EXTRA_ANSWER_SHOWN, R.string.Toast_Answer);
        //data.putExtra("answer_shown", "您已经查看了答案");
        setResult(RESULT_OK, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//兼容性判断
            //Build.VERSION.SDK_INT常量代表了Android设备的版本号
        }

        mImageView = findViewById(R.id.image_view);
        mButton=findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage(v);
            }
        });
    }
    public void loadImage(View view) {
        String url = "http://www.pptok.com/wp-content/uploads/2012/08/xunguang-4.jpg";
        Glide.with(this).load(url).into(mImageView);
    }
}
