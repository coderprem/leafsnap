package com.leafsnap;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utils extends Activity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    static String gettimego(Date olddate, Date newdate) {
        Duration diff = Duration.between(olddate.toInstant(),newdate.toInstant());
//        long days = diff.toDays();
//        diff = diff.minusDays(days);
//        long hours = diff.toHours();
//        diff = diff.minusHours(hours);
//        long minutes = diff.toMinutes();
//        diff = diff.minusMinutes(minutes);
//        long seconds = diff.toMillis();

        NumberFormat formatter = new DecimalFormat("##");
        return String.valueOf(diff.toHours()+":"+String.valueOf(new DecimalFormat("00").format(Integer.parseInt(formatter.format(diff.toMinutes() - diff.toHours()*60)))));

        //return diff.toHours()+":"+diff.toMinutes();
    }
    protected static CharSequence getdurationago(String oldTime) {
        int day = 0;
        int hh = 0;
        int mm = 0;
        String ret="day";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy 'at' hh:mm aa");
            Date oldDate = dateFormat.parse(oldTime);
            Date cDate = new Date();
            Long timeDiff = cDate.getTime() - oldDate.getTime();
            day = (int) TimeUnit.MILLISECONDS.toDays(timeDiff);
            hh = (int) (TimeUnit.MILLISECONDS.toHours(timeDiff) - TimeUnit.DAYS.toHours(day));
            mm = (int) (TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));


        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mm <= 60 && hh != 0) {
            if (hh <= 60 && day != 0) {
                ret= day + " Day";
            } else {
                ret= hh + " Hrs ";
            }
        } else {
            ret= mm + " M ago";
        }

    return ret;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected static CharSequence getdurationtime(Date d1, Date d2) {
        Duration diff = Duration.between(d1.toInstant(),d2.toInstant());
//        long days = diff.toDays();
//        diff = diff.minusDays(days);
//        long hours = diff.toHours();
//        diff = diff.minusHours(hours);
//        long minutes = diff.toMinutes();
//        diff = diff.minusMinutes(minutes);
//        long seconds = diff.toMillis();

        NumberFormat formatter = new DecimalFormat("##");

        if (diff.toDays()>1) {
            return String.valueOf(diff.toHours()+diff.toDays()*24+":"+String.valueOf(new DecimalFormat("00").format(Integer.parseInt(formatter.format(diff.toMinutes() - diff.toHours()*60))))+":"+String.valueOf(new DecimalFormat("00").format(Integer.parseInt(formatter.format(diff.getSeconds() - diff.toMinutes()*60))))+" Day");

        }
        else
        {
            return String.valueOf(diff.toHours()+":"+String.valueOf(new DecimalFormat("00").format(Integer.parseInt(formatter.format(diff.toMinutes() - diff.toHours()*60))))+":"+String.valueOf(new DecimalFormat("00").format(Integer.parseInt(formatter.format(diff.getSeconds() - diff.toMinutes()*60))))+" hr");

        }

        //return diff.toHours()+":"+diff.toMinutes();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }
    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
    public String getTimeTaken(Location source, Location dest) {


        double meter = source.distanceTo(dest);

        double kms = meter / 1000;

        double kms_per_min = 0.5;

        double mins_taken = kms / kms_per_min;

        int totalMinutes = (int) mins_taken;

        Log.d("ResponseT","meter :"+meter+ " kms : "+kms+" mins :"+mins_taken);

        if (totalMinutes<60)
        {
            return ""+totalMinutes+" mins";
        }else {
            String minutes = Integer.toString(totalMinutes % 60);
            minutes = minutes.length() == 1 ? "0" + minutes : minutes;
            return (totalMinutes / 60) + " hour " + minutes +"mins";

        }


    }
    public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    //DateTime tenMinutesLater = DateTime.now().plusMinutes( 10 );
    private static final int earthRadius = 6371;
    public static float calculateDistance(float lat1, float lon1, float lat2, float lon2)
    {
        float dLat = (float) Math.toRadians(lat2 - lat1);
        float dLon = (float) Math.toRadians(lon2 - lon1);
        float a =
                (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        float d = earthRadius * c;
        return d;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public static long getdayDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static <DateTime> LocalDateTime minusSECOND(DateTime dateTime, int i) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm ss", Locale.getDefault());
        LocalDateTime ldt = LocalDateTime.parse(dateFormat.format(dateTime));
        LocalDateTime ldtLater = ldt.plusSeconds(-i);
        return ldtLater;
    }

    static String ConvertSecondToHHMMSSString(int nSecondTime) {
        String time=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time=LocalTime.MIN.plusSeconds(nSecondTime).toString();
        }
        return time;
    }
    public static String ComponentTimes(long totalSecs)
    {
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;

       String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return timeString;
    }
    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }
    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }
    public static Date addYear(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        return cal.getTime();
    }
    public static String convstringtoDatetime(String dateString) {

        if (dateString == null) {
            return null;
        }
        SimpleDateFormat dateFormatlocal = new SimpleDateFormat("HH:mm:ss a , dd-MM-yyyy ", Locale.getDefault());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(dateFormatlocal.format(date));
    }
    public static String convstringtotime(String dateString) {

        if (dateString == null) {
            return null;
        }
        SimpleDateFormat dateFormatlocal = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(dateFormatlocal.format(date));
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }
    public static String getDuplicates(String S) {
        int count = 0;
        String t = "";
        for (int i = 0; i < S.length() - 1; i++) {
            for (int j = i + 1; j < S.length(); j++) {
                if (S.charAt(i) == S.charAt(j) && !t.contains(S.charAt(j) + "")) {
                    t = t + S.charAt(i);
                }
            }
        }
        return String.valueOf(t);
    }

    public void Getcopy(String coppytext)
    {  // Getcopy(((TextView) findViewById(R.id.opendatetxt)).getText().toString());
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null,(coppytext));
        clipboard.setPrimaryClip(clip);
        Snackbar.make(findViewById(android.R.id.content),"Copy", Snackbar.LENGTH_SHORT).show();

    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
    protected static String randomnumber9(int digit) {
        String SALTCHARS = "123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < digit) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    protected static String randomnumber(int digit) {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < digit) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    public static String randomcode(int digit) {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < digit) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    public static String getDurationBreakdown(long millis) {
        if(millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        //sb.append(days);
       // sb.append(" Days ");
        sb.append(hours);
        //sb.append(" Hours ");
        sb.append(minutes);
       // sb.append(" Minutes ");
        sb.append(seconds);
       // sb.append(" Seconds");

        return(sb.toString());
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for(int idx=0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     * @param str which to be converted
     * @return  array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try { return str.getBytes("UTF-8"); } catch (Exception ex) { return null; }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     * @param filename which to be converted to string
     * @return String value of File
     * @throws IOException if error occurs
     */
    public static String loadFileAsString(String filename) throws IOException {
        final int BUFLEN=1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8=false;
            int read,count=0;
            while((read=is.read(bytes)) != -1) {
                if (count==0 && bytes[0]==(byte)0xEF && bytes[1]==(byte)0xBB && bytes[2]==(byte)0xBF ) {
                    isUTF8=true;
                    baos.write(bytes, 3, read-3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count+=read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try{ is.close(); } catch(Exception ignored){}
        }
    }

    /**
     * Returns MAC address of the given interface name.
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return  mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:",aMac));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }
    public static Long toEpoch(Date dateTime, String timeZone) {
        // Epoch of midnight in local time zone
        Calendar timeOffset = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        timeOffset.set(Calendar.MILLISECOND, 0);
        timeOffset.set(Calendar.SECOND, 0);
        timeOffset.set(Calendar.MINUTE, 0);
        timeOffset.set(Calendar.HOUR_OF_DAY, 0);
        long midnightOffSet = timeOffset.getTime().getTime();
        long localTimestamp = dateTime.getTime();
        return timeOffset == null ? null : midnightOffSet + localTimestamp;
    }
    public static String coolNumberFormat(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.#");
        String value = format.format(count / Math.pow(1000, exp));
        return String.format("%s%c", value, "kMBTPE".charAt(exp - 1));
    }
    public static String getscentohr(int input) {
        String Output = null;
        // Variable declaration
        //Scanner scan = new Scanner(System.in);
        final int MIN = 60, HRS = 3600, DYS = 84600;
        int days, seconds, minutes, hours, rDays, rHours;

        // Input
        System.out.println("Enter amount of seconds!");
        // input = scan.nextInt();

        // Calculations
        days = input / DYS;
        rDays = input % DYS;
        hours = rDays / HRS;
        rHours = rDays % HRS;
        minutes = rHours / MIN;
        seconds = rHours % MIN;


        if (input >= DYS) {
            Output=String.valueOf(days);
            //System.out.println(input + " seconds equals to " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
        } else if (input >= HRS && input < DYS) {
            Output= String.valueOf(hours + " Hr");
            // System.out.println(input + " seconds equals to " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
        } else if (input >= MIN && input < HRS) {
            Output= String.valueOf(minutes + ":" + seconds + " Min");
            //System.out.println(input + " seconds equals to " + minutes + " minutes " + seconds + " seconds");
        } else if (input < MIN) {
            Output= String.valueOf(seconds + " Sec");
            //System.out.println(input + " seconds equals to seconds");
        }

        //scan.close();
        return Output;
    }

    public  static String agotime(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Sec " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Min "+suffix;
            } else if (hour < 24) {
                convTime = hour + " Hr "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " Yrs " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " Mon " + suffix;
                } else {
                    convTime = (day / 7) + " Wk " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" D "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }
}
