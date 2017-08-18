package com.ood.clean.waterball.teampathy.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 2017/8/17.
 */

public class NormalDateConverter {

    public static Date stringToDate(String dateString) throws ParseException {
        dateString = dateString.replace("+", "p");
        dateString = dateString.split("p")[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        return format.parse(dateString);
    }
}
