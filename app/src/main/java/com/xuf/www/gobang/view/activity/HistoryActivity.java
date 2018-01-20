package com.xuf.www.gobang.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xuf.www.gobang.R;
import com.xuf.www.gobang.db.HisDao;
import com.xuf.www.gobang.db.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView listView;
    private HisDao hisDao = new HisDao();
    private List<History> histories = new ArrayList<>();
    private MyAdapter adapter;
    private Spinner spinner;
    private String[] titles = {"全部","人机对战","双人对战","WiFi对战","蓝牙对战"};
    private List<String> list = new ArrayList<>();
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        noTitle();
        initView();
        initDate();
        initListener();
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(HistoryActivity.this,ResultActivity.class));
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               position = i;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(HistoryActivity.this, "哈哈", Toast.LENGTH_SHORT).show();
                showDialog2(i);
                return true;
            }
        });
    }

    private void initDate() {
        histories = hisDao.getAllHistoryMessage();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        for(int i=0;i<titles.length;i++)
        {
            list.add(titles[i]);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, list);
        spinner.setAdapter(adapter1);

    }

    private void initView() {
        spinner = (Spinner) this.findViewById(R.id.sp);
        listView = (ListView) this.findViewById(R.id.listview);
        hisDao.openDb(this);
    }
    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return histories.size();
        }

        @Override
        public Object getItem(int i) {
            return histories.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.listview_item, null);
            TextView condition = (TextView) view.findViewById(R.id.item_condition);
            TextView mode = (TextView) view.findViewById(R.id.item_mode);
            ImageView mycolor = (ImageView) view.findViewById(R.id.item_mine_img);
            ImageView hicolor = (ImageView) view.findViewById(R.id.item_other_img);
            TextView tiem = (TextView) view.findViewById(R.id.item_time);
            TextView date = (TextView) view.findViewById(R.id.item_date);
            condition.setText(histories.get(i).getCondition());
            tiem.setText("耗时："+histories.get(i).getTime()+"s");
            mode.setText(histories.get(i).getMode());
            if (histories.get(i).getCondition().equals("失败"))
              condition.setTextColor(Color.RED);
            if(histories.get(i).getMycolor().equals("white"))
            {
                mycolor.setImageResource(R.mipmap.chess_white);
                hicolor.setImageResource(R.mipmap.chess_black);
            }
            else
            {
                mycolor.setImageResource(R.mipmap.chess_black);
                hicolor.setImageResource(R.mipmap.chess_white);
            }
            date.setText(histories.get(i).getDate());
            if(position!=0)
            {
                if(!titles[position].equals(histories.get(i).getMode()))
                {
                    view.setVisibility(View.GONE);
                    
                    return new View(HistoryActivity.this);
                }
            }
            return view;
        }
    }
    public void noTitle()
    {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
    }
    public void showDialog2(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定要删除这条对战记录吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hisDao.deleteHistory(histories.get(position).getUid());
                        histories.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(HistoryActivity.this, "对战记录一删除", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }
}
