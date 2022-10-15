package com.xuexiang.application.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class LineChartView extends View {
    private Paint mBgPaint = new Paint();
    private Paint mXYPaint = new Paint();
    private Paint mLinePaint = new Paint();
    private Paint mLineBgPaint = new Paint();
    private float mSW;//画笔粗细大小
    private float mW;//控件宽度
    private float mH;//控件高度

    //模拟数据点
    private PointF[] ps = new PointF[]{
            new PointF(50, 200), new PointF(100, 150), new PointF(190, 220), new PointF(300, 130),
            new PointF(400, 110), new PointF(500, 200), new PointF(690, 90), new PointF(880, 50)
    };

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSW = dp2px(1);

        //XY轴画笔
        mXYPaint.setColor(Color.BLACK);
        mXYPaint.setStrokeWidth(mSW);
        mXYPaint.setStyle(Paint.Style.STROKE);
        mXYPaint.setAntiAlias(true);

        //折线画笔
        mLinePaint.setColor(Color.BLUE);
        mLinePaint.setStrokeWidth(mSW);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);

        //折线背景画笔
        mLineBgPaint.setColor(Color.BLUE);
        mLineBgPaint.setAlpha(30);
        mLineBgPaint.setStrokeWidth(mSW);
        mLineBgPaint.setStyle(Paint.Style.FILL);
        mLineBgPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setBackgroundColor(Color.WHITE);//背景色
        mW = getWidth();//控件宽度
        mH = getHeight();//控件高度

        canvas.drawLine(mSW/2, 0, mSW/2, mH, mXYPaint);//Y轴
        canvas.drawLine(0, mH-mSW/2, mW, mH-mSW/2, mXYPaint);//X轴

        //绘制折线区
        Path lp = new Path();
        Path lbp = new Path();
        float startX=0;
        for (int i = 0; i < ps.length; i++) {
            PointF p = ps[i];
            if (i == 0) {
                startX=p.x;
                lp.moveTo(p.x, p.y);//折线
                lbp.moveTo(p.x, p.y);//折线背景
            } else if (i == ps.length - 1) {
                lp.lineTo(p.x, p.y);//折线

                lbp.lineTo(p.x, p.y);//折线背景（最后一个）
                lbp.lineTo(p.x, mH-mSW);//折线背景（向下移动一个点）
                lbp.lineTo(startX, mH-mSW);//折线背景（向下移动一个点）
                lbp.close();//折线背景（封闭）
            } else {
                lp.lineTo(p.x, p.y);//折线
                lbp.lineTo(p.x, p.y);//折线背景
            }
        }
        canvas.drawPath(lbp, mLineBgPaint);//折线背景
        canvas.drawPath(lp, mLinePaint);//折线
    }

    protected float dp2px(float v) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, getResources().getDisplayMetrics());
    }
}