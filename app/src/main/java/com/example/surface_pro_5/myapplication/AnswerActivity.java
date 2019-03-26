package com.example.surface_pro_5.myapplication;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.Target;
import java.util.Objects;


public class AnswerActivity extends AppCompatActivity {

    private TextView mAnswerTextView;
    //private static final String EXTRA_ANSWER="answer";
    private static final String EXTRA_ANSWER_SHOWN = "answer_shown";//返回键

    /*public static Intent newIntent(Context packageContext, boolean answerIsTrue)//封装逻辑
    {
        Intent intent = new Intent(packageContext, AnswerActivity.class);
        intent.putExtra(EXTRA_ANSWER, answerIsTrue);
        return intent;
    }*/
    private Button mButton;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//去掉标题栏
        setContentView(R.layout.activity_answer);

        mAnswerTextView = findViewById(R.id.Answer_text_View);
        Intent data = getIntent();//获取传过来的Intent对象
        String answer = data.getStringExtra("msg");//获取键为msg的值
        mAnswerTextView.setText(answer);//显示到组件
        //data.putExtra(EXTRA_ANSWER_SHOWN, R.string.Toast_Answer);
        data.putExtra(EXTRA_ANSWER_SHOWN, "您已经查看了答案");
        setResult(RESULT_OK, data);//返回代码和数据

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//兼容性判断
            //Build.VERSION.SDK_INT常量代表了Android设备的版本号
        }

        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从Android 6.0开始，危险权限需要动态申请
                if (ContextCompat.checkSelfPermission(AnswerActivity.this, Manifest.permission.CALL_PHONE) != 0)//检查是否已经获得授权（PackageManager.PERMISSION_GRANTED代表0）
                {
                    ActivityCompat.requestPermissions(AnswerActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);//动态申请权限，请求代码1
                } else {
                    //Intent intent = new Intent(Intent.ACTION_CALL);//打开电话界面（自动拨打）
                    //Intent intent = new Intent(Intent.ACTION_DIAL);//打开电话界面（手动拨打）
                    //intent.setData(Uri.parse("tel:17520439994"));//使用setData方式补充号码
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:17520439994"));//发送短信
                    intent.putExtra("sms_body", "调用putExtra方法可以用键值对的形式添加短信内容");//填写短信内容
                    //Intent intent = new Intent(Intent.ACTION_VIEW);//设置活动类型
                    //intent.setData(Uri.parse("https://www.baidu.com/s?wd="+"Android"));//设置数据
                    startActivity(intent);//转达意图
                }
            }
        });

        mImageView=findViewById(R.id.image_View);
        //String url="https://images0.cnblogs.com/blog2015/455586/201503/132001056995589.gif";
//        int url=R.drawable.ic_logo_chrome;
//        Glide.with(this)
//                .load(url)
//                .override(100, 100)
//                //.circleCrop()
//                //.dontTransform()//禁用图片变换
//                .into(mImageView);
        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.animator_alpha);//调用XML动画设置
        anim.setTarget(mImageView);//设置到图片组件上
        anim.start();//启动动画

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //TODO 动画开始前的操作
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //TODO 动画结束的操作
                ValueAnimator moneyAnimator =ValueAnimator.ofFloat(0f, 126512.36f);//ofFloat浮点数估值器方法（初始值，最终值）备注：f代表浮点型
                moneyAnimator.setInterpolator(new LinearInterpolator());//插值器
                moneyAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//moneyAnimator动画执行时发生
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float money= (float) animation.getAnimatedValue();//获取动画数值（因为估值器会帮我们计算浮点数值）
                        mAnswerTextView.setText(String.format("%.2f元", money));//格式化字符串，表示小数点后保留2位，显示到文本组件中
                    }
                });
                //moneyAnimator.start();
                int startColor = Color.parseColor("#FFDEAD");//起始颜色
                int endColor = Color.parseColor("#FF4500");//结束颜色
                //ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),startColor, endColor);//不用ofArgb方法的写法，手动调用ArgbEvaluator颜色估值器
                ValueAnimator colorAnimator = ValueAnimator.ofArgb(startColor, endColor);//ofArgb渐变色估值器方法（起始颜色,结束颜色）
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//colorAnimator动画执行时发生
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int color = (int) animation.getAnimatedValue();//获取颜色数值（因为估值器会帮我们计算颜色数值）
                        mAnswerTextView.setTextColor(color);
                    }
                });
                AnimatorSet Set = new AnimatorSet();//创建组合动画对象
                Set.playTogether(moneyAnimator,colorAnimator);//将两个动画组合
                Set.setDuration(3000);//动画持续时间
                Set.start();//启动动画
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //TODO 动画取消的操作
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //TODO 动画重复的操作
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        mImageView.setImageResource(R.drawable.animation_frame);//将 帧文件 设置到控件的 图片资源 中
//        AnimationDrawable animationDrawable = (AnimationDrawable) mImageView.getDrawable();//将图片资源传给动画对象
//        animationDrawable.start();//启动动画
    }
}
