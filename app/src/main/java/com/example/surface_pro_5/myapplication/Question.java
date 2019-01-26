package com.example.surface_pro_5.myapplication;

public class Question {
    private int mTextId;//问题文本的资源ID引用
    private boolean mAnswer;//问题的答案

    public Question(int textId, boolean answer) {
        mTextId = textId;
        mAnswer = answer;
    }

    public int getTextId() {
        return mTextId;
    }

    public void setTextId(int textId) {
        mTextId = textId;
    }

    public boolean isAnswer() {
        return mAnswer;
    }

    public void setAnswer(boolean answer) {
        mAnswer = answer;
    }
}
