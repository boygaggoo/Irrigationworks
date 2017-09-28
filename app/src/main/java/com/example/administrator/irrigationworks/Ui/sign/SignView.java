package com.example.administrator.irrigationworks.Ui.sign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Ui.activity.ChitemSiteActivity;
import com.example.administrator.irrigationworks.Ui.bean.SignViewDataLitepal;
import com.example.administrator.irrigationworks.Ui.bean.TaskChechItembean;
import com.example.administrator.irrigationworks.Ui.bean.TotalDemo;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 签到日历控件
 * Created by E.M on 2016/4/20.
 */
public class SignView extends View {
    private static final String[] WEEK_MARK = {"一", "二", "三", "四", "五", "六", "日"};

    private static final int MAX_COLUMN = 7;
    /**
     * 周内
     */
    private static final int COLOR_MARKER_WEEKDAY = 0xFF999999;
    private static final int COLOR_MARKER_WEEKEND = 0xFF1B89CD;
    /**
     * 已签到背景色
     */
    private static final int COLOR_BACKGROUND_HIGHLIGHT = 0xFF1B89CD;
    /**
     * 未签到背景色
     */
    private static final int COLOR_BACKGROUND_NORMAL = 0xFF9C9C9C;
    /**
     * 等待签到背景色
     */
    private static final int COLOR_BACKGROUND_WAIT = 0xFFFE7471;
    /**
     * 已签到文字颜色
     */
    private static final int COLOR_TEXT_HIGHLIGHT = 0xFF0045FF;
//    private static final int COLOR_TEXT_HIGHLIGHT = 0xFFFFFFFF;
    /**
     * 未签到文字颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0xFF606060;
//    /**
//     * 不可用文字颜色
//     */
//    private static final int COLOR_TEXT_DISABLED = 0xFFD4D4D4;

    private static final int MARKER_TEXT_SIZE = 40;
    private static final int CELL_TEXT_SIZE = 40;

    private static final int VERTICAL_SPACE = 51;
    private static final int VERTICAL_MARGIN = 62;
    private static final int HORIZONTAL_MARGIN = 39;
    private static final int CELL_SIZE = 80;
    private static final int WAIT_LINE_SIZE = 14;

    private int dayOfMonthToday;
    private int markerTextY;
    private int verticalCellTop;
    private int sumDayOfMonth;
    private int daysOfFirstWeek;
    private int horizontalSpace;
    private int deltaTextCellY;
    private int deltaTextMarkerY;

    private int verticalSpace;
    private int verticalMargin;
    private int horizontalMargin;
    private int cellSize;
    private int waitLineSize;

    private Path waitPath;
    private Rect waitRect;
    private Paint paintWeekday;
    private Paint paintWeekend;
    private Paint paintTextNormal;
    private Paint paintTextHighlight;
    private Paint paintBackgroundWait;
    private Paint paintBackgroundNormal;
    private Paint paintBackgroundHighlight;
    private CalendarAdapter adapter;
    private ResolutionUtil resolutionUtil;

    public SignView(Context context) {
        this(context, null);
    }

    public SignView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SignView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResolution();
        initPaint();
        initData();
    }

    private void initResolution() {
        resolutionUtil = ResolutionUtil.getInstance();
        verticalSpace = resolutionUtil.formatVertical(VERTICAL_SPACE);
        verticalMargin = resolutionUtil.formatVertical(VERTICAL_MARGIN);
        horizontalMargin = resolutionUtil.formatHorizontal(HORIZONTAL_MARGIN);
        cellSize = resolutionUtil.formatVertical(CELL_SIZE);
        waitLineSize = resolutionUtil.formatVertical(WAIT_LINE_SIZE);
    }

    private void initPaint() {
        int markerTextSize = resolutionUtil.formatVertical(MARKER_TEXT_SIZE);
        int cellTextSize = resolutionUtil.formatVertical(CELL_TEXT_SIZE);

        paintWeekday = new Paint();
        paintWeekday.setAntiAlias(true);
        paintWeekday.setColor(COLOR_MARKER_WEEKDAY);
        paintWeekday.setTextSize(markerTextSize);
        paintWeekday.setTextAlign(Paint.Align.CENTER);

        paintWeekend = new Paint();
        paintWeekend.setAntiAlias(true);
        paintWeekend.setColor(COLOR_MARKER_WEEKEND);
        paintWeekend.setTextSize(markerTextSize);
        paintWeekend.setTextAlign(Paint.Align.CENTER);

        paintTextNormal = new Paint();
        paintTextNormal.setAntiAlias(true);
        paintTextNormal.setColor(COLOR_TEXT_NORMAL);
        paintTextNormal.setTextSize(cellTextSize);
        paintTextNormal.setTextAlign(Paint.Align.CENTER);

        paintTextHighlight = new Paint();
        paintTextHighlight.setAntiAlias(true);
        paintTextHighlight.setColor(COLOR_TEXT_HIGHLIGHT);
        paintTextHighlight.setTextSize(cellTextSize);
        paintTextHighlight.setTextAlign(Paint.Align.CENTER);

        paintBackgroundWait = new Paint();
        paintBackgroundWait.setAntiAlias(true);
        paintBackgroundWait.setColor(COLOR_BACKGROUND_WAIT);
        paintBackgroundWait.setStrokeWidth(2);
        paintBackgroundWait.setStyle(Paint.Style.STROKE);

        paintBackgroundNormal = new Paint();
        paintBackgroundNormal.setAntiAlias(true);
        paintBackgroundNormal.setColor(COLOR_BACKGROUND_NORMAL);
        paintBackgroundNormal.setStrokeWidth(2);
        paintBackgroundNormal.setStyle(Paint.Style.STROKE);

        paintBackgroundHighlight = new Paint();
        paintBackgroundHighlight.setAntiAlias(true);
        paintBackgroundHighlight.setColor(COLOR_BACKGROUND_HIGHLIGHT);
        paintBackgroundHighlight.setStrokeWidth(2);
        paintBackgroundHighlight.setStyle(Paint.Style.FILL);
    }

    private void initData() {
        Paint.FontMetricsInt fmiMarker = paintWeekday.getFontMetricsInt();
        deltaTextMarkerY = -(fmiMarker.bottom - fmiMarker.top) / 2 - fmiMarker.top;
        markerTextY = verticalMargin + cellSize / 2;

        Paint.FontMetricsInt fmiCell = paintTextNormal.getFontMetricsInt();
        deltaTextCellY = -(fmiCell.bottom - fmiCell.top) / 2 - fmiCell.top;
        verticalCellTop = verticalMargin + cellSize;

        Calendar calendarToday = Calendar.getInstance();
        dayOfMonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek;
        sumDayOfMonth = calendarToday.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar calendarFirstDay = Calendar.getInstance();
        calendarFirstDay.set(Calendar.DAY_OF_MONTH, 1);
        dayOfWeek = calendarFirstDay.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            dayOfWeek = 7;
        } else {
            dayOfWeek = dayOfWeek - 1;
        }
        daysOfFirstWeek = MAX_COLUMN - dayOfWeek + 1;
    }

    private void createWaitBackground(int topX, int topY) {
        waitPath = new Path();
        waitPath.moveTo(topX, topY + waitLineSize);
        waitPath.lineTo(topX, topY);
        waitPath.lineTo(topX + waitLineSize, topY);

        waitPath.moveTo(topX + cellSize - waitLineSize, topY + cellSize);
        waitPath.lineTo(topX + cellSize, topY + cellSize);
        waitPath.lineTo(topX + cellSize, topY + cellSize - waitLineSize);

        waitRect = new Rect(topX, topY, topX + cellSize, topY + cellSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        horizontalSpace = (w - MAX_COLUMN * cellSize - horizontalMargin * 2) / (MAX_COLUMN - 1);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawWeekMark(canvas);
        drawCellsBackground(canvas);
        drawCells(canvas);
    }

    private void drawWeekMark(Canvas canvas) {
        int y = markerTextY + deltaTextMarkerY;
        for (int i = 0; i < 7; i++) {
            int x = horizontalMargin + i * (horizontalSpace + cellSize)
                    + cellSize / 2;
            if (i < 5) {
                canvas.drawText(WEEK_MARK[i], x, y, paintWeekday);
            } else {
                canvas.drawText(WEEK_MARK[i], x, y, paintWeekend);
            }
        }
    }

    private void drawCellsBackground(Canvas canvas) {
        for (int i = 1; i <= dayOfMonthToday; i++) {
            drawCellBackground(canvas, i, getColumnIndex(i), getRowIndex(i));
        }
    }

    /**
     * 根据行列序号绘制日期背景
     *
     * @param canvas     画布
     * @param dayOfMonth 日期
     * @param column     列序号
     * @param row        行序号
     */
    private void drawCellBackground(Canvas canvas, int dayOfMonth, int column, int row) {
        int x = horizontalMargin + column * (horizontalSpace + cellSize)
                + cellSize / 2;
        int y = verticalCellTop + verticalSpace * (row + 1) + cellSize * row + cellSize / 2;
        if (adapter != null) {
            DayType dayType = adapter.getType(dayOfMonth);
            switch (dayType) {
                case WAITING:
                    if (waitPath == null) {
                        createWaitBackground(x - cellSize / 2, y - cellSize / 2);
                    }
                    canvas.drawPath(waitPath, paintBackgroundWait);
                    break;
                case SIGNED:
                    canvas.drawCircle(x, y, cellSize / 2, paintBackgroundHighlight);
                    break;
                default:
                    canvas.drawCircle(x, y, cellSize / 2, paintBackgroundNormal);
                    break;
            }
        } else {
            canvas.drawCircle(x, y, cellSize / 2, paintBackgroundNormal);
        }
    }

    private void drawCells(Canvas canvas) {
        for (int i = 1; i <= sumDayOfMonth; i++) {
            drawCell(canvas, i, getColumnIndex(i), getRowIndex(i));
        }
    }

    /**
     * 根据行列序号绘制日期
     *
     * @param canvas     画布
     * @param dayOfMonth 日期
     * @param column     列序号
     * @param row        行序号
     */
    private void drawCell(Canvas canvas, int dayOfMonth, int column, int row) {
        int x = horizontalMargin + column * (horizontalSpace + cellSize)
                + cellSize / 2;
        int y = verticalCellTop + verticalSpace * (row + 1) + cellSize * row + cellSize / 2
                + deltaTextCellY;
        if (adapter != null && dayOfMonth <= dayOfMonthToday) {
            DayType dayType = adapter.getType(dayOfMonth);
            Paint paint;
            switch (dayType) {
                case SIGNED:
                    paint = paintTextHighlight;
                    break;
                default:
                    paint = paintTextNormal;
                    break;
            }
            canvas.drawText(String.valueOf(dayOfMonth), x, y, paint);
        } else {
            canvas.drawText(String.valueOf(dayOfMonth), x, y, paintTextNormal);
        }
    }

    /**
     * 获取列序号
     *
     * @param dayOfMonth 日期
     * @return 列序号
     */
    private int getColumnIndex(int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            dayOfWeek = 6;
        } else {
            dayOfWeek = dayOfWeek - 2;
        }
        return dayOfWeek;
    }

    /**
     * 获取行序号
     *
     * @param dayOfMonth 日期
     * @return 行序号
     */
    private int getRowIndex(int dayOfMonth) {
        float weight = (dayOfMonth - daysOfFirstWeek) / (MAX_COLUMN * 1f);
        double rowIndexDouble = Math.abs(Math.ceil(weight));
        return (int) rowIndexDouble;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getDta();
//            float x = event.getX();
//            float y = event.getY();
//            if (waitPath != null) {
//                if (adapter.getType(dayOfMonthToday).equals(DayType.WAITING)) {
//                    if (x >= waitRect.left && y >= waitRect.top && x <= waitRect.right && y <= waitRect.bottom) {
//                        if (onTodayClickListener != null) {
//                            onTodayClickListener.onTodayClick();
//                        }
//                    }
//                }
//            }
        }
        return true;
    }

    private void getDta() {
        int positon = 0;
        TotalDemo totalDemo = NimUIKit.getGson().fromJson(poistoricaDataLitepal.get(0).getCheckitem(), TotalDemo.class);
        Log.d("oop", "key为TaskListBean：" + totalDemo);
        Map mapTypes1 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName1()));
        Map mapTypes2 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName2()));
        Map mapTypes3 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName3()));
        Map mapTypes4 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName4()));
        Map mapTypes5 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName6()));
        Map mapTypes6 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getValue5()));
        Map mapTypes7 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName7()));
        Map mapTypes8 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName8()));
        Map mapTypes9 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName9()));
        Map mapTypes10 = JSON.parseObject(NimUIKit.getGson().toJson(totalDemo.getName10()));
        Log.d("oop", "key为mapTypes1：" + mapTypes1);
        Log.d("oop", "key为mapTypes2：" + mapTypes2);
        Log.d("oop", "key为mapTypes3：" + mapTypes3);
        Log.d("oop", "key为mapTypes5：" + mapTypes5);
        Log.d("oop", "key为mapTypes4：" + mapTypes4);
        Log.d("oop", "key为mapTypes6：" + mapTypes6);
        Log.d("oop", "key为mapTypes7：" + mapTypes7);
        Log.d("oop", "key为mapTypes8：" + mapTypes8);
        Log.d("oop", "key为mapTypes9：" + mapTypes9);
        Log.d("oop", "key为mapTypes10：" + mapTypes10);
//                        if (obj.has("checkitem")) {
//                            JSONObject transitListArray = obj.getJSONObject("checkitem");
//                            for (int i = 0; i < transitListArray.length(); i++) {
//                                Log.d("oop","历史数据"+transitListArray);
//                            }
//                        }
        Log.d("oop", "key为：" + taskChechItembean);
        for (Object obj : mapTypes1.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes1.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes2.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes2.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes3.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes3.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
//        for (Object obj : mapTypes4.keySet()) {
//            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
//            taskChechItembean1.setKeys(obj + "");
//            taskChechItembean1.setObjs(mapTypes4.get(obj) + "");
//            taskChechItembean.add(taskChechItembean1);//获取当前的值
//        }
        for (Object obj : mapTypes5.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes5.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes6.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes6.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes7.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes7.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes8.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes8.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes9.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes9.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        for (Object obj : mapTypes10.keySet()) {
            TaskChechItembean taskChechItembean1 = new TaskChechItembean();
            taskChechItembean1.setKeys(obj + "");
            taskChechItembean1.setObjs(mapTypes10.get(obj) + "");
            taskChechItembean.add(taskChechItembean1);//获取当前的值
        }
        Intent intentnotess = new Intent(acit, ChitemSiteActivity.class);
        intentnotess.putExtra("chitems", NimUIKit.getGson().toJson(taskChechItembean));
        acit.startActivity(intentnotess);
    }

    private List<Integer> daylist;
    private List<SignViewDataLitepal> poistoricaDataLitepal;
    private List<TaskChechItembean> taskChechItembean;
    private SignActivity acit;
    public void setAdapter(SignActivity acit,CalendarAdapter adapter,  List<Integer> daylist,List<SignViewDataLitepal> poistoricaDataLitepal,List<TaskChechItembean> taskChechItembean) {
        this.adapter = adapter;
        this.daylist = daylist;
        this.poistoricaDataLitepal = poistoricaDataLitepal;
        this.taskChechItembean = taskChechItembean;
        this.acit = acit;
        this.invalidate();
    }

    public int getDayOfMonthToday() {
        return dayOfMonthToday;
    }

    public void notifyDataSetChanged() {
        invalidate();
    }

    private OnTodayClickListener onTodayClickListener;

    public void setOnTodayClickListener(OnTodayClickListener onTodayClickListener) {
        this.onTodayClickListener = onTodayClickListener;
    }

    public interface OnTodayClickListener {
        void onTodayClick();
    }

    public enum DayType {
        /**
         * 已签到状态，时间已过
         */
        SIGNED(0),
        /**
         * 未签到状态，时间已过
         */
        UNSIGNED(1),
        /**
         * 等待状态，即当日还未签到
         */
        WAITING(2),
        /**
         * 不可达到状态，未到时间
         */
        UNREACHABLE(3),
        /**
         * 不可用状态，非当前月份
         */
        DISABLED(4);

        private int value;

        DayType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static DayType valueOf(int value) {
            switch (value) {
                case 0:
                    return SIGNED;
                case 1:
                    return UNSIGNED;
                case 2:
                    return WAITING;
                case 3:
                    return UNREACHABLE;
                case 4:
                    return DISABLED;
                default:
                    return DISABLED;
            }
        }
    }
}
