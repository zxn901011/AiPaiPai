package com.zxn.aipaipai.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zxn.aipaipai.R;
import com.zxn.aipaipai.db.MyDataBaseOpenHelper;
import com.zxn.aipaipai.utils.Constant;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private MyDataBaseOpenHelper dbHelper;
    private Toolbar register_toolbar;
    private TextInputLayout nameWrapper;
    private EditText etName;
    private TextInputLayout passwordWrapper;
    private EditText etPassword;
    private TextInputLayout confirmWrapper;
    private EditText etConfirm;
    private TextInputLayout phoneNumberWrapper;
    private EditText etPhoneNumber;
    private Button btnRegister;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        setListener();
        initDataBase();
    }

    private void initDataBase() {
        dbHelper=MyDataBaseOpenHelper.getInstance(RegisterActivity.this);
        db=dbHelper.getReadableDatabase();
    }

    private void setListener() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() < 6) {
                    nameWrapper.setError("用户名长度必须超过5");
                } else {
                    nameWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() < 6) {
                    passwordWrapper.setError("密码长度不能小于6");
                } else {
                    passwordWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = etPassword.getText().toString().trim();
                if (!(etConfirm.getText().toString().trim()).equals(password)) {
                    confirmWrapper.setError("两次密码不一致,请再此输入");
                } else {
                    confirmWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //手机号码的正则表达式
                if (!(s.toString().matches(Constant.regularExpression))) {
                    phoneNumberWrapper.setError("请正确输入你的手机号");
                } else {
                    phoneNumberWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void findViews() {
        register_toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(register_toolbar);
        //关键下面两句话，设置了回退按钮，及点击事件的效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        register_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        etName = (EditText) findViewById(R.id.et_name);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        etPassword = (EditText) findViewById(R.id.et_password);
        confirmWrapper = (TextInputLayout) findViewById(R.id.confirmWrapper);
        etConfirm = (EditText) findViewById(R.id.et_confirm);
        phoneNumberWrapper = (TextInputLayout) findViewById(R.id.phoneNumberWrapper);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_umber);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
            String name = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            ContentValues values=new ContentValues();
            values.put("username",name);
            values.put("password",password);
            values.put("phoneNumber",phoneNumber);
            db.insert("UserInfo",null,values);
            values.clear();
            if ((name.length()<6)&&(password.length()<6)&&(!phoneNumber.matches(Constant.regularExpression))){
                Toast.makeText(this, "请先注册您的账号", Toast.LENGTH_SHORT).show();
            }else {
                startLoginActivity();
            }
        }
    }
    private void startLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
