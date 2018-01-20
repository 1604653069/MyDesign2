package com.xuf.www.gobang.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.xuf.www.gobang.util.AudioService;
import com.xuf.www.gobang.util.MusicUtil;
import com.xuf.www.gobang.util.ToastUtil;
import com.xuf.www.gobang.view.fragment.MainFragment;
import com.xuf.www.gobang.view.fragment.TestFragment;


public class MainActivity extends BaseActivity {
    long firstBackTime = 0;

    @Override
    protected Fragment createFragment() {
        MusicUtil.startMusic(this);
        return new TestFragment();
    }

    @Override
    public void onBackPressed() {
        long secondBackTime = System.currentTimeMillis();
        if (secondBackTime - firstBackTime > 2000) {
            ToastUtil.showShort(this, "再按一次退出程序");
            firstBackTime = secondBackTime;
        } else {
            finish();
         MusicUtil.stopMusic(this);
        }
    }
}
