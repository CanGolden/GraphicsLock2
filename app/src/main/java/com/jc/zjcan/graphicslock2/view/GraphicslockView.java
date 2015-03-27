package com.jc.zjcan.graphicslock2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 图形锁View
 * Created by ZJcan on 2015/3/18.
 */
public class GraphicslockView extends View {
    //回调接口
    private OnTouchPointsListener listener;
    //获取震动
    private Vibrator vibrator;
    //最少点数
    public static int POINT_SIZE = 5;
    //图形半径
    private float bitmapR;
    private Paint paint = new Paint();
    //初始化矩阵
    private Matrix matrix = new Matrix();
    //九个点
    private Point[][] points = new Point[3][3];
    //按下点的集合
    private List<Point> pointList = new ArrayList<Point>();
    //是否初始化
    private boolean isInit = false, isSelect = false, isFinish = false, movingNoPoint = false;
    //屏幕宽高 偏移量
    private float width, height, offsetX, offsetY, movingX, movingY;

    public GraphicslockView(Context context) {
        super(context);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public GraphicslockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public GraphicslockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit)
            initPoints(canvas);
        //画点
        points2Canvas(canvas);
        //画线
        if (pointList != null && pointList.size() > 0) {
            //绘制九宫格里的点
            Point a = pointList.get(0);
            for (int i = 0; i < pointList.size(); i++) {
                Point b = pointList.get(i);
                line2Canvas(canvas, a, b);
                a = b;
            }
            //绘制非九宫格点
            if (movingNoPoint) {
                line2Canvas(canvas, a, new Point(movingX, movingY));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        movingX = event.getX();
        movingY = event.getY();
        isFinish = false;
        Point point = null;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                resetPoint();
                point = checkSelectPoint();
                if (point != null && listener != null) {
                    isSelect = true;
                    listener.onStart(true);
                }
                isFinish = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isSelect) {
                    point = checkSelectPoint();
                    if (point == null) {
                        movingNoPoint = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isFinish = true;
                isSelect = false;
                break;
            default:
                break;
        }
        //选中重复检查
        if (!isFinish && isSelect && point != null) {
            if (crossPoint(point)) {
                movingNoPoint = true;
            } else {
                point.state = Point.STATE_PRESSED;
                pointList.add(point);
                vibrator.vibrate(50);
            }
        }
        //绘制结束
        if (isFinish) {
            if (pointList.size() == 1) {
                //绘制不成立
                resetPoint();
            } else if (pointList.size() < POINT_SIZE && pointList.size() > 1) {
                //绘制错误
                errorPoint();
                if (listener != null)
                    listener.onPointFinished(null);
            } else {
                //绘制成功
                if (listener != null) {
                    String password = "";
                    for (Point p : pointList)
                        password = password + p.index;
                    listener.onPointFinished(password);
                }
            }
        }
        //刷新view
        postInvalidate();
        return true;
    }

    /**
     * 绘制不成立
     */
    public void resetPoint() {
        for (Point p : pointList)
            p.state = Point.STATE_NORMAL;
        pointList.clear();
    }

    /**
     * 设置绘制错误
     */
    public void errorPoint() {
        for (Point point : pointList) {
            point.state = Point.STATE_ERROR;
        }
    }


    public void initPoints(Canvas canvas) {
        //1.获取屏幕宽高

        width = getWidth();
        height = getHeight();
        //2.偏移量
        if (width > height) {
            //横屏
            offsetX = (width - height) / 2;
            width = height;
        } else {
            //竖屏
            offsetY = (height - width) / 2;
            height = width;
        }
        bitmapR = width / 11;

        //点的坐标
        points[0][0] = new Point(offsetX + width / 4, offsetY + width / 4);
        points[0][1] = new Point(offsetX + width / 2, offsetY + width / 4);
        points[0][2] = new Point(offsetX + width - width / 4, offsetY + width / 4);

        points[1][0] = new Point(offsetX + width / 4, offsetY + width / 2);
        points[1][1] = new Point(offsetX + width / 2, offsetY + width / 2);
        points[1][2] = new Point(offsetX + width - width / 4, offsetY + width / 2);

        points[2][0] = new Point(offsetX + width / 4, offsetY + width - width / 4);
        points[2][1] = new Point(offsetX + width / 2, offsetY + width - width / 4);
        points[2][2] = new Point(offsetX + width - width / 4, offsetY + width - width / 4);

        //为点标识数字
        int index = 1;
        for (Point[] pointLists : points) {
            for (Point point : pointLists) {
                point.index = index;
                index++;
            }
        }
        isInit = true;
    }

    public void points2Canvas(Canvas canvas) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                paint.setStyle(Paint.Style.STROKE);   //空心
                paint.setStrokeWidth(px2dip(10));   //外框宽
                Point point = points[i][j];
                if (point.state == Point.STATE_NORMAL)   //正常圆
                {
                    paint.setColor(Color.parseColor("#00b285"));
                } else if (point.state == Point.STATE_PRESSED)   //按下圆
                {
                    paint.setColor(Color.parseColor("#b26216"));
                } else   //错误圆
                {
                    paint.setColor(Color.RED);
                }
                canvas.drawCircle(point.x, point.y, bitmapR, paint);
                paint.setStyle(Paint.Style.FILL);   //实心
                paint.setStrokeWidth(0);   //外框宽
                canvas.drawCircle(point.x, point.y, px2dip(12), paint);
            }
        }
    }

    /**
     * 画线
     *
     * @param canvas 画布
     * @param a      第一个点
     * @param b      第二个点
     */
    public void line2Canvas(Canvas canvas, Point a, Point b) {
        paint.setStyle(Paint.Style.FILL);   //空心
        paint.setStrokeWidth(px2dip(7));   //外框宽
        if (a.state == Point.STATE_PRESSED)   //正常线
        {
            paint.setColor(Color.parseColor("#b26216"));
        } else   //错误线
        {
            paint.setColor(Color.RED);
        }
        if (!isFinish)
            canvas.drawLine(a.x, a.y, b.x, b.y, paint);
        else {
            if (pointList.contains(b))
                canvas.drawLine(a.x, a.y, b.x, b.y, paint);
        }
    }

    /**
     * @return
     */
    public Point checkSelectPoint() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                if (Point.with(point.x, point.y, bitmapR, movingX, movingY)) {
                    return point;
                }

            }
        }
        return null;
    }

    /**
     * 交叉点检查
     *
     * @param p 点
     * @return
     */
    public boolean crossPoint(Point p) {
        if (pointList.contains(p))
            return true;
        else
            return false;
    }

    /**
     * 点对象
     */
    public static class Point {
        public static int STATE_NORMAL = 0;
        public static int STATE_PRESSED = 1;
        public static int STATE_ERROR = 2;

        public float x, y;
        public int state = 0, index = 0;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * 2点之间的距离
         *
         * @param a 点
         * @param b 点
         * @return 距离
         */
        public static double distance(Point a, Point b) {
            return Math.abs(a.x - b.x) * Math.abs(a.x - b.x) + Math.abs(a.y - b.y) * Math.abs(a.y - b.y);
        }

        /**
         * 是否重合
         *
         * @param pointX
         * @param pointY
         * @param r
         * @param movingX
         * @param movingY
         * @return 是否重合
         */
        public static boolean with(float pointX, float pointY, float r, float movingX, float movingY) {
            return Math.sqrt((pointX - movingX) * (pointX - movingX) + (pointY - movingY) * (pointY - movingY)) < r;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public interface OnTouchPointsListener {
        void onPointFinished(String pwd);

        void onStart(boolean is);
    }

    public void setOnTouchPointsListener(OnTouchPointsListener listener) {
        if (listener != null)
            this.listener = listener;
    }
}
