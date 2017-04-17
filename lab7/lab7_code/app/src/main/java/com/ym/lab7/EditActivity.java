package com.ym.lab7;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ym on 16-11-15.
 */

public class EditActivity extends Activity {

    private final String FILE_NAME = "lab7file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button save = (Button)findViewById(R.id.save);
        Button load = (Button)findViewById(R.id.load);
        Button clear = (Button)findViewById(R.id.clear);
        final EditText editText = (EditText)findViewById(R.id.editText);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    String str = editText.getText().toString();
                    fileOutputStream.write(str.getBytes());

                    Toast.makeText(EditActivity.this, "Save successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditActivity.this, "Fail to save file.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fileInputStream = openFileInput(FILE_NAME);
                    byte [] contents = new byte[fileInputStream.available()];
                    fileInputStream.read(contents);

                    editText.setText(new String(contents));

                    Toast.makeText(EditActivity.this, "Load successfully.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditActivity.this, "Fail to load file.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }
}
