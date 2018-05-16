package com.massacre.codigotutoria.utils;


import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by saurabh on 28/7/17.
 */
public class CodigoTutoriaDateFormatter {
    public Date parse(String source) throws ParseException {
        //Log.e("Source for date",source);
        Date date=new SimpleDateFormat("yyyy-MM-dd").parse(source);
        return date;
    }

    public String formatYYYYMMDD(Date date){
        //Log.e("Date",date.toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);

    }

}
