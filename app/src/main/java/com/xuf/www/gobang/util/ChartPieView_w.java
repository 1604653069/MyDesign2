package com.xuf.www.gobang.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xuf.www.gobang.db.HisDao;
import com.xuf.www.gobang.db.History;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**图表类
 * Created by lenovo on 2017/11/9.
 */

public class ChartPieView_w {
    private HisDao hisDao = new HisDao();
    private List<History> histories = new ArrayList<>();
    private int vNum=0;
    private int dNum=0;
    PieChart pieChart;
    public PieChart ChartPieView(Context context){
        pieChart=new PieChart(context);
        hisDao.openDb(context);
        histories = hisDao.getAllHistoryMessage();
        pieChart.setNoDataText("没有数据");
        pieChart.setDescription("");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(20f);
         pieChart.setCenterTextSizePixels(40f);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setDrawHoleEnabled(false);
        pieChart.animateXY(1000, 1000);//设置动画效果
       /* pieChart.setDescriptionTextSize(20f);*/
        pieChart.setAnimationCacheEnabled(true);
        pieChart.setTransparentCircleAlpha(125);
        pieChart.setRotationEnabled(true);

        pieChart.getLegend().setEnabled(false);
        getNum();
        initdate();
        return pieChart;
    }


    private int[] color=new int[]{Color.parseColor("#66ccff"), Color.parseColor("#ffff66")};
    private String[] title=new String[]{"胜","败"};
    private void initdate() {
        List<Entry> list=new ArrayList<>();

        PieData pieData = pieChart.getData();
        if (pieData==null)
            pieData=new PieData(title);
           PieDataSet pieDataSet;

           list.add(new Entry(vNum,0));
           list.add(new Entry(dNum,1));

            pieDataSet=new PieDataSet(list,"测试数据");
            pieDataSet.setValueTextSize(20f);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setColors(color);
            pieDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                    return (int)v+"";
                }
            });
            pieData.setDataSet(pieDataSet);
            pieData.setValueTextSize(20f);
            pieData.setValueTextColor(Color.BLACK);

            pieChart.setData(pieData);

            pieDataSet.notifyDataSetChanged();
            pieData.notifyDataChanged();
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();

    }
    public void getNum()
    {
        for(int i=0;i<histories.size();i++)
        {
            if(histories.get(i).getCondition().equals("胜利"))
                vNum++;
            else
                dNum++;
        }
    }
}
