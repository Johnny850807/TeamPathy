package com.ood.clean.waterball.teampathy.MyUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class EnglishAbbrDateConverter {
    public final static String[] DATE_NAME_ENG_ABR = {"Jan.","Feb.","Mar.","Apr.","May.","Jun.",
            "Jul.","Aug.","Sep.","Oct.","Nov.","Dec."};

    public static String dateToTime(Date datetime, boolean showTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
        String date = String.valueOf(calendar.get(Calendar.DATE));
        String month = DATE_NAME_ENG_ABR[(calendar.get(Calendar.MONTH))];
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1)
            hour = "0" + hour ;
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if (minute.length() == 1)
            minute = "0" + minute ;
        StringBuilder str = new StringBuilder();
        str.append(year).append(" ").append(date).append(" ").append(month);
        if (showTime)
            str.append(" ").append(hour).append(":").append(minute);
        return  str.toString();
    }

    public static Date timeToDate(String datetimeString) throws ParseException {
        String[] dateSnippets = datetimeString.split(" ");
        int year = Integer.parseInt(dateSnippets[0]);
        int day = Integer.parseInt(dateSnippets[1]);
        int month = monthAbbrNameToInti(dateSnippets[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0);
        return calendar.getTime();
    }

    // Jun. => 6
    public static int monthAbbrNameToInti(String abbr) throws ParseException {
        for (int i = 0 ; i < DATE_NAME_ENG_ABR.length ; i ++ )
            if (DATE_NAME_ENG_ABR[i].equalsIgnoreCase(abbr))
                return i + 1;  // array index
        throw new java.text.ParseException(abbr + " is not any of the month abbr.",0);
    }

}