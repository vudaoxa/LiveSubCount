package com.tieudieu.util;

import android.util.Log;

/**
 * date 21.06.2012
 * author Mustafa Ferhan Akman
 *
 *         Create a simple and more understandable Android logs.
 * */

public class DebugLog {
    private static boolean enable = true;
    private static boolean isShowLogDetail = false;

    static String className;
    static String methodName;
    static int lineNumber;


    private DebugLog() {
		/* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return enable;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        if (isShowLogDetail){
            buffer.append("[");
            buffer.append(methodName);
            buffer.append(":");
            buffer.append(lineNumber);
            buffer.append("]");
        }
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void i(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void v(String message) {
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

    /**
     * param object
     */
    public static void d(Object object) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(object.toString()));
    }

    public static void setEnable(boolean enable) {
        DebugLog.enable = enable;
    }
}