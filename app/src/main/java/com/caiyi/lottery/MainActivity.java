package com.caiyi.lottery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lottery9188.Activity.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testLottery(View view) {
        startActivity(new Intent(this,LotteryActivity.class));
    }

    public void testTrendView(View view) {
        startActivity(new Intent(this,GaoPinActivity.class));
    }
}