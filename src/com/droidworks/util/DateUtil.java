package com.droidworks.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jasonhudgins on 10/12/14.
 */
public class DateUtil {


    public static Calendar getCalenderGMT() {
        Calendar c = Calendar.getInstance();

        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();

        if(z.inDaylightTime(new Date())){
            offset = offset + z.getDSTSavings();
        }

        int offsetHrs = offset / 1000 / 60 / 60;
        int offsetMins = offset / 1000 / 60 % 60;

        System.out.println("offset: " + offsetHrs);
        System.out.println("offset: " + offsetMins);

        c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
        c.add(Calendar.MINUTE, (-offsetMins));

        return c;
    }

}
