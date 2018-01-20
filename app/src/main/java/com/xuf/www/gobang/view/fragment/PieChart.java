package com.xuf.www.gobang.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuf.www.gobang.util.ChartPieView_w;

/**
 * Created by Admin on 2018/1/14.
 */

public class PieChart extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new ChartPieView_w().ChartPieView(getActivity());
    }
}
