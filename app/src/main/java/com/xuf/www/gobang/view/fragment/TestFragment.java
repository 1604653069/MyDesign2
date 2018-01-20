package com.xuf.www.gobang.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xuf.www.gobang.R;
import com.xuf.www.gobang.util.Constants;
import com.xuf.www.gobang.util.Manager;
import com.xuf.www.gobang.util.MusicUtil;
import com.xuf.www.gobang.util.Util;
import com.xuf.www.gobang.view.activity.AiActivity;
import com.xuf.www.gobang.view.activity.GameActivity;
import com.xuf.www.gobang.view.activity.HistoryActivity;
import com.xuf.www.gobang.view.activity.MyVideoActivity;

import org.w3c.dom.Text;

/**
 * Created by Admin on 2018/1/13.
 */

public class TestFragment extends Fragment implements View.OnClickListener {
    private TextView aiMode;
    private TextView coupleMode;
    private TextView wifiMode;
    private TextView toothMode;
    private TextView soundSetting;
    private TextView recordSetting;
    private TextView history;
    private TextView MyVideo;
    private TextView exit;
    private AudioManager audiomanage;
    private RadioButton  turnOn;
    private RadioButton turnOff;
    private int maxVolume, currentVolume;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        aiMode.setOnClickListener(this);
        coupleMode.setOnClickListener(this);
        wifiMode.setOnClickListener(this);
        toothMode.setOnClickListener(this);
        soundSetting.setOnClickListener(this);
        recordSetting.setOnClickListener(this);
        history.setOnClickListener(this);
        MyVideo.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void initView(View view) {
        audiomanage = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        aiMode = (TextView) view.findViewById(R.id.ai_mode);
        coupleMode = (TextView) view.findViewById(R.id.couple_mode);
        wifiMode = (TextView) view.findViewById(R.id.wifi_mode);
        toothMode = (TextView) view.findViewById(R.id.tooth_mode);
        soundSetting  = (TextView) view.findViewById(R.id.sound_setting);
        recordSetting = (TextView) view.findViewById(R.id.recod_setting);
        history = (TextView) view.findViewById(R.id.history);
        MyVideo = (TextView) view.findViewById(R.id.myvideo);
        exit = (TextView) view.findViewById(R.id.exit);
        if(Util.isOpenSound==true)
        {
            soundSetting.setText("声音设置 \n (开)");
        }
        else
        {
            soundSetting.setText("声音设置 \n (关)");
        }
        if(Util.isSelected ==true)
        {
            recordSetting.setText("录屏设置 \n (开)");
        }
        else
        {
            recordSetting.setText("录屏设置 \n (关)");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), GameActivity.class);
        switch (view.getId())
        {
            case R.id.ai_mode:
                startActivity(new Intent(getActivity(), AiActivity.class));
                break;
            case R.id.couple_mode:
                intent.putExtra(Constants.GAME_MODE, Constants.COUPE_MODE);
                startActivity(intent);
                break;
            case R.id.wifi_mode:
                Util.mode = 1;
                intent.putExtra(Constants.GAME_MODE, Constants.WIFI_MODE);
                startActivity(intent);
                break;
            case R.id.tooth_mode:
                Util.mode =2;
                intent.putExtra(Constants.GAME_MODE, Constants.BLUE_TOOTH_MODE);
                startActivity(intent);
                break;
            case R.id.sound_setting:
                showDialog();
                break;
            case R.id.recod_setting:
                if(Util.isSelected==false)
                {
                    showDialog2();
                }
               else
                {
                    showDialog3();
                }
                break;
            case R.id.history:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            case R.id.myvideo:
                startActivity(new Intent(getActivity(), MyVideoActivity.class));
                break;
            case R.id.exit:
                MusicUtil.stopMusic(getActivity());
                getActivity().finish();

                break;
        }
    }
    public void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sound,null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        turnOn = (RadioButton) view.findViewById(R.id.turn_on);
        turnOff = (RadioButton) view.findViewById(R.id.turn_off);
        Button surnBtn = (Button) view.findViewById(R.id.dialog_sureBtn);
        if(MusicUtil.isPlaying)
        {
            turnOn.setChecked(true);
            turnOff.setChecked(false);
        }
        else
        {
            turnOff.setChecked(true);
            turnOn.setChecked(false);
        }
        turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOff.setChecked(false);
            }
        });
        turnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOn.setChecked(false);
            }
        });
        surnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(turnOn.isChecked())
                {
                    if(!MusicUtil.isPlaying())
                    {
                        MusicUtil.startMusic(getContext());
                    }
                    MusicUtil.isPlaying = true;
                    turnOn.setChecked(true);
                    turnOff.setChecked(false);
                    soundSetting.setText("声音设置 \n (开)");
                }
                else
                {
                    MusicUtil.stopMusic(getContext());
                    MusicUtil.isPlaying = false;
                    turnOn.setChecked(false);
                    turnOff.setChecked(true);
                    soundSetting.setText("声音设置 \n (关)");
                }
                dialog.dismiss();
            }
        });
        maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekBar.setMax(maxVolume);   //拖动条最高值与系统最大声匹配
        currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        seekBar.setProgress(currentVolume);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                seekBar.setProgress(currentVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog.show();
    }
    public void showDialog2()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("未开启对局录屏，现在是否开启？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Util.isSelected = true;
                        Toast.makeText(getActivity(), "录屏功能已开启", Toast.LENGTH_SHORT).show();
                        recordSetting.setText("录屏设置 \n (开)");
                    }
                })
                .setNegativeButton("不用了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Util.isSelected = false;
                        recordSetting.setText("录屏设置 \n (关)");
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

        }

    public void showDialog3(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("对局录屏已开启，现在是否关闭?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Util.isSelected = false;
                        Toast.makeText(getActivity(), "录屏功能已关闭", Toast.LENGTH_SHORT).show();
                        recordSetting.setText("录屏设置 \n (关)");
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Util.isSelected = true;
                        recordSetting.setText("录屏设置 \n (开)");
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
}
