package com.xuf.www.gobang.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xuf.www.gobang.R;
import com.xuf.www.gobang.db.HisDao;
import com.xuf.www.gobang.db.History;
import com.xuf.www.gobang.util.Manager;
import com.xuf.www.gobang.util.Util;
import com.xuf.www.gobang.widget.AiGameView;


public class AiActivity extends Activity {
    private TextView tvWin;
    private Button btnAgain, btnDifficult;
    private IntentFilter intentFilter;
    private MyBroadCastReciver broadCastReciver;
    private Manager manager;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getGameTime();
            handler.postDelayed(this,1000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ai);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        init();
        Util.gameTime =0;
        handler.postDelayed(runnable,1000);
    }

    private void init() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("huangqihong");
        broadCastReciver = new MyBroadCastReciver();
        registerReceiver(broadCastReciver,intentFilter);
        final AiGameView mAigameView = (AiGameView) findViewById(R.id.vw_gobang);
        tvWin = (TextView) findViewById(R.id.tv_win);
        mAigameView.setTextView(tvWin);
        btnAgain = (Button) findViewById(R.id.btn_again);
        btnDifficult = (Button) findViewById(R.id.btn_difficult);

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAigameView.isWin = false;
                mAigameView.restartGame();
                Util.gameTime = 0;
                handler.postDelayed(runnable,1000);
            }
        });

        btnDifficult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mAigameView.getAI_DIFFICULT()) {
                    case 2:
                        mAigameView.setAI_DIFFICULT(3);
                        btnDifficult.setText("难度:中等");
                        break;
                    case 3:
                        mAigameView.setAI_DIFFICULT(4);
                        btnDifficult.setText("难度:困难");
                        break;
                    case 4:
                        mAigameView.setAI_DIFFICULT(2);
                        btnDifficult.setText("难度:简单");
                        break;
                }
                mAigameView.isWin = false;
                mAigameView.restartGame();
            }
        });
//        manager = new Manager(this,this);
//        manager.onCreate();
//        manager.onRecordClick(true);
    }
    public void getGameTime()
    {
        Util.gameTime++;
        Message message = new Message();
        message.what=1;
        handler.sendMessage(message);
    }
    public class MyBroadCastReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
                handler.removeCallbacks(runnable);
                Util.gameTime =0;
        }
    }
}
