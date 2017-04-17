package com.ym.lessionproject1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Button login_btn;
    private Button register_btn;
    private EditText username;
    private EditText password;
    private RadioGroup chose_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn = (Button)findViewById(R.id.login_btn);
        register_btn = (Button)findViewById(R.id.register_btn);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        chose_group = (RadioGroup)findViewById(R.id.chose_group);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")) {
                    showMessage("用户名不能为空");
                    return;
                }
                if (password.getText().toString().equals("")) {
                    showMessage("密码不能为空");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showMessage("对话框“确定”按钮被点击");
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showMessage("对话框“取消”按钮被点击");
                    }
                }).setTitle("提示");
                if (username.getText().toString().equals("Android") && password.getText().toString().equals("123456")) {
                    builder.setMessage("登录成功");
                    builder.create().show();
                }
                else {
                    builder.setMessage("登录失败");
                    builder.create().show();
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "身份注册功能尚未开启";
                switch (chose_group.getCheckedRadioButtonId()) {
                    case R.id.rdbtn1:
                        text = "学生"+text;
                        break;
                    case R.id.rdbtn2:
                        text = "老师"+text;
                        break;
                    case R.id.rdbtn3:
                        text = "社团"+text;
                        break;
                    case R.id.rdbtn4:
                        text = "管理者"+text;
                        break;
                }
                showMessage(text);
            }
        });

        chose_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String text = "身份被选中";
                switch (i) {
                    case R.id.rdbtn1:
                        text = "学生"+text;
                        break;
                    case R.id.rdbtn2:
                        text = "老师"+text;
                        break;
                    case R.id.rdbtn3:
                        text = "社团"+text;
                        break;
                    case R.id.rdbtn4:
                        text = "管理者"+text;
                        break;
                }
                showMessage(text);
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
