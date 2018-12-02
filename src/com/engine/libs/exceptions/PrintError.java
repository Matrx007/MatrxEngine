package com.engine.libs.exceptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrintError {
    public static void printError(String msg) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");
        System.err.println("["+sdf.format(cal.getTime())+"] Error: "+msg);
        System.err.flush();
        System.out.flush();
    }
    public static void printMessage(String msg) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");
        System.out.println("["+sdf.format(cal.getTime())+"] "+msg);
        System.out.flush();
        System.err.flush();
    }
}
