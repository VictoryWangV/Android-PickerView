package com.bigkoo.pickerview.view;

import android.content.Context;
import android.telecom.StatusHints;
import android.view.View;

import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.TimePickerView.Type;
import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class WheelTime {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    private WheelView wv_seconds;
    private int gravity;

    private Type type;
    public static final int DEFULT_START_YEAR = 1900;
    public static final int DEFULT_END_YEAR = 2100;
    public static final int DEFULT_START_MONTH = 1;
    public static final int DEFULT_END_MONTH = 12;
    public static final int DEFULT_START_DAY = 1;
    public static final int DEFULT_END_DAY = 31;
    private int startYear = DEFULT_START_YEAR;
    private int endYear = DEFULT_END_YEAR;
    private int startMonth = DEFULT_START_MONTH;
    private int endMonth = DEFULT_END_MONTH;
    private int startDay = DEFULT_START_DAY;
    private int endDay = DEFULT_END_DAY; //表示31天的
    private int currentYear;


    // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
    private int textSize = 18;
    //文字的颜色和分割线的颜色
    int textColorOut;
    int textColorCenter;
    int dividerColor;
    // 条目间距倍数
    float lineSpacingMultiplier = 1.6F;

    public WheelTime(View view) {
        super();
        this.view = view;
        type = Type.ALL;
        setView(view);
    }

    public WheelTime(View view, Type type, int gravity, int textSize) {
        super();
        this.view = view;
        this.type = type;
        this.gravity = gravity;
        this.textSize = textSize;
        setView(view);
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0, 0);
    }

    public void setPicker(int year, final int month, int day, int h, int m, int s) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        Context context = view.getContext();
        currentYear = year;
        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
        wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字
        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据
        wv_year.setGravity(gravity);
        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        if (year == startYear) {

            wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
            wv_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == endYear) {
            wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
            wv_month.setCurrentItem(month);
        } else {
            wv_month.setAdapter(new NumericWheelAdapter(1, 12));
            wv_month.setCurrentItem(month);
        }
        wv_month.setLabel(context.getString(R.string.pickerview_month));

        wv_month.setGravity(gravity);
        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);

        if (year == startYear && month + 1 == startMonth) {
// 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_day.setAdapter(new NumericWheelAdapter(startDay, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_day.setAdapter(new NumericWheelAdapter(startDay, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_day.setAdapter(new NumericWheelAdapter(startDay, 29));
                } else {

                    wv_day.setAdapter(new NumericWheelAdapter(startDay, 28));
                }
            }
            wv_day.setCurrentItem(day - startDay);
        } else if (year == endYear && month + 1 == endMonth) {

// 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                }
            }
            wv_day.setCurrentItem(day - 1);
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                } else {

                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
            wv_day.setCurrentItem(day - 1);
        }

        wv_day.setLabel(context.getString(R.string.pickerview_day));

        wv_day.setGravity(gravity);
        //时
        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setLabel(context.getString(R.string.pickerview_hours));// 添加文字
        wv_hours.setCurrentItem(h);
        wv_hours.setGravity(gravity);
        //分
        wv_mins = (WheelView) view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        wv_mins.setLabel(context.getString(R.string.pickerview_minutes));// 添加文字
        wv_mins.setCurrentItem(m);
        wv_mins.setGravity(gravity);
        //秒
        wv_seconds = (WheelView) view.findViewById(R.id.second);
        wv_seconds.setAdapter(new NumericWheelAdapter(0, 59));
        wv_seconds.setLabel(context.getString(R.string.pickerview_seconds));// 添加文字
        wv_seconds.setCurrentItem(s);
        wv_seconds.setGravity(gravity);

        // 添加"年"监听
        OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + startYear;
                currentYear = year_num;
                System.out.println("startmonth:" + startMonth);
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (year_num == startYear) {//等于开始的年
                    //重新设置月份

                    wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
                    wv_month.setCurrentItem(0);
                    int monthNum = wv_month.getCurrentItem() + startMonth;

                    if (monthNum == startMonth) {
                        //重新设置日
                        int maxItem;
                        if (list_big
                                .contains(String.valueOf(monthNum))) {

                            wv_day.setAdapter(new NumericWheelAdapter(startDay, 31));
                            maxItem = 31;
                        } else if (list_little.contains(String.valueOf(monthNum))) {

                            wv_day.setAdapter(new NumericWheelAdapter(startDay, 30));
                            maxItem = 30;
                        } else {
                            if ((year_num % 4 == 0 && year_num % 100 != 0)
                                    || year_num % 400 == 0) {

                                wv_day.setAdapter(new NumericWheelAdapter(startDay, 29));
                                maxItem = 29;
                            } else {

                                wv_day.setAdapter(new NumericWheelAdapter(startDay, 28));
                                maxItem = 28;
                            }
                        }

                            wv_day.setCurrentItem(0);

                    } else {
                        int maxItem;
                        if (list_big
                                .contains(String.valueOf(monthNum))) {

                            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                            maxItem = 31;
                        } else if (list_little.contains(String.valueOf(monthNum))) {

                            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                            maxItem = 30;
                        } else {
                            if ((year_num % 4 == 0 && year_num % 100 != 0)
                                    || year_num % 400 == 0) {

                                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                                maxItem = 29;
                            } else {

                                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                                maxItem = 28;
                            }
                        }

                            wv_day.setCurrentItem(0);

                    }


                } else if (year_num == endYear) {
                    //重新设置月份
                    wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
                    wv_month.setCurrentItem(0);
                    int monthNum = wv_month.getCurrentItem() + 1;

                    if (monthNum == endMonth) {
                        int maxItem;
                        if (list_big
                                .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                            if (endDay > 31) {
                                endDay = 31;
                            }
                            wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                            maxItem = endDay;
                        } else if (list_little.contains(String.valueOf(wv_month
                                .getCurrentItem() + 1))) {
                            if (endDay > 30) {
                                endDay = 30;
                            }
                            wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                            maxItem = endDay;
                        } else {
                            if ((year_num % 4 == 0 && year_num % 100 != 0)
                                    || year_num % 400 == 0) {
                                if (endDay > 29) {
                                    endDay = 29;
                                }
                                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                                maxItem = endDay;
                            } else {
                                if (endDay > 28) {
                                    endDay = 28;
                                }
                                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                                maxItem = endDay;
                            }
                        }

                            wv_day.setCurrentItem(0);

                    } else {

                        int maxItem;
                        if (list_big
                                .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {

                            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                            maxItem = 31;
                        } else if (list_little.contains(String.valueOf(wv_month
                                .getCurrentItem() + 1))) {

                            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                            maxItem = 30;
                        } else {
                            if ((year_num % 4 == 0 && year_num % 100 != 0)
                                    || year_num % 400 == 0) {

                                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                                maxItem = 29;
                            } else {

                                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                                maxItem = 28;
                            }
                        }

                            wv_day.setCurrentItem(0);

                    }

                } else {
                    //重新设置月份
                    wv_month.setAdapter(new NumericWheelAdapter(1, 12));
                    wv_month.setCurrentItem(0);
                    int maxItem;
                    if (list_big
                            .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {

                        wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                        maxItem = 31;
                    } else if (list_little.contains(String.valueOf(wv_month
                            .getCurrentItem() + 1))) {

                        wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                        maxItem = 30;
                    } else {
                        if ((year_num % 4 == 0 && year_num % 100 != 0)
                                || year_num % 400 == 0) {

                            wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                            maxItem = 29;
                        } else {

                            wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                            maxItem = 28;
                        }
                    }

                        wv_day.setCurrentItem(0);

                }


            }
        };
        // 添加"月"监听
        OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;


                if (currentYear == startYear) {
                    month_num = month_num + startMonth - 1;

                    int maxItem = 30;

                    // 判断大小月及是否闰年,用来确定"日"的数据
                    if (list_big.contains(String.valueOf(month_num))) {

                        wv_day.setAdapter(new NumericWheelAdapter(startDay, 31));
                        maxItem = 31;
                    } else if (list_little.contains(String.valueOf(month_num))) {

                        wv_day.setAdapter(new NumericWheelAdapter(startDay, 30));
                        maxItem = 30;
                    } else {
                        if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
                                .getCurrentItem() + startYear) % 100 != 0)
                                || (wv_year.getCurrentItem() + startYear) % 400 == 0) {

                            wv_day.setAdapter(new NumericWheelAdapter(startDay, 29));
                            maxItem = 29;
                        } else {

                            wv_day.setAdapter(new NumericWheelAdapter(startDay, 28));
                            maxItem = 28;
                        }
                    }

                        wv_day.setCurrentItem(0);

                } else if (currentYear == endYear && month_num == endMonth) {
                    int maxItem;
                    if (list_big
                            .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                        if (endDay > 31) {
                            endDay = 31;
                        }
                        wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                        maxItem = endDay;
                    } else if (list_little.contains(String.valueOf(wv_month
                            .getCurrentItem() + 1))) {
                        if (endDay > 30) {
                            endDay = 30;
                        }
                        wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                        maxItem = endDay;
                    } else {
                        if ((currentYear % 4 == 0 && currentYear % 100 != 0)
                                || currentYear % 400 == 0) {
                            if (endDay > 29) {
                                endDay = 29;
                            }
                            wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                            maxItem = endDay;
                        } else {
                            if (endDay > 28) {
                                endDay = 28;
                            }
                            wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                            maxItem = endDay;
                        }
                    }

                        wv_day.setCurrentItem(0);

                } else {
                    int maxItem = 30;
                    // 判断大小月及是否闰年,用来确定"日"的数据
                    if (list_big.contains(String.valueOf(month_num))) {

                        wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                        maxItem = 31;
                    } else if (list_little.contains(String.valueOf(month_num))) {

                        wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                        maxItem = 30;
                    } else {
                        if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
                                .getCurrentItem() + startYear) % 100 != 0)
                                || (wv_year.getCurrentItem() + startYear) % 400 == 0) {

                            wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                            maxItem = 29;
                        } else {

                            wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                            maxItem = 28;
                        }
                    }

                        wv_day.setCurrentItem(0);

                }


            }
        };
        wv_year.setOnItemSelectedListener(wheelListener_year);
        wv_month.setOnItemSelectedListener(wheelListener_month);

        switch (type) {
            case ALL:
               /* textSize = textSize * 3;*/
                break;
            case YEAR_MONTH_DAY:
               /* textSize = textSize * 4;*/
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                wv_seconds.setVisibility(View.GONE);
                break;
            case HOURS_MINS:
                /*textSize = textSize * 4;*/
                wv_year.setVisibility(View.GONE);
                wv_month.setVisibility(View.GONE);
                wv_day.setVisibility(View.GONE);
                wv_seconds.setVisibility(View.GONE);
                break;
            case MONTH_DAY_HOUR_MIN:
               /* textSize = textSize * 3;*/
                wv_year.setVisibility(View.GONE);
                wv_seconds.setVisibility(View.GONE);
                break;
            case YEAR_MONTH:
               /* textSize = textSize * 4;*/
                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                wv_seconds.setVisibility(View.GONE);
            case YEAR_MONTH_DAY_HOUR_MIN:
               /* textSize = textSize * 4;*/

                wv_seconds.setVisibility(View.GONE);
        }
        setContentTextSize();
    }


    private void setContentTextSize() {
        wv_day.setTextSize(textSize);
        wv_month.setTextSize(textSize);
        wv_year.setTextSize(textSize);
        wv_hours.setTextSize(textSize);
        wv_mins.setTextSize(textSize);
        wv_seconds.setTextSize(textSize);
    }

    private void setTextColorOut() {
        wv_day.setTextColorOut(textColorOut);
        wv_month.setTextColorOut(textColorOut);
        wv_year.setTextColorOut(textColorOut);
        wv_hours.setTextColorOut(textColorOut);
        wv_mins.setTextColorOut(textColorOut);
        wv_seconds.setTextColorOut(textColorOut);
    }

    private void setTextColorCenter() {
        wv_day.setTextColorCenter(textColorCenter);
        wv_month.setTextColorCenter(textColorCenter);
        wv_year.setTextColorCenter(textColorCenter);
        wv_hours.setTextColorCenter(textColorCenter);
        wv_mins.setTextColorCenter(textColorCenter);
        wv_seconds.setTextColorCenter(textColorCenter);
    }

    private void setDividerColor() {
        wv_day.setDividerColor(dividerColor);
        wv_month.setDividerColor(dividerColor);
        wv_year.setDividerColor(dividerColor);
        wv_hours.setDividerColor(dividerColor);
        wv_mins.setDividerColor(dividerColor);
        wv_seconds.setDividerColor(dividerColor);
    }

    private void setLineSpacingMultiplier() {
        wv_day.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_month.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_year.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_hours.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_mins.setLineSpacingMultiplier(lineSpacingMultiplier);
        wv_seconds.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    public void setLabels(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        if (label_year != null)
            wv_year.setLabel(label_year);
        if (label_month != null)
            wv_month.setLabel(label_month);
        if (label_day != null)
            wv_day.setLabel(label_day);
        if (label_hours != null)
            wv_hours.setLabel(label_hours);
        if (label_mins != null)
            wv_mins.setLabel(label_mins);
        if (label_seconds != null)
            wv_seconds.setLabel(label_seconds);
    }


    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_year.setCyclic(cyclic);
        wv_month.setCyclic(cyclic);
        wv_day.setCyclic(cyclic);
        wv_hours.setCyclic(cyclic);
        wv_mins.setCyclic(cyclic);
        wv_seconds.setCyclic(cyclic);
    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        if (currentYear == startYear) {

            sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                    .append((wv_month.getCurrentItem() + startMonth)).append("-")
                    .append((wv_day.getCurrentItem() + startDay)).append(" ")
                    .append(wv_hours.getCurrentItem()).append(":")
                    .append(wv_mins.getCurrentItem()).append(":")
                    .append(wv_seconds.getCurrentItem());


        } else {
            sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                    .append((wv_month.getCurrentItem() + 1)).append("-")
                    .append((wv_day.getCurrentItem() + 1)).append(" ")
                    .append(wv_hours.getCurrentItem()).append(":")
                    .append(wv_mins.getCurrentItem()).append(":")
                    .append(wv_seconds.getCurrentItem());
        }

        return sb.toString();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }


    public void setRange2(Date startDate, Date endDate) {
        if (startDate == null && endDate != null) {
            if (endDate.getYear() > startYear) {
                this.endYear = endDate.getYear();
                this.endMonth = endDate.getMonth();
                this.endDay = endDate.getDate();
            } else if (endDate.getYear() == startYear) {
                if (endDate.getMonth() > startMonth) {
                    this.endYear = endDate.getYear();
                    this.endMonth = endDate.getMonth();
                    this.endDay = endDate.getDate();
                } else if (endDate.getMonth() == startMonth) {
                    if (endDate.getDate() > startDay) {
                        this.endYear = endDate.getYear();
                        this.endMonth = endDate.getMonth();
                        this.endDay = endDate.getDate();
                    }
                }
            }
            System.out.println("startnull");

        } else if (startDate != null && endDate == null) {
            if (startDate.getYear() < endYear) {
                this.startMonth = startDate.getMonth();
                this.startDay = startDate.getDate();
                this.startYear = startDate.getYear();
            } else if (startDate.getYear() == endYear) {
                if (startDate.getMonth() < endMonth) {
                    this.startMonth = startDate.getMonth();
                    this.startDay = startDate.getDate();
                    this.startYear = startDate.getYear();
                } else if ((startDate.getMonth() == endMonth)) {
                    if (startDate.getDate() < endDay) {
                        this.startMonth = startDate.getMonth();
                        this.startDay = startDate.getDate();
                        this.startYear = startDate.getYear();
                    }
                }
            }
            System.out.println("endnull");

        } else if (startDate != null && endDate != null) {
            this.startYear = startDate.getYear();
            this.endYear = endDate.getYear();
            this.startMonth = startDate.getMonth();
            this.endMonth = endDate.getMonth();
            this.startDay = startDate.getDate();
            this.endDay = endDate.getDate();


        }


    }


    /**
     * 设置间距倍数,但是只能在1.0-2.0f之间
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }

    /**
     * 设置分割线的颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        setDividerColor();
    }

    /**
     * 设置分割线之间的文字的颜色
     *
     * @param textColorCenter
     */
    public void setTextColorCenter(int textColorCenter) {
        this.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    /**
     * 设置分割线以外文字的颜色
     *
     * @param textColorOut
     */
    public void setTextColorOut(int textColorOut) {
        this.textColorOut = textColorOut;
        setTextColorOut();
    }
}
