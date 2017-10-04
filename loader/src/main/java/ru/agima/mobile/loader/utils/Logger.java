package ru.agima.mobile.loader.utils;

import android.util.Log;

public class Logger {
    private static boolean isEnableLogging;
    private static final String tag = "Loader";

    public static void disableLogging() {
        isEnableLogging = false;
    }

    public static void enableLogging() {
        isEnableLogging = true;
    }

    public static void debug(Object... args) {
        if (args == null) return;
        final StringBuilder text = new StringBuilder();
        for (Object arg : args) {
            text.append(arg);
            text.append(" ");
        }
        d(text.toString());
    }

    public static void error(Object... args) {
        if (args == null) return;
        final StringBuilder text = new StringBuilder();
        for (Object arg : args) {
            text.append(arg);
            text.append(" ");
        }
        e(text.toString());
    }

    public static void error(String text, Throwable throwable) {
        e(text, throwable);
    }

    private static void d(String text) {
        if (isEnableLogging) Log.d(tag, text);
    }

    private static void e(String text) {
        if (isEnableLogging) Log.e(tag, text);
    }

    private static void e(String text, Throwable throwable) {
        if (isEnableLogging) Log.e(tag, text, throwable);
    }
}