package com.smart.bean.watchviewpro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

/**
 * auther   : bean
 * on       : 2017/11/14
 * QQ       : 596928539
 * github   : https://github.com/Xbean1024
 * function :
 */

public class WatchView extends BaseView {
    private Paint mPaint;
    private int mMeasureWidth;
    private int mMeasureHeight;

    private int mCicleRadius;

    /*
    * 表盘中心
    * 同是也是 表针的旋转中心
    * */
    private int mCirlceCenterX;//中心
    private int mCircleCenterY;//中心
    private int mValueLine;//刻度线
    private int mHandLineStartY;

    public WatchView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    public WatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = measureHeight(widthMeasureSpec);
        mMeasureHeight = measureHeight(heightMeasureSpec);

        int padding = 10;
        mCicleRadius = (mMeasureHeight >= mMeasureWidth) ? mMeasureWidth / 2 - padding : mMeasureHeight / 2 - padding;
        mCirlceCenterX = mMeasureWidth / 2;
        mCircleCenterY = mMeasureHeight / 2;
        mHandLineStartY = (mMeasureHeight >= mMeasureWidth) ? mMeasureHeight / 2 - mMeasureWidth / 2 + padding : mMeasureWidth / 2 - mMeasureHeight / 2 + padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWatch(canvas);
    }


    private void drawWatch(Canvas canvas) {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        canvas.drawCircle(mCirlceCenterX, mCircleCenterY, mCicleRadius, mPaint);
        canvas.drawPoint(mCirlceCenterX, mCircleCenterY, mPaint);//绘制中心点
        /*
        * 表盘上一共 60格
        * 每格代表6度，为1秒（短线）
        * 每30度为一个小时，（长线）
        * 当弧度能被30整除，则为一个小时
        * 当弧度能被6整除，但不能被30整除，则
        * */
        int value = 0;
        canvas.save();
        for (int i = 0; i < 360; i++) {
            if (i % 6 == 0) {
                canvas.rotate(6, mCirlceCenterX, mCircleCenterY);//每 6 度 旋转一次画布
            }
            if (i % 30 == 0) {
                //时针
                Log.i(TAG, "drawWatch: " + i);
                mPaint.setStrokeWidth(1);
                mValueLine = mMeasureHeight / 15;
                if (i == 0) {
                    value = 12;
                } else {
                    value = value + 1;
                }
                mPaint.setTextSize(20);
                canvas.drawText(value + "", mCirlceCenterX - 10, mHandLineStartY + mValueLine + mValueLine / 2, mPaint);
                if (value == 12) {
                    value = 0;
                }
            } else {
                mPaint.setStrokeWidth(2);
                mValueLine = mMeasureHeight / 30;
            }

            if (i % 6 == 0) {
                if (i % 30 == 0) {
                    mPaint.setStrokeWidth(5);
                }
                canvas.drawLine(mCirlceCenterX, mHandLineStartY, mCirlceCenterX, mHandLineStartY + mValueLine, mPaint);
            }
        }
        canvas.restore();
    }
}
