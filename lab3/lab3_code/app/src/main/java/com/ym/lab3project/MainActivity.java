package com.ym.lab3project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView contactListView;

    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactListView = (ListView)findViewById(R.id.contactListView);

        contactList.add(new Contact("Aaron", "17715523654", "手机", "江苏苏州电信", "BB4C3B"));
        contactList.add(new Contact("Elvis", "18825653224", "手机", "广东揭阳移动", "c48d30"));
        contactList.add(new Contact("David", "15052116654", "手机", "江苏无锡移动", "4469b0"));
        contactList.add(new Contact("Edwin", "18854211875", "手机", "山东青岛移动", "20A17B"));
        contactList.add(new Contact("Frank", "13955188541", "手机", "安徽合肥移动", "BB4C3B"));
        contactList.add(new Contact("Joshua", "13621574410", "手机", "江苏苏州移动", "BB4C3B"));
        contactList.add(new Contact("Ivan", "15684122771", "手机", "山东烟台联通", "4469b0"));
        contactList.add(new Contact("Mark", "17765213579", "手机", "广东珠海电信", "20A17B"));
        contactList.add(new Contact("Joseph", "13315466578", "手机", "河北石家庄电信", "BB4C3B"));
        contactList.add(new Contact("Phoebe", "17895466428", "手机", "山东东营移动", "c48d30"));

//        final List<Map<String, Object>> dataSource = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("chara", (contactList.get(i).name).charAt(0));
//            map.put("name", contactList.get(i).name);
//            dataSource.add(map);
//        }
//        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, dataSource, R.layout.contact_list_view_item,
//                new String[] {"chara", "name"},
//                new int[] {R.id.chara, R.id.name});
//        contactListView.setAdapter(simpleAdapter);

        contactAdapter = new ContactAdapter(contactList, this);
        contactListView.setAdapter(contactAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                intent.putExtra("contact", contactList.get(i));
                startActivity(intent);
            }
        });
        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int index = i;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("删除联系人")
                        .setMessage("确定删除联系人"+(String) ((Contact)adapterView.getItemAtPosition(i)).name+"?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                contactList.remove(index);
                                contactAdapter.notifyDataSetChanged();
//                                contactList.remove(index);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();

                return true;
            }
        });
    }

    private class ContactAdapter extends BaseAdapter {

        private List<Contact> list;
        private Context context;

        private ContactAdapter(List<Contact> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            if (list == null) {
                return null;
            }
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View convertView;
            ViewHolder viewHolder;

            if (view == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.contact_list_view_item, null);
                viewHolder = new ViewHolder();
                viewHolder.chara = (TextView)convertView.findViewById(R.id.chara);
                viewHolder.name = (TextView)convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            }
            else {
                convertView = view;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.chara.setText(list.get(i).name.charAt(0)+"");
            viewHolder.name.setText(list.get(i).name);

            return convertView;
        }

        private class ViewHolder {
            public TextView chara;
            public TextView name;
        }
    }
}
