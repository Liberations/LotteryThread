package com.caiyi.data;

import android.graphics.Color;

import com.caiyi.interfaces.ILotteryData;

public class BlueBall implements ILotteryData {
    public int num;
    @Override
    public boolean isOdd() {
        return num/2==1;
    }

    @Override
    public int getBgColor() {
        return Color.BLUE;
    }
}
