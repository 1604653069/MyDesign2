package com.xuf.www.gobang.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xuf.www.gobang.db.HisDao;
import com.xuf.www.gobang.db.History;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lenovo on 2017/11/29.
 */

public class PieChartView {
    private PieChart pieChart;
    private HisDao hisDao = new HisDao();
    private History history;
    private List<History> histories = new ArrayList<>();
    private int victoryNum=0;
    private int otherNum=0;
    private int count=0;
    public PieChart PieChartView(Context context, String[] strings) {
        hisDao.openDb(context);
        history = new History();
        getDate();
        getVictoryNum();
        pieChart = new PieChart(context);
        pieChart.setNoDataTextDescription("没有数据");
        pieChart.setDescription("");
        pieChart.setCenterTextSizePixels(0);
        pieChart.setTop(80);
        // 设置隐藏饼图上文字，只显示百分比
        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(0);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleColorTransparent(false);
        pieChart.setTransparentCircleAlpha(1);


        Legend legend= pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(35);
        legend.setTextSize(35);

        PieData pieData=  new PieData();

        PieDataSet pieDataSet = new PieDataSet(null, "");
        pieDataSet.setValueTypeface(Typeface.SANS_SERIF);
        pieDataSet.addColor(Color.RED);
        pieDataSet.addColor(Color.GREEN);
        pieDataSet.setVisible(true);

        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return  new DecimalFormat(".0").format(v)+ "%";
            }
        });

        try {
            for (DataSet set: pieChart.getData().getDataSets())
                set.setDrawValues(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pieData.setValueFormatter(new PercentFormatter());
        pieData.addDataSet(pieDataSet);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueTextSize(30);

        for (int i = 0; i < 2; i++) {
            if(i==0)
            {
                pieData.addEntry(new Entry(victoryNum,i),0);
                pieData.addXValue(strings[i]);
            }
            else
            {
                pieData.addEntry(new Entry(otherNum,i),0);
                pieData.addXValue(strings[i]);
            }
        }

        pieChart.setData(pieData);
        return pieChart;



    }
    public void getDate()
    {
        histories = hisDao.getAllHistoryMessage();
        count = histories.size();
    }
    public void getVictoryNum()
    {
        for(int i=0;i<histories.size();i++)
        {
            if(histories.get(i).getCondition().equals("胜利"))
            {
                victoryNum++;
            }
            else
            {
                otherNum++;
            }
        }
    }
}
