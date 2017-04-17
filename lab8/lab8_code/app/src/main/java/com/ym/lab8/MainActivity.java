package com.ym.lab8;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button add;

    private List<Map<String, String>> dataSource;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        add = (Button) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivityForResult(intent, 999);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_dialog, null);


                final TextView name = ((TextView)dialogView.findViewById(R.id.name));
                final TextView birthday = ((TextView)dialogView.findViewById(R.id.birthday));
                final TextView gift = ((TextView)dialogView.findViewById(R.id.gift));
                final TextView phone = ((TextView)dialogView.findViewById(R.id.phone));

                name.setText(dataSource.get(position).get("name"));
                birthday.setText(dataSource.get(position).get("birthday"));
                gift.setText(dataSource.get(position).get("gift"));
                phone.setText(dataSource.get(position).get("phone"));


                String str1 = "";
                Cursor cursor1 = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
                while (cursor1.moveToNext()) {
                    String str2 = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    if (cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            .equals(name.getText().toString())) {

                        if (Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor cursor2 = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + str2,
                                    null,
                                    null
                            );
                            while (cursor2.moveToNext()) {
                                str1 = str1 + cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "\n";
                            }
                            cursor2.close();
                        }
                    }
                }
                cursor1.close();
                phone.setText(str1.equals("") ? "无" : str1);

                builder.setView(dialogView);
                builder.setTitle("-.-")
                        .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (name.length() != 0) {
                                    myDB db = new myDB(getBaseContext());
                                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                                    sqLiteDatabase.execSQL("update " + myDB.TABLE_NAME +
                                            " set birthday = ? where name = ?", new Object[]{
                                            birthday.getText().toString(), name.getText().toString()});
                                    sqLiteDatabase.close();
                                }
                                if (gift.length() != 0) {
                                    myDB db = new myDB(getBaseContext());
                                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                                    sqLiteDatabase.execSQL("update " + myDB.TABLE_NAME +
                                            " set gift = ? where name = ?", new Object[]{
                                            gift.getText().toString(), name.getText().toString()});
                                    sqLiteDatabase.close();
                                }

                                renderViewWithDB();
                            }
                        })
                        .setNegativeButton("放棄修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("是否删除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDB db = new myDB(getBaseContext());
                                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                                sqLiteDatabase.execSQL("delete from " + myDB.TABLE_NAME
                                        + " where name = ?", new String[]{dataSource.get(position).get("name")});
                                sqLiteDatabase.close();
                                dataSource.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                return true;
            }
        });


        renderViewWithDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999) {
            if (resultCode == RESULT_OK) {
                renderViewWithDB();
            }
        }
    }
    private void renderViewWithDB() {
        try {
            myDB db = new myDB(getBaseContext());
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from "
                    + myDB.TABLE_NAME, null);
            dataSource = new ArrayList<Map<String, String>>();

            while (cursor.moveToNext()) {
                String nameInfo = cursor.getString(0);
                String birthInfo = cursor.getString(1);
                String giftInfo = cursor.getString(2);
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", nameInfo);
                map.put("birthday", birthInfo);
                map.put("gift", giftInfo);
                dataSource.add(map);
            }

            adapter = new SimpleAdapter(this, dataSource, R.layout.list_item,
                    new String[]{"name", "birthday", "gift"},
                    new int[]{R.id.name, R.id.birthday, R.id.gift});
            listView.setAdapter(adapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
