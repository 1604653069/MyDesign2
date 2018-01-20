package com.xuf.www.gobang.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Manager {
    private final int REQUEST_CODE = 0x001;
    private final int RESULT_CODE = 0x002;
    private  String FILE_PATH = "/sdcard/MyVideo/";

    private final int DISPLAY_WIDTH = 1280;
    private final int DISPLAY_HEIGHT = 720;

    private Context mContext;
    private Activity mActivity;

    private ToggleButton mToggleButton;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private int mScreenDensity;

    private MediaRecorder mMediaRecorder;
    public Manager(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    public void onCreate(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission2 = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int checkCallPhonePermission3 = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO);
            if (checkCallPhonePermission2 != PackageManager.PERMISSION_GRANTED && checkCallPhonePermission3 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 123);
                return;
            }
        }
        Log.d("zhangjunling","onCreate()");
        initData();

        mMediaProjectionManager = (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mMediaRecorder = new MediaRecorder();
        mMediaProjectionCallback = new MediaProjectionCallback();
       /* initRecorder(FILE_PATH);
        prepareRecorder();*/
    }

    private void initData() {
        DisplayMetrics displayMetrics  = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.densityDpi;
    }



    /**
     * 开始录屏
     * @param
     */
    public void onRecordClick(boolean b) {
        if(b){
            Log.d("zhangjunling","checked true");
            getFILE_PATH();
            initRecorder(FILE_PATH);
            prepareRecorder();
            if(null == mMediaProjection){
                Intent intent = new Intent(mMediaProjectionManager.createScreenCaptureIntent());
                mActivity.startActivityForResult(intent,REQUEST_CODE);
                return;
            }

            mVirtualDisplay = createVirtualDisplay();
            mMediaRecorder.start();

        }else{
            Log.d("zhangjunling","checked false");
            stopRecord();
            mVirtualDisplay.release();
            /*initRecorder(FILE_PATH);
            prepareRecorder();*/

        }
    }

    private void stopRecord() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("zhangjunling","onActivityResult()00");
        if(requestCode != REQUEST_CODE){
            Toast.makeText(mContext,"REQUEST_CODE error",Toast.LENGTH_SHORT).show();
            Util.isSelected = false;
            return;
        }
        if(resultCode != mActivity.RESULT_OK){
            Toast.makeText(mContext,"RESULT_CODE error",Toast.LENGTH_SHORT).show();
            Util.isSelected = false;
            return;
        }

        mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode,data);
        mMediaProjection.registerCallback(mMediaProjectionCallback , null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
        Log.d("zhangjunling","onActivityResult()01");
    }

    private final class MediaProjectionCallback extends MediaProjection.Callback{

        @Override
        public void onStop() {
            Log.d("zhangjunling","MediaProjectionCallback::onStop()");
            super.onStop();
            if(Util.isSelected){
               Util.isSelected = false;
                stopRecord();
                mVirtualDisplay.release();
                mMediaProjection.stop();
                mMediaProjection.unregisterCallback(mMediaProjectionCallback);
                mMediaProjection = null;
            }
        }
    }

    private VirtualDisplay createVirtualDisplay() {
        /**
         * 创建虚拟画面
         * 第一个参数：虚拟画面名称
         * 第二个参数：虚拟画面的宽度
         * 第三个参数：虚拟画面的高度
         * 第四个参数：虚拟画面的标志
         * 第五个参数：虚拟画面输出的Surface
         * 第六个参数：虚拟画面回调接口
         */
        return mMediaProjection
                .createVirtualDisplay("MainActivity", DISPLAY_WIDTH,
                        DISPLAY_HEIGHT, mScreenDensity,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mMediaRecorder.getSurface()/*mImageReader.getSurface()*/,
                        null /* Callbacks */, null /* Handler */);
    }

    private void prepareRecorder() {
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            mActivity.finish();
        }
    }

    private void initRecorder(String filePath) {
        /**
         *  视频编码格式：default，H263，H264，MPEG_4_SP
         获得视频资源：default，CAMERA
         音频编码格式：default，AAC，AMR_NB，AMR_WB
         获得音频资源：defalut，camcorder，mic，voice_call，voice_communication,
         voice_downlink,voice_recognition, voice_uplink
         输出方式：amr_nb，amr_wb,default,mpeg_4,raw_amr,three_gpp
         */
        //设置音频源
        if (Build.VERSION.SDK_INT >= 21) {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        }else {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        //设置视频源：Surface和Camera 两种
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        //设置视频输出格式
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //设置视频编码格式
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置音频编码格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置视频编码的码率
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        //设置视频编码的帧率
        mMediaRecorder.setVideoFrameRate(30);
        //设置视频尺寸大小
        mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        //设置视频输出路径
        mMediaRecorder.setOutputFile(filePath);

    }


    public void onResume(){
        Log.d("zhangjunling","onResume()");
    }


    public void onPause(){
        Log.d("zhangjunling","onPause()");
    }

    public void onDestroy(){

        Log.d("zhangjunling","onDestroy()");
        if(Util.isSelected){
            Util.isSelected = false;
        }
        if(null != mMediaRecorder){
           // mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        if(null != mVirtualDisplay){
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        if(null != mMediaProjection){
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }
    public String getFILE_PATH()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        FILE_PATH+=format+".mp4";
        return FILE_PATH;
    }
}
