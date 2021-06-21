package com.margin.base.utils;

import android.util.Log;

import com.margin.base.SnapConfig;


/**
 * Created by : mr.lu
 * Created at : 2021/6/11 at 00:39
 * Description:
 */
public class L {
    public static void e(TAG tag, String s) {
        Log.e(tag.toString(), s);
    }

    public static void e(TAG tag, Exception e) {
        Log.e(tag.toString(), e.toString());
    }


    public static class TAG {
        public String tag;

        public TAG(String tag) {
            this.tag = SnapConfig.LOG_PREFIX + tag;
        }

        @Override
        public String toString() {
            return tag;
        }
    }

    public static void d(TAG tag, String msg) {
        if (SnapConfig.LOG_ENABLE) {
            Log.d(tag.toString(), msg);
        }
    }

    public static void i(TAG tag, String msg) {
        Log.i(tag.toString(), msg);
    }
}
