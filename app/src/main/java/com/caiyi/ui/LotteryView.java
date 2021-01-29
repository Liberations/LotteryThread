package com.caiyi.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.caiyi.interfaces.ILotteryData;
import com.lottery9188.Activity.R;

import java.util.ArrayList;
import java.util.List;

public class LotteryView extends View {
    private List<ILotteryData> hundreds;
    private List<ILotteryData> tens;
    private List<ILotteryData> units;
    private int divColor;// 分割线颜色
    private int ballTextColor;// 球上字体颜色
    private int ballTextSize;// 球上字体大小
    private int headTextColor;// 个十百字体颜色
    private int headTextSize;// 个十百字体大小
    private int head2TextColor;// 顶部奇偶字体颜色
    private int head2TextSize;// 顶部奇偶字体大小
    private int divWidth;//分割线宽度
    private int ballLineWidth;//连接线宽度
    private int ballBgHeight;
    private int headHeight;//顶部高度
    private int ballBgWidth;
    private int ballRadius;//球的半径
    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final String TAG = "LotteryView";
    private int height;
    private boolean isHorizontal;//是否水平
    private int width;

    public LotteryView(Context context) {
        this(context, null, 0);
    }

    public LotteryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LotteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources resources = context.getResources();
        hundreds = new ArrayList<>();
        tens = new ArrayList<>();
        units = new ArrayList<>();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.myLottery);
        divColor = typedArray.getColor(R.styleable.myLottery_divColor, Color.GRAY);//分割线颜色
        divWidth = typedArray.getDimensionPixelSize(R.styleable.myLottery_divWidth, 4);//分割线宽度
        ballBgHeight = typedArray.getDimensionPixelSize(R.styleable.myLottery_ballBgHeight, 120);//每个网格高度
        ballLineWidth = typedArray.getDimensionPixelSize(R.styleable.myLottery_ballLineWidth, 4);//球连接线的宽度
        headHeight = typedArray.getDimensionPixelSize(R.styleable.myLottery_headHeight, 240);//抬头高度
        ballRadius = typedArray.getDimensionPixelSize(R.styleable.myLottery_ballRadius, 45);//每个球的半径
        ballTextColor = typedArray.getColor(R.styleable.myLottery_ballTextColor, Color.WHITE);//颜色
        ballTextSize = typedArray.getDimensionPixelSize(R.styleable.myLottery_ballTextSize, 30);//字体大小
        headTextColor = typedArray.getColor(R.styleable.myLottery_headTextColor, Color.BLACK);//颜色
        headTextSize = typedArray.getDimensionPixelSize(R.styleable.myLottery_headTextSize, 30);//字体大小
        head2TextColor = typedArray.getColor(R.styleable.myLottery_head2TextColor, Color.BLACK);//颜色
        head2TextSize = typedArray.getDimensionPixelSize(R.styleable.myLottery_head2TextSize, 30);//字体大小
        isHorizontal = typedArray.getBoolean(R.styleable.myLottery_isHorizontal, false);//字体大小
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isHorizontal){
            drawHXYLine(canvas);
            drawHBall(canvas, 4 * ballBgWidth, hundreds);
            drawHBall(canvas, 2 * ballBgWidth, tens);
            drawHBall(canvas, 0, units);
        }else {
            drawXYLine(canvas);
            drawBall(canvas, 0, hundreds);
            drawBall(canvas, 2 * ballBgWidth, tens);
            drawBall(canvas, 4 * ballBgWidth, units);
        }

    }


    private void drawHBall(Canvas canvas, int startY, List<ILotteryData> datas) {
        //初始化起点
        for (int i = 0; i < datas.size(); i++) {
            ILotteryData data = datas.get(i);
            int centerX =  (int) (headHeight + 1.5 * ballBgHeight + i * ballBgHeight);
            int centerY = data.isOdd() ? startY + ballBgWidth / 2 : startY + (int) (1.5 * ballBgWidth);
            mPaint.setColor(data.getBgColor());
            canvas.drawCircle(centerX, centerY, ballRadius, mPaint);
            if (i + 1 <= datas.size() - 1) {
                int nextI = i + 1;
                ILotteryData nextData = datas.get(nextI);
                int nextCenterX = (int) (headHeight + 1.5 * ballBgHeight + nextI * ballBgHeight);
                int nextCenterY =  nextData.isOdd() ? startY + ballBgWidth / 2 : startY + (int) (1.5 * ballBgWidth);
                mPaint.setStrokeWidth(ballLineWidth);
                canvas.drawLine(centerX, centerY, nextCenterX, nextCenterY, mPaint);
            }
            canvas.save();
            String text;
            if (data.isOdd()) {
                text = "奇";
            } else {
                text = "偶";
            }
            canvas.translate(centerX, centerY);
            mTextPaint.setColor(ballTextColor);
            mTextPaint.setTextSize(ballTextSize);
            float textWidth = mTextPaint.measureText(text);
            float baseLineY = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
            canvas.drawText(text, -textWidth / 2, baseLineY, mTextPaint);
            canvas.restore();
        }
    }


    private void drawHXYLine(Canvas canvas) {
        mPaint.setColor(divColor);
        mPaint.setStrokeWidth(divWidth);

        for (int i = 0; i <= 6; i++) {
            int startX = 0;
            //奇数线留空
            if (i % 2 == 1) {
                startX = headHeight;
            }
            Log.d(TAG, "drawXYLine:startX " + startX);
            canvas.drawLine(startX, i * ballBgWidth, width, i * ballBgWidth, mPaint);
        }


        //绘制Y轴
        canvas.drawLine(0, 0, 0, getHeight(), mPaint);
        for (int i = 0; i <= hundreds.size(); i++) {
            canvas.drawLine(headHeight + i * ballBgHeight, 0, headHeight + i * ballBgHeight, getHeight(), mPaint);

        }
        //绘制个十百
        drawHTopText(canvas, "百位",headHeight / 2 , ballBgWidth*5);
        drawHTopText(canvas, "十位",  headHeight / 2,ballBgWidth * 3);
        drawHTopText(canvas, "个位",  headHeight / 2,ballBgWidth );

        //绘制奇偶
        drawHTopOdd(canvas);


    }

    //绘制奇偶
    private void drawHTopOdd(Canvas canvas) {
        canvas.save();
        canvas.translate(headHeight + ballBgHeight / 2,0 );
        mTextPaint.setTextSize(head2TextSize);
        mTextPaint.setColor(head2TextColor);
        String text;
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                text = "奇";
            } else {
                text = "偶";
            }
            int tempWidth;
            tempWidth = i == 0 ? ballBgWidth / 2 : ballBgWidth;
            canvas.translate(0,tempWidth );
            float textWidth = mTextPaint.measureText(text);
            float baseLineY = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
            canvas.drawText(text, -textWidth / 2, baseLineY, mTextPaint);
        }
        canvas.restore();
    }


    private void drawHTopText(Canvas canvas, String text, int dx, int dy) {
        canvas.save();
        canvas.translate(dx, dy);
        mTextPaint.setTextSize(headTextSize);
        mTextPaint.setColor(headTextColor);
        float textWidth = mTextPaint.measureText(text);
        float baseLineY = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
        canvas.drawText(text, -textWidth / 2, baseLineY, mTextPaint);
        canvas.restore();
    }

    private void drawBall(Canvas canvas, int startX, List<ILotteryData> datas) {
        //初始化起点
        for (int i = 0; i < datas.size(); i++) {
            ILotteryData data = datas.get(i);
            int centerX = data.isOdd() ? startX + ballBgWidth / 2 : startX + (int) (1.5 * ballBgWidth);
            int centerY = (int) (headHeight + 1.5 * ballBgHeight + i * ballBgHeight);
            mPaint.setColor(data.getBgColor());
            canvas.drawCircle(centerX, centerY, ballRadius, mPaint);
            if (i + 1 <= datas.size() - 1) {
                int nextI = i + 1;
                ILotteryData nextData = datas.get(nextI);
                int nextCenterX = nextData.isOdd() ? startX + ballBgWidth / 2 : startX + (int) (1.5 * ballBgWidth);
                int nextCenterY = (int) (headHeight + 1.5 * ballBgHeight + nextI * ballBgHeight);
                mPaint.setStrokeWidth(ballLineWidth);
                canvas.drawLine(centerX, centerY, nextCenterX, nextCenterY, mPaint);
            }
            canvas.save();
            String text;
            if (data.isOdd()) {
                text = "奇";
            } else {
                text = "偶";
            }
            canvas.translate(centerX, centerY);
            mTextPaint.setColor(ballTextColor);
            mTextPaint.setTextSize(ballTextSize);
            float textWidth = mTextPaint.measureText(text);
            float baseLineY = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
            canvas.drawText(text, -textWidth / 2, baseLineY, mTextPaint);
            canvas.restore();
        }
    }


    private void drawXYLine(Canvas canvas) {
        mPaint.setColor(divColor);
        mPaint.setStrokeWidth(divWidth);
        //绘制X轴
        for (int i = 0; i <= hundreds.size(); i++) {
            canvas.drawLine(0, headHeight + i * ballBgHeight, getWidth(), headHeight + i * ballBgHeight, mPaint);
        }

        //绘制Y轴
        for (int i = 0; i <= 6; i++) {
            int startY = 0;
            //奇数线留空
            if (i % 2 == 1) {
                startY = headHeight;
            }
            Log.d(TAG, "drawXYLine: " + startY);
            canvas.drawLine(i * ballBgWidth, startY, i * ballBgWidth, getHeight(), mPaint);
        }
        //绘制个十百
        drawTopText(canvas, "百位", ballBgWidth, headHeight / 2);
        drawTopText(canvas, "十位", ballBgWidth * 3, headHeight / 2);
        drawTopText(canvas, "个位", ballBgWidth * 5, headHeight / 2);

        //绘制奇偶
        drawTopOdd(canvas);


    }

    //绘制奇偶
    private void drawTopOdd(Canvas canvas) {
        canvas.save();
        canvas.translate(0, headHeight + ballBgHeight / 2);
        mTextPaint.setTextSize(head2TextSize);
        mTextPaint.setColor(head2TextColor);
        String text;
        for (int i = 0; i < 6; i++) {
            if (i % 2 == 0) {
                text = "奇";
            } else {
                text = "偶";
            }
            int tempWidth;
            tempWidth = i == 0 ? ballBgWidth / 2 : ballBgWidth;
            canvas.translate(tempWidth, 0);
            float textWidth = mTextPaint.measureText(text);
            float baseLineY = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
            canvas.drawText(text, -textWidth / 2, baseLineY, mTextPaint);
        }
        canvas.restore();
    }


    private void drawTopText(Canvas canvas, String text, int dx, int dy) {
        canvas.save();
        canvas.translate(dx, dy);
        mTextPaint.setTextSize(headTextSize);
        mTextPaint.setColor(headTextColor);
        float textWidth = mTextPaint.measureText(text);
        float baseLineY = Math.abs(mTextPaint.ascent() + mTextPaint.descent()) / 2;
        canvas.drawText(text, -textWidth / 2, baseLineY, mTextPaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        ballBgWidth = isHorizontal ? heightSize / 6 : widthSize / 6;
        Log.d(TAG, "onMeasure: " + widthSize);
        if (isHorizontal) {
            if (width != 0) {
                setMeasuredDimension(width, heightSize);
                return;
            }
        } else {
            if (height != 0) {
                setMeasuredDimension(widthSize, height);
                return;
            }
        }
        setMeasuredDimension(widthSize, heightSize);
    }


    public void setData(List<ILotteryData> hundreds,
                        List<ILotteryData> tens,
                        List<ILotteryData> units) {
        this.hundreds = hundreds;
        this.units = units;
        this.tens = tens;
        height = hundreds.size() * ballBgHeight + headHeight + ballBgHeight;
        width = hundreds.size() * ballBgHeight + headHeight + ballBgHeight;
        invalidate();

    }


}
