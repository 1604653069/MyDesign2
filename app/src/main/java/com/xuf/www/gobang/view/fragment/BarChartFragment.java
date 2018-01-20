package com.xuf.www.gobang.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xuf.www.gobang.R;
import com.xuf.www.gobang.db.HisDao;
import com.xuf.www.gobang.db.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2018/1/18.
 */

public class BarChartFragment extends Fragment {
    private BarChart barChart;
    private HisDao hisDao = new HisDao();
    private String[] titles ={"人机对战","双人对战","WiFi对战","蓝牙对战"};
    private List<History> histories = new ArrayList<>();
    private int winRobortNum=0;
    private int lostRobortNum=0;
    private int winCoupleNum=0;
    private int lostCoupleNum=0;
    private int winWiFiNum=0;
    private int lostWiFiNum=0;
    private int winToothNum=0;
    private int lostToothNum=0;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        hisDao.openDb(getActivity());
        View view = inflater.inflate(R.layout.barchart_fragment, container, false);
        initView(view);
        initDate();
        BarData date = getDate();
        setBarChart(barChart,date);
        return view;
    }

    private void initDate() {
            histories = hisDao.getAllHistoryMessage();
           for(int i=0;i<histories.size();i++)
           {
                if(histories.get(i).getMode().equals("人机对战"))
                {
                    if(histories.get(i).getCondition().equals("胜利"))
                        winRobortNum++;
                    else
                        lostRobortNum++;
                }
               if(histories.get(i).getMode().equals("双人对战"))
               {
                   if(histories.get(i).getCondition().equals("胜利"))
                       winCoupleNum++;
                   else
                       lostCoupleNum++;
               }
               if(histories.get(i).getMode().equals("WiFi对战"))
               {
                   if(histories.get(i).getCondition().equals("胜利"))
                       winWiFiNum++;
                   else
                       lostWiFiNum++;
               }
               if(histories.get(i).getMode().equals("蓝牙对战"))
               {
                   if(histories.get(i).getCondition().equals("胜利"))
                       winToothNum++;
                   else
                       lostToothNum++;
               }
           }
    }

    private void initView(View view) {
        barChart = (BarChart) view.findViewById(R.id.bar);
    }
    public BarData getDate()
    {
        ArrayList<String> x = new ArrayList<>();
        for(int i=0;i<titles.length;i++)
        {
            x.add(titles[i]);
        }

        ArrayList<BarEntry> y = new ArrayList<>();
        BarEntry barEntry = new BarEntry(winRobortNum,0);
        y.add(barEntry);
        BarDataSet barDataSet = new BarDataSet(y, "");
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return (int)v+"";
            }
        });
        barDataSet.setHighlightEnabled(false);
        barDataSet.setColor(Color.GREEN);

        ArrayList<BarEntry> y1 = new ArrayList<>();
        BarEntry barEntry1 = new BarEntry(lostRobortNum,0);
        y1.add(barEntry1);
        BarDataSet barDataSet1 = new BarDataSet(y1, "");
        barDataSet1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return (int)v+"";
            }
        });
        barDataSet1.setHighlightEnabled(false);
        barDataSet1.setColor(Color.RED);







        ArrayList<BarEntry> y2 = new ArrayList<>();
        BarEntry barEntry2 = new BarEntry(winRobortNum,1);
        y2.add(barEntry2);
        BarDataSet barDataSet2 = new BarDataSet(y2, "");
        barDataSet2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return (int)v+"";
            }
        });
        barDataSet2.setHighlightEnabled(false);
        barDataSet2.setColor(Color.GREEN);

        ArrayList<BarEntry> y3 = new ArrayList<>();
        BarEntry barEntry3 = new BarEntry(winRobortNum,1);
        y3.add(barEntry3);
        BarDataSet barDataSet3 = new BarDataSet(y3, "");
        barDataSet3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return (int)v+"";
            }
        });
        barDataSet3.setHighlightEnabled(false);
        barDataSet3.setColor(Color.RED);




        ArrayList<BarEntry> y4 = new ArrayList<>();
        BarEntry barEntry4 = new BarEntry(winRobortNum,2);
        y4.add(barEntry4);
        BarDataSet barDataSet4 = new BarDataSet(y4, "");
        barDataSet4.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return (int)v+"";
            }
        });
        barDataSet4.setHighlightEnabled(false);
        barDataSet4.setColor(Color.GREEN);

        ArrayList<BarEntry> y5 = new ArrayList<>();
        BarEntry barEntry5 = new BarEntry(winRobortNum,2);
        y5.add(barEntry5);
        BarDataSet barDataSet5 = new BarDataSet(y5, "");
        barDataSet5.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return (int)v+"";
            }
        });
        barDataSet5.setHighlightEnabled(false);
        barDataSet5.setColor(Color.RED);


        ArrayList<BarEntry> y6 = new ArrayList<>();
        BarEntry barEntry6 = new BarEntry(winRobortNum,3);
        y6.add(barEntry6);
        BarDataSet barDataSet6 = new BarDataSet(y6, "");
        barDataSet6.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return (int)v+"";
            }
        });
        barDataSet6.setHighlightEnabled(false);
        barDataSet6.setColor(Color.GREEN);

        ArrayList<BarEntry> y7 = new ArrayList<>();
        BarEntry barEntry7 = new BarEntry(winRobortNum,3);
        y7.add(barEntry7);
        BarDataSet barDataSet7 = new BarDataSet(y7, "");
        barDataSet7.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return (int)v+"";
            }
        });
        barDataSet7.setHighlightEnabled(false);
        barDataSet7.setColor(Color.RED);

        ArrayList<BarDataSet> barDataSets = new ArrayList<>();
        barDataSets.add(barDataSet);
        barDataSets.add(barDataSet1);
        barDataSets.add(barDataSet2);
        barDataSets.add(barDataSet3);
        barDataSets.add(barDataSet4);
        barDataSets.add(barDataSet5);
        barDataSets.add(barDataSet6);
        barDataSets.add(barDataSet7);
        BarData barData = new BarData(x, barDataSets);

        return barData;
    }
    public void setBarChart(BarChart barChart,BarData barData)
    {
        barChart.setDescription("");
        barChart.setData(barData);
        barChart.setDrawGridBackground(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
    }
}
