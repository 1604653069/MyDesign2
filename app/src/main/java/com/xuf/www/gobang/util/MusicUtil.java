package com.xuf.www.gobang.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.xuf.www.gobang.R;

/**
 * Created by Admin on 2018/1/9.
 */

public class MusicUtil {
    public static Intent intent;
    private static MediaPlayer mediaPlayer;
    public static boolean isPlaying =false;
    public static void startMusic(Context context)
    {
        mediaPlayer =  getInstance(context);
        intent = new Intent(context,AudioService.class);
        isPlaying = true;
        context.startService(intent);
    }
    public static void stopMusic(Context context)
    {
        context.stopService(intent);
    }
    public static boolean isPlaying()
    {
        if(mediaPlayer.isPlaying())
        {
            isPlaying =true;
            return true;
        }

        else{
            isPlaying = false;
            return false;
        }

    }
    public static MediaPlayer getInstance(Context context)
    {
        if(mediaPlayer==null)
        {
            mediaPlayer  = MediaPlayer.create(context,R.raw.backgroundmusic);
        }
            return mediaPlayer;
    }
}
