package com.lt.setupview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Scorpio
 * @date 2018/9/10
 * QQ:751423471
 * phone:13982250340
 */
public class SetUpView extends View {
    private Context mContext;
    /*--------------------颜色------------------------*/
    //签到背景颜色
    private int mSetUpBgColor;
    //线的背景颜色
    private int mSetUpLineBgColor;
    //放大六边形颜色
    private int mSetUpBigHexagonColor;
    //下方日期的颜色
    private int mSetUpDaysColor;
    //下方日期的大小
    private int mSetUpDaysSize;
    /*----------------------------------------------*/
    /*----------------------画笔-----------------------*/
    //签到背景画笔
    private Paint mSetUpBgPaint;
    //中间线的画笔
    private Paint mSetUpLineBgPaint;
    //放大的六边形画笔
    private Paint mSetUpBigHexagonPaint;
    //字体画笔
    private Paint mSetUpDaysPaint;
    /*----------------------画笔-----------------------*/
    //礼物
    private Bitmap giftBitmap;
    //礼物矩形区域
    private RectF giftRecf;

    public SetUpView(Context context) {
        this(context, null);
    }

    public SetUpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetUpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //获取颜色、字体大小等等
        initView(attrs);
        //初始化数据、画笔等
        initTools();
    }

    private void initTools() {
        //初始化画笔
        mSetUpBgPaint=getPaint(mSetUpBgColor,0, Paint.Style.FILL,0);
        mSetUpLineBgPaint=getPaint(mSetUpLineBgColor,0, Paint.Style.FILL,0);
        mSetUpBigHexagonPaint=getPaint(mSetUpBigHexagonColor,0, Paint.Style.FILL,3);
        mSetUpDaysPaint=getPaint(mSetUpDaysColor,0, Paint.Style.FILL,0);
        //礼物图标
        giftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_signpage_gift);
        //礼物矩形区域
        giftRecf=new RectF(0,0,giftBitmap.getWidth(),giftBitmap.getHeight());
    }



    private void initView(AttributeSet attrs) {
        //初始化颜色
        if (attrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(R.styleable.SetUpView);
            mSetUpBgColor = typedArray.getColor(R.styleable.SetUpView_setUpBgColor, Color.BLACK);
            mSetUpLineBgColor = typedArray.getColor(R.styleable.SetUpView_setUpLineBgColor, Color.BLACK);
            mSetUpBigHexagonColor = typedArray.getColor(R.styleable.SetUpView_setUpBigHexagonColor, Color.BLACK);
            mSetUpDaysColor = typedArray.getColor(R.styleable.SetUpView_setUpDaysColor, Color.BLACK);
            mSetUpDaysSize = typedArray.getDimensionPixelSize(R.styleable.SetUpView_setUpDaysSize, 12);
            //释放资源
            typedArray.recycle();
        }
    }
    private Paint getPaint(int paintColor,int textSize, Paint.Style style, int linWidth) {
        Paint paint = new Paint();
        //颜色
        paint.setColor(paintColor);
        //抗锯齿
        paint.setAntiAlias(true);
        //设置画笔宽度
        paint.setStrokeWidth(linWidth);
        //防抖动
        paint.setDither(true);
        //文字大小
        paint.setTextSize(textSize);
        /*
         * Paint.Style.FILL :填充内部
         * Paint.Style.FILL_AND_STROKE ：填充内部和描边
         * Paint.Style.STROKE ：仅描边
         */
        paint.setStyle(style);
        /*
         * 设置线冒样式，取值有
         * Cap.ROUND(圆形线冒)
         * Cap.SQUARE(方形线冒)
         * Paint.Cap.BUTT(无线冒)
         */
        paint.setStrokeCap(Paint.Cap.ROUND);
        /*
         * 设置线段连接处样式，取值有：
         * Join.MITER（结合处为锐角）
         * Join.Round(结合处为圆弧)
         * Join.BEVEL(结合处为直线)
         */
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }
}
