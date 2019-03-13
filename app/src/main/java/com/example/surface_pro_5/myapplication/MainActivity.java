package com.example.surface_pro_5.myapplication;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mButtonTrue;//组件成员变量m开头，Android编程命名约定
    private Button mButtonFalse;
    private TextView mQuestionTextView;//显示题目
    private Button mButtonNext;//下一题按钮
    private Button mButtonAnswer;//查看答案按钮
    private int mQuestionsIndex = 0;//题目索引
    private Question[] mQuestions = new Question[]{//以数组方式存储和调用题目数据
            new Question(R.string.Q1, false),
            new Question(R.string.Q2, true),
            new Question(R.string.Q3, true),
            new Question(R.string.Q4, true),
            new Question(R.string.Q5, true),
            new Question(R.string.Q6, true),
            new Question(R.string.Q7, true),
            new Question(R.string.Q8, true),
    };
    //private int sum=0;//未来用作计分
    private static final String TAG = "MainActivity";//日志来源
    private static final String KEY_INDEX = "index";//用于传递Bundle的键
    private static final int REQUEST_CODE_ANSWER = 10;//请求代码（表示发给AnswerActivity的）
    private TranslateAnimation mTranslateAnimation;//平移
    private AlphaAnimation mAlphaAnimation;//透明度
    String channelId = "chat";//测试阶段
    String channelName = "聊天消息";//测试阶段
    int importance = NotificationManager.IMPORTANCE_HIGH;//测试阶段

    @Override
    protected void onSaveInstanceState(Bundle outState) {//重写保存状态方法
        super.onSaveInstanceState(outState);
        Log.d(TAG, "outState保存状态");
        outState.putInt(KEY_INDEX, mQuestionsIndex);//保存当前题目的索引，以键值对的形式保存
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()初始化界面");//三个参数：日志来源,日志内容，异常信息（可选参数）
        /*Log类记录日志信息的方法所对应的日志级别
         * Error（错误）Log.e(……)
         * Warning（警告）Log.w(……)
         * Info（信息）Log.i(……)
         * Debug（调试输出）Log.d(……)
         * Verbose（只用于开发）Log.v(……)*/
        if (savedInstanceState != null) {
            mQuestionsIndex = savedInstanceState.getInt(KEY_INDEX, 0);//返回KEY_INDEX所对应的值，如果找不到，就返回默认值0
            Log.d(TAG, "恢复状态");
        }
        setContentView(R.layout.activity_main);

        createNotificationChannel(channelId, channelName, importance);//测试阶段

        mButtonTrue = findViewById(R.id.Button_true);//引用组件赋值给成员变量
        mButtonFalse = findViewById(R.id.Button_false);
        mButtonTrue.setOnClickListener(new View.OnClickListener() {//此处采用匿名内部类实现View.setOnClickListener接口，并重写唯一的onClick方法
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,R.string.Toast_BT,Toast.LENGTH_SHORT).show();//字符串资源引用方式
                //Toast.makeText(MainActivity.this,"你点击了左边的按钮",Toast.LENGTH_SHORT).show();//硬编码字符串方式
                //消息提示框.显示文本信息(指明要显示消息的Activity,显示内容（字符串资源引用/硬编码字符串）,消息停留时长)
                checkQuestion(true,v);
            }
        });
        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,R.string.Toast_BF,Toast.LENGTH_SHORT).show();
                checkQuestion(false,v);
            }
        });

        mQuestionTextView = findViewById(R.id.question_text_View);
        updateQuestion();//获取题目

        mButtonNext = findViewById(R.id.Button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionsIndex = (mQuestionsIndex + 1) % mQuestions.length;//防溢出的递增循环算法
                updateQuestion();
                mButtonNext.setEnabled(false);//新的题目开始时保持按钮不可用的状态
                if (mQuestionsIndex == mQuestions.length - 1)//判断是否为最后一题
                {
                    Toast.makeText(MainActivity.this, R.string.last, Toast.LENGTH_SHORT).show();
                    mButtonNext.setText(R.string.Button_Reset);//更换钮换文字
                    updateButtonNext(R.drawable.ic_button_reset);//更换按钮图片
                }
                if (mQuestionsIndex == 0)//判断是否为第一题
                {
                    mButtonNext.setText(R.string.Button_Next);//更换按钮文字
                    updateButtonNext(R.drawable.ic_button_next);//更换按钮图片
                }
                /*Random rand = new Random();//随机出题
                int i = rand.nextInt(mQuestions.length);*/
            }
        });

        mButtonAnswer = findViewById(R.id.Button_Tips);
        mButtonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                if (mQuestions[mQuestionsIndex].isAnswer()) {
                    temp = "正确";
                } else {
                    temp = "错误";
                }
                Intent intent = new Intent();//实例化Intent对象
                intent.putExtra("mes", temp);//以键值对的形式保存信息数据
                intent.setClass(MainActivity.this, AnswerActivity.class);//异步消息传递要调用的（包名,类名）
                //Intent intent=new Intent(MainActivity.this, AnswerActivity.class);//如果不传递任何值，可直接以构造方法调用
                //boolean a=mQuestions[mQuestionsIndex].isAnswer();//获取答案
                //Intent intent=AnswerActivity.newIntent(MainActivity.this,a);//封装到intent对象中
                //startActivity(intent);//调用activity
                startActivityForResult(intent, REQUEST_CODE_ANSWER);//需要返回值的调用方法，第一个参数上面的方法相同，第二个参数为请求代码（由开发者自定义）
                //有时，在一个activity启动多个不同类型的子activity，且需要判断消息回馈方时，就会用到该请求代码
            }
        });
    }

    private void updateQuestion()//获取题目方法
    {
        int i = mQuestions[mQuestionsIndex].getTextId();//获取题目的资源ID
        mQuestionTextView.setText(i);//显示出来
        mTranslateAnimation = new TranslateAnimation(0, 200, 0, 0);//这四个参数含义分别是当前View x起点坐标、x终点坐标、y起点坐标、y终点坐标
        mTranslateAnimation.setDuration(2000);//动画持续时间
        mTranslateAnimation.setRepeatCount(1);//重复次数(不包括第一次)
        mTranslateAnimation.setRepeatMode(Animation.REVERSE);//动画执行模式

        mAlphaAnimation = new AlphaAnimation(0, 1);
        mAlphaAnimation.setDuration(1000);
        mAlphaAnimation.setRepeatMode(Animation.REVERSE);

        mQuestionTextView.startAnimation(mAlphaAnimation);
    }

    private void checkQuestion(boolean userAnswer,View v)//检验用户答题方法,View为测试阶段变量
    {
        boolean trueAnswer = mQuestions[mQuestionsIndex].isAnswer();
        int message;
        if (userAnswer == trueAnswer) {
            message = R.string.yes;
            mButtonNext.setEnabled(true);

        } else {
            message = R.string.no;
            mButtonNext.setEnabled(false);
        }
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        sendChatMsg(v);//测试阶段
    }

    private void createNotificationChannel(String channelId, String channelName, int importance) //测试阶段
    {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void sendChatMsg(View view)//测试阶段
    {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("收到一条聊天消息")
                .setContentText("test")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_button_next)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_button_next))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    private void updateButtonNext(int imageID)//用于更换(Next)按钮的图片
    {
        Drawable d = getDrawable(imageID);//获取图片资源
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());//左右边距，上下边距，长，宽
        mButtonNext.setCompoundDrawables(null, null, d, null);//放在按钮右边
    }

    @Override
    protected void onStart() {
        super.onStart();//首先要调用超类中的实现方法
        Log.d(TAG, "onStart()使界面可见");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()界面前台显示");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()界面离开前台");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()界面不可见");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()销毁" + TAG);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()我又肥来了");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//重写处理返回结果方法，请求代码，结果代码，Intent对象
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("answer_shown");
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
        //请求代码比喻为谁发起的请求
        //结果代码比喻为谁返回了数据
        //Intent对象比喻为数据内容
    }
}