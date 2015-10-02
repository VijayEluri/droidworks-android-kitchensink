package com.droidworks.util;

/**
 * Created by jasonhudgins on 11/13/14.
 */
public interface AndroidLogger {
    abstract void e(String tag, String msg);
    abstract void e(String tag, String msg, Throwable tr);

    abstract void d(String tag, String msg);
    abstract void d(String tag, String msg, Throwable tr);
}
