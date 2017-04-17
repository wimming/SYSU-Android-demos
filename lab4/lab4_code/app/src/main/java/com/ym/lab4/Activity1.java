package com.ym.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym on 16-10-24.
 */

public class Activity1 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);

        ListView listView = (ListView)findViewById(R.id.list_view);

        List<Map<String, Object>> dataSource = new ArrayList<>();
        Map<String, Object> map;
        for (int i = 0; i < FruitsModel.getInstance().data.size(); ++i) {
            map = new HashMap<>();
            map.put("image", FruitsModel.getInstance().data.get(i).res);
            map.put("name", FruitsModel.getInstance().data.get(i).name);
            dataSource.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(Activity1.this, dataSource, R.layout.list_item, new String [] {"image", "name"}, new int[] {R.id.image, R.id.name});

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("com.ym.lab4.receiver1");
                intent.putExtra("index", position);
                sendBroadcast(intent);
            }
        });
    }
}
