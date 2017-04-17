package com.ym.lab3project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ym on 16-10-12.
 */

public class ContactActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        RelativeLayout header = (RelativeLayout)findViewById(R.id.header);
        ListView optionListView = (ListView)findViewById(R.id.optionListView);
        TextView name = (TextView)findViewById(R.id.name);
        TextView telNum = (TextView)findViewById(R.id.telNum);
        TextView typeAndLocation = (TextView)findViewById(R.id.typeAndLocation);
        ImageButton back = (ImageButton)findViewById(R.id.back);
        ImageButton star = (ImageButton)findViewById(R.id.star);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("编辑联系人");
        arrayAdapter.add("分享联系人");
        arrayAdapter.add("加入黑名单");
        arrayAdapter.add("删除联系人");

        optionListView.setAdapter(arrayAdapter);

        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        name.setText(contact.name);
        telNum.setText(contact.telNum);
        typeAndLocation.setText(contact.type+" "+contact.location);
        int rgbValue = (int)Math.pow(16, 6)*(16*15+15) + Integer.valueOf(contact.bgColor, 16);
        header.setBackgroundColor(rgbValue);
//        Log.i("XXX", Integer.toHexString(rgbValue));
//        header.setBackgroundColor(Integer.valueOf("FF"+contact.bgColor, 16));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        star.setTag(0);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)view.getTag() == 0) {
                    view.setBackground(getResources().getDrawable(R.drawable.full_star));
                    view.setTag(1);
                }
                else {
                    view.setBackground(getResources().getDrawable(R.drawable.empty_star));
                    view.setTag(0);
                }
            }
        });
    }

}
