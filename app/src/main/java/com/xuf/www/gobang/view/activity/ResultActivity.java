package com.xuf.www.gobang.view.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.xuf.www.gobang.R;
import com.xuf.www.gobang.util.ChartPieView_w;
import com.xuf.www.gobang.util.PieChartView;
import com.xuf.www.gobang.view.fragment.BarChartFragment;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        noTitle();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_pie,new com.xuf.www.gobang.view.fragment.PieChart()).commit();
          getSupportFragmentManager().beginTransaction().replace(R.id.fl_pie,new BarChartFragment()).commit();
    }
    public void noTitle()
    {
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null)
        {
            supportActionBar.hide();
        }
    }
}
