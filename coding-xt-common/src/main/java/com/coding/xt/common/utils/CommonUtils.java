package com.coding.xt.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static void main(String[] args) {
        System.out.println(CommonUtils.checkEmail("hsd@gmail.com"));
        for (int i = 1; i <= 1; i++) {
            System.out.println("---");
        }
        List<String> days = listDays("America/New_York", 7, "MM-dd");
        for (String day : days) {
            System.out.println(day);
        }
    }
    public static int random5Num(){
        return (int)((Math.random()*9+1)*10000);
    }
    public static String replaceContent(String content, String replace, String... values) {
        for (String v : values) {
            StringBuilder sb = new StringBuilder(content);
            int index = content.indexOf(replace);
            if (index == -1) {
                break;
            }
            int end = index + replace.length();
            sb = sb.replace(index, end, v);
            content = sb.toString();
        }
        return content;
    }

    private static Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$");

    public static boolean isMobile(String mobile) {
        if (StringUtils.isBlank(StringUtils.trim(mobile))) {
            return false;
        }
        boolean flag = false;
        try {
            Matcher m = p.matcher(mobile);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

//    public static String getRealIp(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
//    }

    public static String millisecond() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public static Date now0Date() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date convertDate(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(time);
    }
    public static Date convertDateyyyyMMdd(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(time);
    }

    public static String convertStringyyyyMMdd(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    public static String convertDate(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        return sdf.format(time);
    }

    public static String convertDateTime(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    public static String getBeforeTime(Long time) {
        long s = 60;
        long m = 60 * s;
        long h = 24 * m;
        long d = 30 * h;
        long mon = 12 * d;
        if (time == null) {
            return "0秒前";
        }
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - time;
        long seconds = diff / 1000;
        if (seconds < s) {
            return seconds + "秒前";
        }
        if (seconds < m) {
            return seconds / s + "分前";
        }
        if (seconds < h) {
            return seconds / m + "小时前";
        }
        if (seconds < d) {
            return seconds / h + "天前";
        }
        if (seconds < mon) {
            return seconds / d + "月前";
        }
        return "很久远之前";
    }

    public static String genScoreOrderTradeNo(Long userId) {
        return millisecond() + userId;
    }

    public static String genInviteCode(Long userId) {
        if (userId == null) {
            return null;
        }
        String x = Long.toHexString(userId);
        int length = x.length();
        if (length < 2) {
            x = "000" + x;
        } else if (length < 3) {
            x = "00" + x;
        } else if (length < 4) {
            x = "0" + x;
        }
        return x;
    }

    public static Long getUserIdByInviteCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        if (code.startsWith("000")) {
            code = code.substring(2);
        } else if (code.startsWith("00")) {
            code = code.substring(1);
        } else if (code.startsWith("0")) {
            code = code.substring(0);
        }
        return Long.parseLong(code, 16);
    }

    public static Date afterTime(Date currentTime, int add) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR, add);
        return calendar.getTime();
    }

    public static boolean isAllNum(String content) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }
        String c = content.trim();
        return StringUtils.isNumeric(c);
    }

    public static int getSecond(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        second = hour * 60 * 60 + minute * 60 + second;
        return second;
    }

    public static String md5(String value,String slat) {
        return DigestUtils.md5Hex(value);
    }

    public static List<Long> listBeforeDays(Date currentTime, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        List<Long> timeList = new ArrayList<>();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        timeList.add(calendar.getTimeInMillis());
        for (int n = 0; n <= day; n++) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            timeList.add(calendar.getTimeInMillis());
        }
        return timeList;
    }

    /**
     * 不支持跨2年以上
     *
     * @param firstTime
     * @param secondTime
     * @return
     */
    public static int diffDay(long firstTime, long secondTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(firstTime);
        int currentDay1 = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear1 = calendar.get(Calendar.YEAR);
        LocalDate localDate1 = LocalDate.ofYearDay(calendar.get(Calendar.YEAR), 1);
        int dayCount1 = localDate1.isLeapYear() ? 366 : 365;

        calendar.setTimeInMillis(secondTime);
        int currentDay2 = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear2 = calendar.get(Calendar.YEAR);
//        LocalDate localDate2 = LocalDate.ofYearDay(calendar.get(Calendar.YEAR), 1);
//        int dayCount2 = localDate2.isLeapYear() ? 366 : 365;
        int diff = currentYear1 - currentYear2;
        if (diff == 0) {
            return currentDay1 - currentDay2;
        }
        if (diff > 0) {
            return (diff * dayCount1) + currentDay1 - currentDay2;
        }
        return currentDay1 - ((diff * dayCount1) + currentDay2);
    }
    public static int diffDayTimeZone(String timeZone,long firstTime, long secondTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(firstTime);
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        int currentDay1 = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear1 = calendar.get(Calendar.YEAR);
        LocalDate localDate1 = LocalDate.ofYearDay(calendar.get(Calendar.YEAR), 1);
        int dayCount1 = localDate1.isLeapYear() ? 366 : 365;

        calendar.setTimeInMillis(secondTime);
        int currentDay2 = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear2 = calendar.get(Calendar.YEAR);
//        LocalDate localDate2 = LocalDate.ofYearDay(calendar.get(Calendar.YEAR), 1);
//        int dayCount2 = localDate2.isLeapYear() ? 366 : 365;
        int diff = currentYear1 - currentYear2;
        if (diff == 0) {
            return currentDay1 - currentDay2;
        }
        if (diff > 0) {
            return (diff * dayCount1) + currentDay1 - currentDay2;
        }
        return currentDay1 - ((diff * dayCount1) + currentDay2);
    }

    public static List<String> listDays(String timeZone, int add, String pattern) {
        List<String> dayList = new ArrayList<>();
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String timeZoneTime = sdf.format(Calendar.getInstance().getTime());
        Date date = null;
        try {
            date = sdf.parse(timeZoneTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dayList.add(formatTime(date, pattern));
            for (int i = 1; i < add; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                dayList.add(formatTime(calendar.getTime(), pattern));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayList;
    }

    public static List<String> listBeforeDays(String timeZone, int add, String pattern) {
        List<String> dayList = new ArrayList<>();
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String timeZoneTime = sdf.format(Calendar.getInstance().getTime());
        Date date = null;
        try {
            date = sdf.parse(timeZoneTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (add <= 0){
                return dayList;
            }
            calendar.add(Calendar.DAY_OF_YEAR,-add);
            dayList.add(formatTime(calendar.getTime(), pattern));
            for (int i = 1; i < add; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                dayList.add(formatTime(calendar.getTime(), pattern));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayList;
    }

    public static String timeZoneDateString(Long time,String timeZone, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf.format(time);
    }
    public static Date timeZoneDate(Long time, String timeZone, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        String format = sdf.format(new Date(time));
        try {
            return sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatTime(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatTime(String date, String datePattern, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        try {
            Date date1 = sdf.parse(date);
            SimpleDateFormat sdf1 = new SimpleDateFormat(pattern);
            return sdf1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isSameDayTimeZone(String timeZone, Long createTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(createTime));
        calendar1.setTimeZone(TimeZone.getTimeZone(timeZone));
        int year1  = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        return year == year1 && month == month1 && day == day1;
    }
    public static boolean isSameDayNoTimeZone(String timeZone, Long alreadyTimeZoneTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(alreadyTimeZoneTime));
        int year1  = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        return year == year1 && month == month1 && day == day1;
    }
    /**
     *检查Email 格式（正则表达式）
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        /*
         * " \w"：匹配字母、数字、下划线。等价于'[A-Za-z0-9_]'。
         * "|"  : 或的意思，就是二选一
         * "*" : 出现0次或者多次
         * "+" : 出现1次或者多次
         * "{n,m}" : 至少出现n个，最多出现m个
         * "$" : 以前面的字符结束
         */
        String REGEX="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,3}){1,3}$";
        Pattern p = Pattern.compile(REGEX);
        Matcher matcher=p.matcher(email);

        return matcher.matches();
    }


    /**
     * 截取第一处的中文数字字符串
     * @param str
     * @return
     */
    public static String getNumberStr(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isFirst = true;
        String chineseNumStr = "零一二三四五六七八九十百千万亿";
        for (int i = 0; i < str.length(); i++) {
            String tempStr = str.substring(i, i + 1);
            if (chineseNumStr.contains(tempStr)) {
                stringBuilder.append(tempStr);
                if (isFirst) {
                    isFirst = false;
                }
            } else {
                if (!isFirst) {
                    break;
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将中文数字转换为 阿拉伯数字
     * @param chineseNumber 中文数字字符串
     * @return 转换后的阿拉伯数字
     */
    public static long chineseNumber2Int(String chineseNumber) {
        String aval = "零一二三四五六七八九";
        String bval = "十百千万亿";
        int[] bnum = {10, 100, 1000, 10000, 100000000};
        long num = 0;
        char[] arr = chineseNumber.toCharArray();
        int len = arr.length;
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < len; i++) {
            char s = arr[i];
            //跳过零
            if (s == '零') continue;
            //用下标找到对应数字
            int index = bval.indexOf(s);
            //如果不在bval中，即当前字符为数字，直接入栈
            if (index == -1) {
                stack.push(aval.indexOf(s));
            } else { //当前字符为单位。
                int tempsum = 0;
                int val = bnum[index];
                //如果栈为空则直接入栈
                if (stack.isEmpty()) {
                    stack.push(val);
                    continue;
                }
                //如果栈中有比val小的元素则出栈，累加，乘N，再入栈
                while (!stack.isEmpty() && stack.peek() < val) {
                    tempsum += stack.pop();
                }
                //判断是否经过乘法处理
                if (tempsum == 0) {
                    stack.push(val);
                } else {
                    stack.push(tempsum * val);
                }
            }
        }
        //计算最终的和
        while (!stack.isEmpty()) {
            num += stack.pop();
        }
        return num;
    }
    }

