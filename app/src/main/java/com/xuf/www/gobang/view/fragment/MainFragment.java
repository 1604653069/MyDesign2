package com.xuf.www.gobang.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
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
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.xuf.www.gobang.db.History;
import com.xuf.www.gobang.util.Constants;
import com.xuf.www.gobang.R;
import com.xuf.www.gobang.util.MusicUtil;
import com.xuf.www.gobang.util.Util;
import com.xuf.www.gobang.view.activity.AiActivity;
import com.xuf.www.gobang.view.activity.GameActivity;
import com.xuf.www.gobang.view.activity.HistoryActivity;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private AudioManager audiomanage;
    private RadioButton  turnOn;
    private RadioButton turnOff;
    private int maxVolume, currentVolume;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initView(view);

        return view;
    }

    private void initView(View root){
        ButtonRectangle coupeTextView = (ButtonRectangle) root.findViewById(R.id.tv_coupe_mode);
        ButtonRectangle wifiTextView = (ButtonRectangle) root.findViewById(R.id.tv_wifi_mode);
        ButtonRectangle blueToothTextView = (ButtonRectangle) root.findViewById(R.id.tv_blue_tooth_mode);

        ButtonRectangle soundTextView = (ButtonRectangle) root.findViewById(R.id.tv_sound);
        ButtonRectangle screenTextView = (ButtonRectangle) root.findViewById(R.id.tv_screen);
        ButtonRectangle historyTextView = (ButtonRectangle) root.findViewById(R.id.tv_history);
        ButtonRectangle aiTextView = (ButtonRectangle) root.findViewById(R.id.tv_ai_mode);
        audiomanage = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        coupeTextView.setOnClickListener(this);
        wifiTextView.setOnClickListener(this);
        blueToothTextView.setOnClickListener(this);

        soundTextView.setOnClickListener(this);
        screenTextView.setOnClickListener(this);
        historyTextView.setOnClickListener(this);
        aiTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), GameActivity.class);
        switch (v.getId()){
            case R.id.tv_coupe_mode:
                intent.putExtra(Constants.GAME_MODE, Constants.COUPE_MODE);
                startActivity(intent);
                break;
            case R.id.tv_wifi_mode:
                intent.putExtra(Constants.GAME_MODE, Constants.WIFI_MODE);
                startActivity(intent);
                break;
            case R.id.tv_blue_tooth_mode:
                intent.putExtra(Constants.GAME_MODE, Constants.BLUE_TOOTH_MODE);
                startActivity(intent);
                break;
            case R.id.tv_sound:
                showDialog();
                break;
            case R.id.tv_screen:
                showDialog2();
                break;
            case R.id.tv_history:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            case R.id.tv_ai_mode:
                startActivity(new Intent(getActivity(), AiActivity.class));
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
               }
               else
               {
                   MusicUtil.stopMusic(getContext());
                   MusicUtil.isPlaying = false;
                   turnOn.setChecked(false);
                   turnOff.setChecked(true);
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
                    }
                })
                .setNegativeButton("不用了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Util.isSelected = false;
                        dialogInterface.dismiss();
                    }
                })
        .create().show();
    }
}
