package com.ym.lab4;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ym on 16-10-24.
 */

public class Activity2 extends AppCompatActivity {

    private BroadcastReceiver receiver2 = new Receiver2();
    private BroadcastReceiver lab5Widget = new Lab5Widget();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        final EditText editText = (EditText)findViewById(R.id.edit_text);
        Button toggle = (Button)findViewById(R.id.toggle);
        Button send = (Button)findViewById(R.id.send);

        toggle.setTag(0);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.ym.lab4.receiver2");

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button)v;
                if ((int)button.getTag() == 0) {
                    button.setText("Unregister Broadcast");
                    button.setTag(1);

                    registerReceiver(receiver2, intentFilter);
                    registerReceiver(lab5Widget, intentFilter);
                }
                else {
                    button.setText("Register Broadcast");
                    button.setTag(0);

                    unregisterReceiver(receiver2);
                    unregisterReceiver(lab5Widget);
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.ym.lab4.receiver2");
                intent.putExtra("content", editText.getText().toString());
                sendBroadcast(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver2);
        unregisterReceiver(lab5Widget);
        super.onDestroy();
    }

}
