package com.xuf.www.gobang.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.xuf.www.gobang.util.Constants;
import com.xuf.www.gobang.util.Manager;
import com.xuf.www.gobang.util.Util;
import com.xuf.www.gobang.view.fragment.CoupleGameFragment;
import com.xuf.www.gobang.view.fragment.NetGameFragment;

/**
 * Created by Administrator on 2015/12/8.
 */
public class GameActivity extends BaseActivity {
    private Manager manager;
    @Override
    protected Fragment createFragment() {
        int gameMode = getIntent().getIntExtra(Constants.GAME_MODE, Constants.INVALID_MODE);
        Fragment fragment = null;
        manager = Util.getInstance(this,this);
        switch (gameMode){
            case Constants.INVALID_MODE:
                break;
            case Constants.COUPE_MODE:
                fragment = new CoupleGameFragment();
                break;
            case Constants.WIFI_MODE:
            case Constants.BLUE_TOOTH_MODE:
                fragment = NetGameFragment.newInstance(gameMode);
                break;
        }

        return fragment;
    }

    @Override
    public void onBackPressed() {

    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode,resultCode,data);
    }
}
