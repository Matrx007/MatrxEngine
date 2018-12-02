package com.engine.libs.console;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Print {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    public static void print(String text) {
        System.out.println("["+simpleDateFormat.format(new Date())+"]: "+text);
    }

    public static void print(Class from, String text) {
        System.out.println("["+simpleDateFormat.format(new Date())+"] (from "+from.getName()+"): "+text);
    }

    public static void err(Class from, String text) {
        System.err.println("["+simpleDateFormat.format(new Date())+"] (from "+from.getName()+"): "+text);
    }

    public static void err(String text) {
        System.err.println("["+simpleDateFormat.format(new Date())+"]: "+text);
    }

    public static void err(Class from, Exception e, String text) {
        System.err.println("["+simpleDateFormat.format(new Date())+"] (from "+from.getName()+"): "+text);
        e.printStackTrace();
    }

    public static void err(Exception e, String text) {
        System.err.println("["+simpleDateFormat.format(new Date())+"]: "+text);
        e.printStackTrace();
    }

    public static void err(Class from, Exception e) {
        System.err.println("["+simpleDateFormat.format(new Date())+"] (from "+from.getName()+")");
        e.printStackTrace();
    }

    public static void err(Exception e) {
        System.err.println("["+simpleDateFormat.format(new Date())+"]");
        e.printStackTrace();
    }

    public static void err() {
        System.err.println("["+simpleDateFormat.format(new Date())+"] Error occurred");
    }
}
