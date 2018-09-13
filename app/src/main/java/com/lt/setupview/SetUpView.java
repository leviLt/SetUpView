package com.lt.setupview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Scorpio
 * @date 2018/9/10
 * QQ:751423471
 * phone:13982250340
 */
public class SetUpView extends View {

    private static final int DEFAULT_HEIGHT = 85; //默认高度
    private static final int DEFAULT_PADDING = 10; //默认padding值
    private static final int DAYS_MARGIN_TOP = 13; // 文字距离的marginTop值
    private static final float SETUP_HEXAGON_SCALE = 1F / 6; //六边形的缩放值
    private static final float SETUP_LINE_BG_SCALE = 1F / 4; //横线的 缩放值
    private static final float SECTION_SCALE = 1.2F / 2; //截面的缩放值

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
    /*-----------------------颜色-----------------------*/
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
    //padding
    private int mPadding;
    //控件宽
    private int mSetUpViewWidth;
    //控件高度
    private int mSetUpViewHeight;
    //六边形的半径
    private int mHexagonRadius;
    //横线的高度
    private int mLineHeight;
    //控件宽度直线
    private RectF mSetUpLineRecf;
    //签到时间
    private List<String> days;
    //六边形的Y轴坐标
    private int circleY;
    //六边形的中心点坐标
    private List<Point> centerHexagonPoints;

    /*-----------------------pathList---- -----------------------*/
    // 六边形path路径集合
    private List<Path> mHexagonPaths;
    /*-----------------------pathList---------------------------*/

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
        mSetUpBgPaint = getPaint(mSetUpBgColor, 0, Paint.Style.FILL, 0);
        mSetUpLineBgPaint = getPaint(mSetUpLineBgColor, 0, Paint.Style.FILL, 0);
        mSetUpBigHexagonPaint = getPaint(mSetUpBigHexagonColor, 0, Paint.Style.FILL, 3);
        mSetUpDaysPaint = getPaint(mSetUpDaysColor, 0, Paint.Style.FILL, 0);
        //礼物图标
        giftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_signpage_gift);
        //礼物矩形区域
        giftRecf = new RectF(0, 0, giftBitmap.getWidth(), giftBitmap.getHeight());

        days = new ArrayList<>();
        days.add("1");
        days.add("2");
        days.add("3");
        days.add("4");
        days.add("5");
        centerHexagonPoints=new ArrayList<>();
        mHexagonPaths=new ArrayList<>();
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

    private Paint getPaint(int paintColor, int textSize, Paint.Style style, int linWidth) {
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //转换padding px2dp
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PADDING, getResources().getDisplayMetrics());
        //文字距离上方的距离 px2dp
        int daysMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DAYS_MARGIN_TOP, getResources().getDisplayMetrics());
        //控件的宽高
        mSetUpViewHeight = h;
        mSetUpViewWidth = w;
        //六边形的半径
        mHexagonRadius = (int) (mSetUpViewHeight * SETUP_HEXAGON_SCALE / 2);
        //横线的高度
        mLineHeight = (int) (mHexagonRadius * SETUP_LINE_BG_SCALE);

        mSetUpLineRecf = new RectF(0, mSetUpViewHeight * SECTION_SCALE / 2 - mLineHeight / 2
                , mSetUpViewWidth, mSetUpViewHeight * SECTION_SCALE / 2 + mLineHeight / 2);

        circleY = (int) (mSetUpLineRecf.top + mLineHeight / 2);
        //计算点和图形的位置
        calculateCirclePoints();
    }

    private void calculateCirclePoints() {
        if (days != null) {
            int viewCount = days.size() + 1;
            //控件宽度中，计算每段距离大小
            int oncePiece = (mSetUpViewWidth - mHexagonRadius * 2 * viewCount) / viewCount;

            for (int i = 0; i < days.size(); i++) {
                //每个六边形的 中心点位置
                Point circlePoint = new Point((i + 1) * oncePiece + ((i + 1) * 2 - 1) * oncePiece, circleY);
                //小正六边形Path
                Path smallHexagonPath = new Path();
                for (int j = 0; j < 6; j++) {
                    //第一个点
                    if (j == 1) {
                        smallHexagonPath.moveTo(circlePoint.x - mHexagonRadius, circlePoint.y);
                    } else {
                        //其余5个点
                        smallHexagonPath.lineTo((float) (circlePoint.x - mHexagonRadius * Math.cos(j*60)),
                                (float) (circlePoint.y - mHexagonRadius * Math.sin(j*60)));
                    }
                }
                smallHexagonPath.close();
                centerHexagonPoints.add(circlePoint);
                mHexagonPaths.add(smallHexagonPath);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode==MeasureSpec.AT_MOST||heightMode==MeasureSpec.UNSPECIFIED){
            heightSize=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEIGHT, getResources().getDisplayMetrics());
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画横线
        drawLineRecf(canvas);
        //画小的六边形
        drawSmallHexagon(canvas);
    }

    private void drawLineRecf(Canvas canvas) {
        canvas.drawRect(mSetUpLineRecf,mSetUpLineBgPaint);
    }

    private void drawSmallHexagon(Canvas canvas) {
        for (Path path:mHexagonPaths) {
            canvas.drawPath(path,mSetUpBigHexagonPaint);
        }
    }
}
