package com.caiyi.interfaces;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public interface ITrendChart {
    void draw(Canvas canvas, int i, int i2, int i3, int i4, float f);

    /**
     * 是否支持伸缩
     * @return
     */
    boolean getCanScale();

    /**
     * 是否支持滑动
     * @return
     */
    boolean[] getCanScroll();

    /**
     * 获取伸缩比例
     * @return
     */
    float[] getScaleRange();

    int[] getScrollRange();

    void initChart(Context context, int i, int i2, float f);

    boolean initOk();

    boolean onClick(MotionEvent motionEvent, float f, float f2, int i, int i2, float f3);

    boolean onLongClick(MotionEvent motionEvent, float f, float f2, int i, int i2, float f3);

    void reCalcScroll(float f, int i, int i2);
}
