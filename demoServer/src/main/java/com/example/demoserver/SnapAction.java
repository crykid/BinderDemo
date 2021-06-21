package com.example.demoserver;

/**
 * Created by : mr.lu
 * Created at : 2021/6/11 at 00:32
 * Description:
 */
public interface SnapAction {
    String ACTION_SWITCH_THEME = "ACTION_SWITCH_THEME";
    String ACTION_OPEN_BROWSER = "ACTION_OPEN_BROWSER";
    String EXTRA_STRING_URL = "url";
    /**
     * 0或不传值为打开，1为关闭
     */
    String ACTION_ENABLE_LOG = "ACTION_ENABLE_LOG";
    String EXTRA_INT_ENABLE_LOG = "enable_log";

}
