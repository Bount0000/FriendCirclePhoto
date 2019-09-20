package com.zlc.friendcirclephoto.ui;

import android.os.Bundle;
import android.view.View;

import com.zlc.friendcirclephoto.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView(R.id.id_iv_back).setVisibility(View.GONE);
    }

    public void sendPhoto(View view){
        toActivity(SendDynamicActivity.class);
    }
}
