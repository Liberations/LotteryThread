package com.caiyi.lottery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.caiyi.data.BlueBall;
import com.caiyi.data.GreenBall;
import com.caiyi.data.RedBall;
import com.caiyi.interfaces.ILotteryData;
import com.caiyi.ui.LotteryView;
import com.lottery9188.Activity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LotteryActivity extends AppCompatActivity {
    private LotteryView lotteryView;
    List<ILotteryData> hundreds = new ArrayList<>();
    List<ILotteryData> tens = new ArrayList<>();
    List<ILotteryData> units = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        lotteryView = (LotteryView) findViewById(R.id.lv);
        test();
    }

    public void testData(View view) {
        test();
    }

    private void test() {
        hundreds.clear();
        tens.clear();
        units.clear();
        for (int i = 0; i < 100; i++) {
            RedBall redBall = new RedBall();
            redBall.num =new Random().nextInt(10);
            hundreds.add(redBall);
        }
        for (int i = 0; i < 100; i++) {
            BlueBall blueBall = new BlueBall();
            blueBall.num =new  Random().nextInt(10);
            tens.add(blueBall);
        }
        for (int i = 0; i < 100; i++) {
            GreenBall greenBall = new GreenBall();
            greenBall.num =new  Random().nextInt(10);
            units.add(greenBall);
        }
        lotteryView.setData(hundreds,tens,units);
    }
}
