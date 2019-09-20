package com.zlc.friendcirclephoto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 通过xml查找相应的ID，通用方法
     */
    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }


    public void toActivity(Class clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

}
