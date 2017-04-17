package com.ym.lessionproject1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by ym on 16-10-9.
 */
public class Activity2 extends Activity {

    private LinearLayout rootView;
    private TextInputLayout usernameInput;
    private TextInputLayout passwordInput;

    private Button login_btn;
    private Button register_btn;
    private EditText username;
    private EditText password;
    private RadioGroup chose_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        rootView = (LinearLayout)findViewById(R.id.root_view);
        usernameInput = (TextInputLayout)findViewById(R.id.username_input);
        passwordInput = (TextInputLayout)findViewById(R.id.password_input);


        login_btn = (Button)findViewById(R.id.login_btn);
        register_btn = (Button)findViewById(R.id.register_btn);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        chose_group = (RadioGroup)findViewById(R.id.chose_group);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")) {
                    usernameInput.setErrorEnabled(true);
                    passwordInput.setErrorEnabled(false);
                    usernameInput.setError("用户名不能为空");
                    return;
                }
                if (password.getText().toString().equals("")) {
                    usernameInput.setErrorEnabled(false);
                    passwordInput.setErrorEnabled(true);
                    passwordInput.setError("密码不能为空");
                    return;
                }

                usernameInput.setErrorEnabled(false);
                passwordInput.setErrorEnabled(false);
                if (username.getText().toString().equals("Android") && password.getText().toString().equals("123456")) {
                    showMessage("登录成功");
                }
                else {
                    showMessage("登录失败");
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
        Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT)
                .setAction("按钮", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Activity2.this, "Snackbar的按钮被点击了", Toast.LENGTH_SHORT).show();
                    }
                })
                .setActionTextColor(Color.BLUE)
                .show();
    }

}
