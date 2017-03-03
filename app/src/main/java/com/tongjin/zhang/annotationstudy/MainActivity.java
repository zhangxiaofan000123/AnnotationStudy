package com.tongjin.zhang.annotationstudy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@EMLayoutBinder(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @EMViewBinder(R.id.hello)
    private TextView mHello;
    @EMViewBinder(R.id.test1)
    private Button mTest1;
    @EMViewBinder(R.id.test2)
    private Button mTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHello.setText("Hello Annotation!");
    }

    @EMOnClickBinder({R.id.test1, R.id.test2})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.test1:
                mHello.setText("I am test11");

                break;
            case R.id.test2:
                mHello.setText("I am test2");
                break;
            default:
                break;

        }
    }
}
