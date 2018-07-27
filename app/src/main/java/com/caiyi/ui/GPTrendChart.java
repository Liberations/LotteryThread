package com.caiyi.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.caiyi.data.TrendData;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 */
public class GPTrendChart extends GTrendChart {
    private static final boolean DEBUG = false;
    private static final String TAG = "DDTrendChart";
    private boolean mDrawLine = true;
    private String mLotteryType;
    private Path mPathPoint = new Path();
    private ISelectedChangeListener mSelectedChangeListener;
    private TreeSet<Integer> mSelectedRed = new TreeSet();
    private boolean mShowYilou = true;
    private ArrayList<TrendData> mTrendData;
    private int redCount = 11;

    public interface ISelectedChangeListener {
        void onSelectedChange(TreeSet<Integer> treeSet);
    }

    public GPTrendChart(Context context, GpTrendView lottoTrendView) {
        super(context, lottoTrendView);
        this.mPaint.setTextAlign(Align.CENTER);
    }

    /**
     * @param str
     * @param arrayList 数据
     */
    public void updateData(String str, ArrayList<TrendData> arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            if ("01".equals(str) || "50".equals(str)) {
                this.mLotteryType = str;
            } else {
                this.mLotteryType = "01";
            }
            this.mTrendData = arrayList;
            this.mSelectedRed.clear();
            this.mPathPoint.reset();
            this.mScaleRange = new float[]{0.0f, 0.0f};
            if ("01".equals(this.mLotteryType)) {
                this.redCount = 11;
                for (int i = 0; i < arrayList.size(); i++) {
                    TrendData trendData = (TrendData) arrayList.get(i);
                    if ("row".equals(trendData.getType())) {
                        String[] split = trendData.getRed().split(",");
                        for (int i2 = 0; i2 < split.length; i2++) {
                            if (split[i2].equals("0")) {
                                float f = ((float) i2 + 0.5f) * this.mXItemWidth;
                                float f2 = (((float) i) + 0.5f) * ((float) this.mXItemHeight);
                                if (i == 0) {
                                    this.mPathPoint.moveTo(f, f2);
                                } else {
                                    this.mPathPoint.lineTo(f, f2);
                                }
                            }
                        }
                    }
                }
            }

            if (this.mTrendView != null) {
                initChart(this.mTrendView.getContext(), this.mTrendView.getWidth(), this.mTrendView.getHeight(), this.mTrendView.getScale());
                this.mTrendView.invalidate();
            }
            if (this.mSelectedChangeListener != null) {
                this.mSelectedChangeListener.onSelectedChange(this.mSelectedRed);
            }
        }
    }

    public void initChart(Context context, int i, int i2, float f) {
        if (i != 0 && i2 != 0 && this.mTrendData != null && this.mTrendData.size() >= 4) {
            super.initChart(context, i, i2, f);
            if (this.mTrendView != null) {
                this.mTrendView.setNowY((float) (-this.mPicY.getHeight()));
            }
        }
    }

    public void setSelectedChangeListener(ISelectedChangeListener iSelectedChangeListener) {
        this.mSelectedChangeListener = iSelectedChangeListener;
    }

    /**
     * 画线
     *
     * @param z
     */
    public void setDrawLine(boolean z) {
        if ((this.mDrawLine != z ? 1 : null) != null) {
            this.mDrawLine = z;
            if (this.mTrendData != null) {
                drawContent();
            }
            if (this.mTrendView != null) {
                this.mTrendView.invalidate();
            }
        }
    }

    /**
     * 遗漏   是否现实遗漏数据
     *
     * @param z
     */
    public void setShowYilou(boolean z) {
        if ((this.mShowYilou != z ? 1 : null) != null) {
            this.mShowYilou = z;
            if (this.mTrendData != null) {
                drawContent();
            }
            if (this.mTrendView != null) {
                this.mTrendView.invalidate();
            }
        }
    }


    public boolean onClick(MotionEvent motionEvent, float f, float f2, int i, int i2, float f3) {
        if (motionEvent.getY() <= ((float) i2) - (((float) this.mXItemHeight) * f3) || motionEvent.getX() <= ((float) this.mYItemWidth) * f3) {
            return false;
        }
        int x = (int) ((motionEvent.getX() - f) / (((float) this.mXItemWidth) * f3));

        if (this.mSelectedRed.contains(Integer.valueOf(x))) {
            this.mSelectedRed.remove(Integer.valueOf(x));
        } else {
            this.mSelectedRed.add(Integer.valueOf(x));
        }
        if (this.mSelectedChangeListener != null) {
            this.mSelectedChangeListener.onSelectedChange(this.mSelectedRed);
        }

        drawXBottom();
        return true;
    }

    /**
     * 画y轴
     */
    public void drawY() {
        if (this.mTrendData != null && this.mTrendData.size() >= 4) {
            Canvas beginRecording = this.mPicY.beginRecording(this.mYItemWidth, (this.mYItemHeight * this.mTrendData.size()) + this.mDivHeight);
            this.mPaint.setStyle(Style.FILL);
            int size = this.mTrendData.size();
            for (int i = 0; i < size; i++) {
                this.mPaint.setFakeBoldText(true);
                int i2 = i * this.mYItemHeight;
                if (i == size ) {
                    this.mRect.set(0, this.mYItemHeight * i, this.mYItemWidth, (this.mYItemHeight * i) + this.mDivHeight);
                    this.mPaint.setColor(-1);
                    beginRecording.drawRect(this.mRect, this.mPaint);
                    this.mPaint.setColor(this.mCDiv);
                    //画Y轴间隔的距离的2条线
//                    beginRecording.drawLine(0.0f, (float) i2, (float) this.mYItemWidth, (float) i2, this.mPaint);
//                    beginRecording.drawLine(0.0f, (float) ((this.mDivHeight + i2) - 1), (float) this.mYItemWidth, (float) ((this.mDivHeight + i2) - 1), this.mPaint);
                    this.mRect.set(0, this.mDivHeight + i2, this.mYItemWidth, (this.mYItemHeight + i2) + this.mDivHeight);
                } else if (i > size ) {
                    this.mRect.set(0, this.mDivHeight + i2, this.mYItemWidth, (this.mYItemHeight + i2) + this.mDivHeight);
                } else {
                    this.mRect.set(0, i2, this.mYItemWidth, this.mYItemHeight + i2);
                }
                String type = ((TrendData) this.mTrendData.get(i)).getType();
                if (type.equals("row")) {
                    type = ((TrendData) this.mTrendData.get(i)).getPid()+"期";
                    if (i % 2 == 0) {
                        this.mPaint.setColor(this.mCOddY);
                    } else {
                        this.mPaint.setColor(this.mCEvenY);
                    }
                    beginRecording.drawRect(this.mRect, this.mPaint);
                    this.mPaint.setColor(this.mCYText);
                }else {
                    type = "??";
                }

                this.mPaint.setTextSize((float) this.mYTextSize);
                drawText2Rect(type, beginRecording, this.mRect, this.mPaint);
            }
            this.mPicY.endRecording();
        }
    }

    /**
     * 左下角
     */
    public void drawLeftBottom() {
        int i = this.mXItemHeight + this.mBottomMargin;
        Canvas beginRecording = this.mPicLeftBottom.beginRecording(this.mYItemWidth, i);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(-1);
        this.mRect.set(0, 0, this.mYItemWidth, i);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(this.mCDiv);
        beginRecording.drawLine(0.0f, 2.0f, (float) this.mYItemWidth, 2.0f, this.mPaint);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setTextSize((float) this.mLcTextSize);
        this.mRect.set(0, this.mBottomMargin, this.mYItemWidth, i);
        drawText2Rect("预选区", beginRecording, this.mRect, this.mPaint);
        this.mPicLeftBottom.endRecording();
    }

    /**
     * 画左上角
     */
    public void drawLeftTop() {
        Canvas beginRecording = this.mPicLeftTop.beginRecording(this.mYItemWidth, this.mXItemHeight);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(this.mCQihaoTextBg);
        this.mRect.set(0, 0, this.mYItemWidth, this.mXItemHeight);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setTextSize((float) this.mLcTextSize);
        drawText2Rect("  ", beginRecording, this.mRect, this.mPaint);
        this.mPicLeftTop.endRecording();
    }

    /**
     * 画上边
     */
    public void drawXTop() {
        int i;
        float i2 = this.mXItemWidth * this.redCount;
        int i3 = this.mXItemHeight;
        Canvas beginRecording = this.mPicXTop.beginRecording((int) i2, i3);
        this.mRect.set(0, 0, i2, i3);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(this.mCXTitleBg);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(-1);
        this.mRect.set(this.redCount * this.mXItemWidth, 0, (this.redCount * this.mXItemWidth), i3);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setTextSize((float) this.mLcTextSize);
        for (i = 1; i <= this.redCount; i++) {
            String str;
            float i4 = i * this.mXItemWidth;
            this.mPaint.setColor(this.mCDiv);
            //xTOP上面的垂线
//            beginRecording.drawLine((float) i4, 0.0f, (float) i4, (float) i3, this.mPaint);
            this.mRect.set(i4 - this.mXItemWidth, 0, i4, i3);
            this.mPaint.setColor(mQiHaoNum);
            if (i <= 9) {
                str = "0" + i;
            } else {
                str = "" + i;
            }
            drawText2Rect(str, beginRecording, this.mRect, this.mPaint);

        }
        this.mPicXTop.endRecording();
    }

    /**
     * 画Xbottom
     */
    public void drawXBottom() {
        if(true)return;
        int i = 1;
        float i2 = this.mXItemWidth * this.redCount;
        int i3 = this.mXItemHeight + this.mBottomMargin;
//        int i3 = this.mTimeHeight + this.mBottomMargin;
        Canvas beginRecording = this.mPicXBottom.beginRecording((int) i2, i3);
        this.mRect.set(0, 0, i2, i3);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(-1);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(this.mCDiv);
        beginRecording.drawLine(0.0f, 2.0f, (float) i2, 2.0f, this.mPaint);
        this.mPaint.setTextSize((float) this.mXTextSize);
        for (int i4 = 1; i4 <= this.redCount; i4++) {
            String str;
            this.mRect.set((i4 - 1) * this.mXItemWidth, this.mBottomMargin, this.mXItemWidth * i4, i3);
            if (this.mSelectedRed.contains(Integer.valueOf(i4 - 1))) {
                this.mPaint.setStyle(Style.FILL);
                this.mPaint.setColor(this.mCBallSelectedRed);
                beginRecording.drawCircle(this.mRect.centerX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                this.mPaint.setColor(-1);
            } else {
                this.mPaint.setStyle(Style.STROKE);
                this.mPaint.setColor(this.mCBallSelectedStroke);
                beginRecording.drawCircle(this.mRect.centerX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                this.mPaint.setColor(this.mCXbottomTextRed);
                this.mPaint.setStyle(Style.FILL);
            }
            if (i4 <= 9) {
                str = "0" + i4;
            } else {
                str = "" + i4;
            }
            drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
        }
        this.mPicXBottom.endRecording();
    }

    /**
     * 画球
     */
    public void drawContent() {
        int i;
        float i2 = this.mXItemWidth * this.redCount;
        Canvas beginRecording = this.mPicContent.beginRecording((int) i2, (this.mYItemHeight * this.mTrendData.size()) + this.mDivHeight);
        this.mPaint.setTextSize((float) this.mCTextSize);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setFakeBoldText(false);
        int i3 = this.redCount;//球的个数
        int size = this.mTrendData.size();//数据的个数,包括平均遗漏等数据
        for (i = 0; i <= size; i++) {
            int i4 = i * this.mXItemHeight;
            if (i != size) {
                //如果不包含平均遗漏等  画横线
                if (i < size ) {
                    this.mRect.set(0, i4, i2, this.mXItemHeight + i4);
                    if (i % 2 == 0) {
                        this.mPaint.setColor(-1);
                    } else {
                        this.mPaint.setColor(this.mCOddContent);
                    }
                    //画框
                    beginRecording.drawRect(this.mRect, this.mPaint);
                    this.mPaint.setColor(this.mCDiv);
                    //画横线
//                    beginRecording.drawLine(0.0f, (float) i4, (float) i2, (float) i4, this.mPaint);
                } else {
                    //画平均遗漏等数据
                    this.mRect.set(0, this.mDivHeight + i4, i2, (i4 + this.mDivHeight) + this.mXItemHeight);
                    if (i == (size ) + 1) {
                        this.mPaint.setColor(this.mCAvgYilouBg);
                    } else if (i == size - 1) {
                        this.mPaint.setColor(this.mCLianchuBg);
                    } else {
                        this.mPaint.setColor(-1);
                    }
                    beginRecording.drawRect(this.mRect, this.mPaint);
                }
            }
        }
        float size2 = this.mTrendData.size() * this.mXItemWidth;
        for (i = 0; i <= i3; i++) {
            float i5 = i * this.mXItemWidth;
            if (i == this.redCount) {
                this.mPaint.setColor(-1);
                this.mRect.set(i5, 0, i5, size2);
                beginRecording.drawRect(this.mRect, this.mPaint);
                this.mPaint.setColor(this.mCDiv);
            } else if (i < this.redCount) {
                this.mPaint.setColor(this.mCDiv);
//                beginRecording.drawLine((float) i5, 0.0f, (float) i5, (float) size2, this.mPaint);
            }
        }
        i = (size ) * this.mXItemHeight;
        this.mPaint.setColor(-1);
        //画垂直方向的空白的矩形（与平均遗漏）
        this.mRect.set(0, i, i2, this.mDivHeight + i);
        beginRecording.drawRect(this.mRect, this.mPaint);
        this.mPaint.setColor(this.mCDiv);
        //画横线
//        beginRecording.drawLine(0.0f, (float) i, (float) i2, (float) i, this.mPaint);

//        beginRecording.drawLine(0.0f, (float) ((this.mDivHeight + i) - 1), (float) i2, (float) ((this.mDivHeight + i) - 1), this.mPaint);
        if ("01".equals(this.mLotteryType) && this.mDrawLine) {
            //绘制折线信息
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setColor(this.mCBallRed);
            this.mPaint.setStrokeWidth(3);
            beginRecording.drawPath(this.mPathPoint, this.mPaint);
            this.mPaint.setStyle(Style.FILL);
        }
        i = this.mTrendData.size();
        this.mPaint.setStyle(Style.FILL);
        //画数字
        for (int i6 = 0; i6 < i; i6++) {
            int i7;
            String str;
            TrendData trendData = (TrendData) this.mTrendData.get(i6);

            //如果数据有空的情况
            if (TextUtils.isEmpty(trendData.getRed())) {
                int i8 = this.mXItemHeight * i6;
                    if (i6 >= i ) {
                    i8 += this.mDivHeight;
                }
                this.mRect.set(0, i8, getDisplayWidth(this.mTrendView.getContext()), this.mXItemHeight + i8);
                this.mPaint.setColor(-1);
                drawText2Rect("等待开奖", beginRecording, this.mRect, this.mPaint);
            } else {
                String[] split = trendData.getRed().split(",");
                int i8 = this.mXItemHeight * i6;
                if (i6 >= i ) {
                    i8 += this.mDivHeight;
                }
                for (i7 = 0; i7 < split.length; i7++) {
                    this.mRect.set(this.mXItemWidth * i7, i8, (i7 + 1) * this.mXItemWidth, this.mXItemHeight + i8);
                    if (!"row".equals(trendData.getType())) {
                        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
                        drawText2Rect(split[i7], beginRecording, this.mRect, this.mPaint);
                    } else if (split[i7].equals("0")) {
                        this.mPaint.setColor(this.mCBallRed);
                        beginRecording.drawCircle(this.mRect.centerX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
                        this.mPaint.setColor(-1);
                        if (i7 < 9) {
                            str = "0" + (i7 + 1);
                        } else {
                            str = "" + (i7 + 1);
                        }
                        drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
                    } else if (this.mShowYilou) {
                        this.mPaint.setColor(this.mCYilou);
                        drawText2Rect(split[i7], beginRecording, this.mRect, this.mPaint);
                    }
                }
            }
        }


        this.mPicContent.endRecording();
    }

    protected CharSequence getKuaiPingLeftTime() {
        return null;
    }
}
