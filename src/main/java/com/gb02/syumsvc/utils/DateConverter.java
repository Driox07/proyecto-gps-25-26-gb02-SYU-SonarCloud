package com.gb02.syumsvc.utils;

import java.text.SimpleDateFormat;

import com.gb02.syumsvc.exceptions.UnexpectedErrorException;

public class DateConverter {
    public static java.sql.Date string2sqlDate(Object date){
        try{
            if(date instanceof java.sql.Date) return (java.sql.Date)date;

            java.util.Date jdate;
            if(date instanceof java.util.Date){
                jdate = (java.util.Date) date;
            }
            else if (date instanceof String){
                SimpleDateFormat sfd = new SimpleDateFormat("YYYY-MM-dd");
                jdate = sfd.parse((String)date);
            }else{
                throw new UnexpectedErrorException("");
            }
            java.sql.Date sdate = new java.sql.Date(jdate.getTime());
            return sdate;
        }catch (Exception e){
            e.printStackTrace();
            throw(new UnexpectedErrorException("Error converting java util date to java sql date"));
        }
    }
}
