package com.xuf.www.gobang.util;


import android.app.Activity;
import android.content.Context;


/**
 * Created by Admin on 2018/1/9.
 */

public class Util {
    public static boolean isSelected = false; //是否开启录屏
    public static boolean isOpenSound = true;//是否开启背景音乐
    public static int mode = 0;
    public static Manager manager = null;
    public static Manager getInstance(Context context, Activity activity)
    {
        if(manager==null)
        {
            manager = new Manager(context,activity);
        }
        return manager;
    }
    public static int gameTime =0;
}
