package com.ym.lab8;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ym on 16-11-22.
 */

public class CreateActivity extends AppCompatActivity {

    private Button add;
    private EditText name;
    private EditText birthday;
    private EditText gift;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        add = (Button) findViewById(R.id.add);
        name = (EditText) findViewById(R.id.name);
        birthday = (EditText) findViewById(R.id.birthday);
        gift = (EditText) findViewById(R.id.gift);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_text = name.getText().toString();
                String birthday_text = birthday.getText().toString();
                String gift_text = gift.getText().toString();

                if (name_text.length() == 0) {
                    Toast.makeText(CreateActivity.this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
                    return;
                }

                myDB db = new myDB(getBaseContext());
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from " + myDB.TABLE_NAME + " where name like ?", new String[]{name_text});

                if (cursor.moveToFirst()) {
                    Toast.makeText(CreateActivity.this, "名字重复啦，请核查", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("name", name_text);
                contentValues.put("birthday", birthday_text);
                contentValues.put("gift", gift_text);
                sqLiteDatabase.insert(myDB.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();
                setResult(RESULT_OK, new Intent());
                finish();

            }
        });
    }
}
