package com.est.app.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@SuppressWarnings("Duplicates")
public class TimeUtils {

    /**
     * Cached local time zone.
     */
    private static volatile TimeZone timeZone;

    /**
     * Observed JVM behaviour is that if the timezone of the host computer is
     * changed while the JVM is running, the zone offset does not change but
     * keeps the initial value. So it is correct to measure this once and use
     * this value throughout the JVM's lifecycle. In any case, it is safer to
     * use a fixed value throughout the duration of the JVM's life, rather than
     * have this offset change, possibly midway through a long-running query.
     */
    private static int zoneOffsetMillis = createGregorianCalendar().get(Calendar.ZONE_OFFSET);

    /**
     * Creates a Gregorian calendar for the default timezone using the default
     * locale. Dates in H2 are represented in a Gregorian calendar. So this
     * method should be used instead of Calendar.getInstance() to ensure that
     * the Gregorian calendar is used for all date processing instead of a
     * default locale calendar that can be non-Gregorian in some locales.
     *
     * @return a new calendar instance.
     */
    public static GregorianCalendar createGregorianCalendar() {
        return new GregorianCalendar();
    }

    /**
     * Allocates a <code>Date</code> object and initializes it to
     * represent the specified number of milliseconds since the
     * standard base time known as "the epoch", namely January 1,
     * 1970, 00:00:00 GMT.
     *
     * @param   now   the milliseconds since January 1, 1970, 00:00:00 GMT.
     * @see     System#currentTimeMillis()
     */
    public static long computeStartOfNextSecond(long now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.SECOND, 1);
        return cal.getTime().getTime();
    }

    public static long computeStartOfNextMinute(long now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MINUTE, 1);
        return cal.getTime().getTime();
    }

    public static long computeStartOfNextHour(long now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.add(Calendar.HOUR, 1);
        return cal.getTime().getTime();
    }

    public static long computeStartOfNextDay(long now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));

        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime().getTime();
    }

    public static long computeStartOfNextWeek(long now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        return cal.getTime().getTime();
    }

    public static long computeStartOfNextMonth(long now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));

        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime().getTime();
    }
    /**
     * Adds or subtracts the specified amount of time to the given calendar field,
     * based on the calendar's rules. For example, to subtract 5 days from
     * the current time of the calendar, you can achieve it by calling:
     * <p><code>computeStartOf(-5)</code>.
     *
     * @param amount the amount of date or time to be added to the field.
     */
    public static long computeStartOfSecond(int amount) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.SECOND, amount);
        return cal.getTime().getTime();
    }

    public static long computeStartOfMinute(int amount) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MINUTE, amount);
        return cal.getTime().getTime();
    }

    public static long computeStartOfMinute(long now, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MINUTE, amount);
        return cal.getTime().getTime();
    }

    public static long computeStartOfHour(int amount) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.add(Calendar.HOUR, amount);
        return cal.getTime().getTime();
    }

    public static long computeStartOfHour(long now, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.add(Calendar.HOUR, amount);
        return cal.getTime().getTime();
    }

    public static long computeStartOfDay(int amount) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, amount);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime().getTime();
    }

    public static long computeStartOfDay(long now, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));

        cal.add(Calendar.DAY_OF_MONTH, amount);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime().getTime();
    }

    public static long computeStartOfWeek(long now, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, amount);
        return cal.getTime().getTime();
    }

    public static long computeStartOfWeek(int amount) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, amount);
        return cal.getTime().getTime();
    }

    public static long computeStartOfMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, month);
        return cal.getTime().getTime();
    }

    public static int getMinute(long now){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));
        return cal.get(Calendar.MINUTE); //
    }

    public static int getHour(long now){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));
        return cal.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        //return cal.get(Calendar.HOUR);        // gets hour in 12h format
    }

    public static int getDay(long now){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(now));
        return cal.get(Calendar.DAY_OF_WEEK); // gets hour in 24h format
        //return cal.get(Calendar.HOUR);        // gets hour in 12h format
    }

    public static long currentHourInMs(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime();
    }

    public static long currentTimeMillis(){
        //System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime();
    }

    public static String getCurrentTime(){
        Calendar cal = Calendar.getInstance();
        return getTimeFormat(cal.getTime().getTime());
    }

    public static String getCurrentTimeShort(){
        Calendar cal = Calendar.getInstance();
        return getTimeFormatShort(cal.getTime().getTime());
    }

    public static String getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        return getTimeFormatDate(cal.getTime().getTime());
    }

    public static String getCurrentHour(){
        Calendar cal = Calendar.getInstance();
        return getTimeFormatHour(cal.getTime().getTime());
    }

    public static String getTimeFormat(long timeInMs){
        //SimpleDateFormat formatter = new SimpleDateFormat("dd" + " " + "MMMM" + " " + "yyyy" + " " + "HH" + ":" + "mm" + ":" + "ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd" + " " + "MMM" + " " + "yyyy" + " " + "HH" + ":" + "mm" + ":" + "ss");
        return formatter.format(timeInMs);
    }

    public static String getTimeFormatShortly(long timeInMs){
        SimpleDateFormat formatter = new SimpleDateFormat("dd" + " " + "MMM" + " " + " " + "HH" + ":" + "mm" + ":" + "ss");
        return formatter.format(timeInMs);
    }

    public static String getTimeFormatNumeric(long timeInMs){
        SimpleDateFormat formatter = new SimpleDateFormat("dd" + "." + "MM" + "." + "yy" + " " + "HH" + ":" + "mm" + ":" + "ss");
        return formatter.format(timeInMs);
    }

    public static String getTimeFormatCronometer(long timeInMs){

        String s = "'";
        String year = s + " " + "year" + s + " " ;
        String month = s + " " + "month" + s + " " ;
        String day = s + " " + "day" + s + " " ;
/*
        long s = 1000 * 10 ;
        long m = 1000 * 60 * 20;
        long h = 1000 * 60 * 60 * 3;
        long d = 1000 * 60 * 60 * 24 * 4;
        long mm = 1000 * 60 * 60 * 24 * 30 * 5;
        long yy = 1000 * 60 * 60 * 24 * 30 * 12 * 6;
        timeInMs = s + m + h + d ;
*/
        int yYear = Integer.parseInt(DurationFormatUtils.formatDuration(timeInMs,"y"));
        int mMonth = Integer.parseInt(DurationFormatUtils.formatDuration(timeInMs,"M"));
        int dDay = Integer.parseInt(DurationFormatUtils.formatDuration(timeInMs,"d"));

        return DurationFormatUtils.formatDuration(timeInMs, (yYear > 0 ? "y"+year : "") + (mMonth >0 ? "M"+ month : "") + (dDay > 0 ? "d"+day : "") + "HH:mm:ss");
        //return DurationFormatUtils.formatDuration(timeInMs,"yy"+year +"M"+ month+"d"+day+"HH:mm:ss");


        //return DurationFormatUtils.formatDuration(timeInMs,"yy'"+year +"'M'"+ month+"'d'"+day+"'HH':'mm':'ss").
        //      replace("00"+year, "").replace("0" + month, "").replace("0" + day, "");
        //return DurationFormatUtils.formatDuration(timeInMs,"'yy'"+year +"'M'"+ month+"'d'"+day+"'H':'m':'s");
        //return DurationFormatUtils.formatDuration(timeInMs,"'P'yyyy'Y'M'M'd'DT'H'H'm'M's.SSS'S'");
    }

    public static String getTimeFormatCronometerSecondOnly(long timeInMs){
        return DurationFormatUtils.formatDuration(timeInMs,  "ss");
    }

    public static String getTimeFormatShort(long timeInMs){
        SimpleDateFormat formatter = new SimpleDateFormat("dd" + "." + "MM" + "." + "yy" + "_" + "HH" + "" + "mm");
        return formatter.format(timeInMs);
    }

    /**
     * Customized Date and Time Formats
     * Pattern	                        Output
     * dd.MM.yy	                        30.06.09
     * yyyy.MM.dd G 'at' hh:mm:ss z	    2009.06.30 AD at 08:29:36 PDT
     * EEE, MMM d, ''yy	Tue,            Jun 30, '09
     * h:mm a	                        8:29 PM
     * H:mm	                            8:29
     * H:mm:ss:SSS	                    8:28:36:249
     * K:mm a,z	                        8:29 AM,PDT
     * yyyy.MMMMM.dd GGG hh:mm aaa	    2009.June.30 AD 08:29 AM
     * @param timeInMs
     * @return
     */
    public static String getTimeFormatDetails(long timeInMs){
        //SimpleDateFormat formatter = new SimpleDateFormat("dd" + " " + "MMMM" + " " + "yyyy" + " EEEEE " + "HH" + ":" + "mm" + ":" + "ss");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd" + " " + "MMM" + " " + "yyyy" + " EEE " + "HH" + ":" + "mm" + ":" + "ss");
        //return formatter.format(timeInMs);
        /**
         * remove day !
         */
        SimpleDateFormat formatter = new SimpleDateFormat("dd" + " " + "MMM" + " " + "yy" + " EEE " + "HH" + ":" + "mm" + ":" + "ss");
        String formated = formatter.format(timeInMs);
        formated = formated.substring(0,10) + formated.substring(14);
        return formated;
    }

    public static String getTimeFormatDate(long timeInMs){
        SimpleDateFormat formatter = new SimpleDateFormat("dd" + "." + "MM" + "." + "yyyy");
        return formatter.format(timeInMs);
    }

    public static String getTimeFormatHour(long timeInMs){
        SimpleDateFormat formatter = new SimpleDateFormat("HH" + ":" + "mm" + ":" + "ss");
        return formatter.format(timeInMs);
    }

    public static String getTimeFormatMinute(long timeInMs){
        SimpleDateFormat formatter = new SimpleDateFormat("mm" + ":" + "ss" + "." + "sss" );
        return formatter.format(timeInMs);
    }

    /**
     * Returns local time zone.
     *
     * @return local time zone
     */
    private static TimeZone getTimeZone() {
        TimeZone tz = timeZone;
        if (tz == null) {
            timeZone = tz = TimeZone.getDefault();
        }
        return tz;
    }

    public static long getTimeInMs(LocalDate localDate){
        //Lets suppose you have a DatePicker instance called datePicker
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
        cal.set(Calendar.MONTH, localDate.getMonth().getValue() - 1);
        cal.set(Calendar.YEAR, localDate.getYear());
        return cal.getTimeInMillis();
    }


    public static void waitInMilis(long ms){
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }

    public static void waitInMicros(long micros){
        long waitUntil = System.nanoTime() + (micros * 1_000);
        while(waitUntil > System.nanoTime()){
        }
    }
    public static void waitInNanos(int ns){
        try { Thread.sleep(0, ns); } catch (InterruptedException e) { }
    }

    public static void main(String[] ard){

        String prettyTime = TimeUtils.getTimeFormatDetails(1588758622000L);
        System.out.println("prettyTime = " + prettyTime);

        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());

        long s = 1000L * 10 ;
        long m = 1000L * 60 * 20;
        long h = 1000L * 60 * 60 * 3;
        long d = 1000L * 60 * 60 * 24 * 30;
        long M = 1000L * 60 * 60 * 24 * 30 * 5;
        long y = 1000L * 60 * 60 * 24 * 30 * 12 * 6;
        //long timeInMs = s + m + h + d ;
        long timeInMs = s + m + h + d + M + y;

        System.out.println("TimeFormatCronometer = " + getTimeFormatCronometer(timeInMs));

        System.out.println(getTimeFormatDetails(computeStartOfHour(0)));
        System.out.println(getTimeFormat(computeStartOfHour(0)));
        System.out.println(getTimeFormat(computeStartOfHour(1)));
        System.out.println(getTimeFormat(computeStartOfHour(2)));
        System.out.println(getTimeFormat(computeStartOfHour(4)));
        System.out.println(getTimeFormat(computeStartOfDay(0)));
        System.out.println(getTimeFormat(computeStartOfDay(-1)));
        System.out.println(getTimeFormat(computeStartOfDay(-3)));

        System.out.println(getTimeFormat(computeStartOfWeek(0)));
        System.out.println(getTimeFormat(computeStartOfWeek(-1)));

        System.out.println(getHour(computeStartOfHour(0)));
        System.out.println(getHour(computeStartOfHour(1)));
        System.out.println(getHour(computeStartOfHour(2)));
        System.out.println(getHour(computeStartOfHour(4)));
        System.out.println(getHour(computeStartOfHour(6)));
        System.out.println(getHour(computeStartOfHour(8)));

        /**
         * Default time
         */
        /*
        for(int i= 0; i< 24; i++)
        {
            long currentTime = TimeUtils.currentTimeMillis();
         (currentTime , i);
            int hour = TimeUtils.getCandlestickIntervalAsHour(TimeUtils.computeStartOfHour(tInMs, 0));
            System.out.println("Hour in regular = " + getTimeFormat(currentTime) + " h : " + hour);

            System.out.println("1h  : " + getTimeFormat(TimeUtils.computeStartOfHour(tInMs, - 1 * (hour % 1))));
            System.out.println("2h  : " + ge   long tInMs = computeStartOfHourtTimeFormat(TimeUtils.computeStartOfHour(tInMs, - 1 * (hour % 2))));
            System.out.println("4h  : " + getTimeFormat(TimeUtils.computeStartOfHour(tInMs, - 1 * (hour % 4))));
            System.out.println("6h  : " + getTimeFormat(TimeUtils.computeStartOfHour(tInMs, - 1 * (hour % 6))));
            System.out.println("8h  : " + getTimeFormat(TimeUtils.computeStartOfHour(tInMs, - 1 * (hour % 8))));
            System.out.println("12h : " + getTimeFormat(TimeUtils.computeStartOfHour(tInMs, - 1 * (hour % 12))));
        }
        */
    }

}
