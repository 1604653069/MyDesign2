package com.xuf.www.gobang.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.xuf.www.gobang.R;
import com.xuf.www.gobang.bean.Point;
import com.xuf.www.gobang.db.HisDao;
import com.xuf.www.gobang.db.History;
import com.xuf.www.gobang.util.GameJudger;
import com.xuf.www.gobang.util.Manager;
import com.xuf.www.gobang.util.ToastUtil;
import com.xuf.www.gobang.util.Util;
import com.xuf.www.gobang.widget.GoBangBoard;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Xuf on 2016/2/7.
 */
public class CoupleGameFragment extends Fragment implements GoBangBoard.PutChessListener, View.OnClickListener, View.OnTouchListener{

    private boolean mIsGameStarted = false;
    private boolean mIsWhiteFirst = true;
    private boolean mCurrentWhite;
    private int num =0;
    private GoBangBoard mGoBangBoard;
    private ButtonRectangle mStartGame;
    private ButtonRectangle mStartFirst;
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
    private int time = 0;
    private Manager manager;
    private HisDao hisDao = new HisDao();
    private History history ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_couple_game, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        hisDao.openDb(getActivity());
        history = new History();
        if(Util.isSelected)
        {
            manager = Util.getInstance(getActivity(),getActivity());
            manager.onCreate();
        }

        mGoBangBoard = (GoBangBoard) view.findViewById(R.id.go_bang_board);
        mGoBangBoard.setOnTouchListener(this);
        mGoBangBoard.setPutChessListener(this);

        mCurrentWhite = mIsWhiteFirst;

        mStartGame = (ButtonRectangle) view.findViewById(R.id.btn_start_game);
        mStartGame.setOnClickListener(this);

        mStartFirst = (ButtonRectangle) view.findViewById(R.id.btn_start_first);
        mStartFirst.setOnClickListener(this);

        ButtonRectangle exitGame = (ButtonRectangle) view.findViewById(R.id.btn_exit_game);
        exitGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_game:
                if (!mIsGameStarted) {
                    mIsGameStarted = true;
                    mCurrentWhite = mIsWhiteFirst;
                    setWidgets();
                    if(Util.isSelected)
                    {

                    manager.onRecordClick(true);
                    }
                    time=0;
                    handler.postDelayed(runnable,1000);
                }
                break;
            case R.id.btn_start_first:
                mIsWhiteFirst = !mIsWhiteFirst;
                mCurrentWhite = mIsWhiteFirst;
                String buttonText;
                if (mCurrentWhite){
                    buttonText = "白子先手";
                } else {
                    buttonText = "黑子先手";
                }
                mStartFirst.setText(buttonText);
                break;
            case R.id.btn_exit_game:
                manager.onRecordClick(false);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onPutChess(int[][] board, int x, int y) {
        if (GameJudger.isGameEnd(board, x, y)) {
            String msg = String.format("%s赢了", mCurrentWhite ? "白棋" : "黑棋");
            ToastUtil.showShort(getActivity(), msg);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
            String format = simpleDateFormat.format(new Date());
            history.setDate(format);
            history.setMode("双人对战");
            history.setMycolor("white");
            history.setHiscolor("black");
            if(mCurrentWhite==true)
                history.setCondition("胜利");
            else
                history.setCondition("失败");
            history.setTime(time+"");
            hisDao.addHistory(history);
            mIsGameStarted = false;
            Toast.makeText(getContext(), "这局游戏发费了"+time+"s", Toast.LENGTH_SHORT).show();
            handler.removeCallbacks(runnable);
            resetWidgets();
            try{
                manager.onRecordClick(false);
            }catch (Exception e)
            {

            }
        }
    }

    private void setWidgets() {
        mGoBangBoard.clearBoard();
        mStartGame.setEnabled(false);
        mStartFirst.setEnabled(false);
    }

    private void resetWidgets() {
        mStartGame.setEnabled(true);
        mStartFirst.setEnabled(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!mIsGameStarted) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                Point point = mGoBangBoard.convertPoint(x, y);
                if (mGoBangBoard.putChess(mCurrentWhite, point.x, point.y)) {
                    mCurrentWhite = !mCurrentWhite;
                }
                break;
        }
        return false;
    }

    public void getGameTime()
    {
        time++;
        Message message = new Message();
        message.what=1;
        handler.sendMessage(message);
    }
}
