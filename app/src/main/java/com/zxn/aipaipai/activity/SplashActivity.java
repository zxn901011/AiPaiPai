package com.zxn.aipaipai.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zxn.aipaipai.R;
import com.zxn.aipaipai.db.MyDataBaseOpenHelper;


public class SplashActivity extends AppCompatActivity {
    private TextInputLayout usernameWrapper;
    private EditText et_username;
    private TextInputLayout passwordWrapper;
    private EditText et_password;
    private Button btn_register;
    private Button btn_login;
    private Button btn_correct;
    private MyDataBaseOpenHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViews();
        initDataBase();
    }

    private void initDataBase() {
        dbHelper = MyDataBaseOpenHelper.getInstance(this);
        db = dbHelper.getReadableDatabase();
    }

    private void findViews() {
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        et_username = usernameWrapper.getEditText();
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        et_password = passwordWrapper.getEditText();
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_correct = (Button) findViewById(R.id.btn_correct);
        btn_correct.setOnClickListener(new MyClickListener());
        btn_login.setOnClickListener(new MyClickListener());
        btn_register.setOnClickListener(new MyClickListener());
        et_username.addTextChangedListener(new MyTextChangedListener());
    }

    class MyTextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().length() < 6) {
                usernameWrapper.setError("用户名长度必须超过6个");
            } else {
                usernameWrapper.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register:
                    startRegisterActivity();
                    break;
                case R.id.btn_login:
                    startLoginActivity();
                    break;
                case R.id.btn_correct:
                    startCorrectActivity();
            }
        }
    }

    private void startCorrectActivity() {
        Intent intent = new Intent(SplashActivity.this, CorrectActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Cursor cursor = null;
        String phoneNumber=et_username.getText().toString().trim();
        try {
            cursor = db.query("UserInfo", new String[]{"phoneNumber","password"}, "phoneNumber=?", new String[]{phoneNumber}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            Toast.makeText(this, "用户名不存在", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (cursor.moveToNext()) {
                String password = cursor.getString(cursor.getColumnIndex("password"));
                Log.e("MainActivity=====", password);
                if (password.equals(et_password.getText().toString().trim())) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "您的密码错误", Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
        }
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
