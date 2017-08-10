package com.zxn.aipaipai.activity;

import android.content.ContentValues;
import android.database.Cursor;
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

public class CorrectActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar correct_toolbar;
    private TextInputLayout usernameWrapper;
    private EditText etUsername;
    private TextInputLayout passwordWrapper;
    private EditText et_password;
    private Button btnCorrect;
    private MyDataBaseOpenHelper dbHelper;
    private SQLiteDatabase db;
    private TextInputLayout new_passwordWrapper;
    private EditText et_new_password;
    private TextInputLayout new_confirm_passwordWrapper;
    private EditText etNewConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);
        findViews();
        setTextListener();
        initDataBase();
    }

    private void setTextListener() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()<6){
                    usernameWrapper.setError("您的用户名长度小于6");
                }else {
                    usernameWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()<6){
                    new_passwordWrapper.setError("您的密码长度低于6");
                }else {
                    new_passwordWrapper.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        etNewConfirmPassword.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = et_new_password.getText().toString().trim();
                if (!(etNewConfirmPassword.getText().toString().trim()).equals(password)) {
                    new_confirm_passwordWrapper.setError("两次密码不一致,请再此输入");
                } else {
                    new_confirm_passwordWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDataBase() {
        dbHelper = MyDataBaseOpenHelper.getInstance(this);
        db = dbHelper.getReadableDatabase();
    }
    private void findViews() {
        correct_toolbar = (Toolbar) findViewById(R.id.correct_toolbar);
        setSupportActionBar(correct_toolbar);
        //关键下面两句话，设置了回退按钮，及点击事件的效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        correct_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        etUsername =  usernameWrapper.getEditText();
        passwordWrapper= (TextInputLayout) findViewById(R.id.passwordWrapper);
        et_password= passwordWrapper.getEditText();
        new_passwordWrapper = (TextInputLayout) findViewById(R.id.new_passwordWrapper);
        et_new_password = new_passwordWrapper.getEditText();
        new_confirm_passwordWrapper = (TextInputLayout) findViewById(R.id.new_confirm_passwordWrapper);
        etNewConfirmPassword = new_confirm_passwordWrapper.getEditText();
        btnCorrect = (Button) findViewById(R.id.btn_correct);
        btnCorrect.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnCorrect) {
            String username=etUsername.getText().toString().trim();
            String password=et_password.getText().toString().trim();
            Cursor cursor= null;
            try {
                cursor = db.query("UserInfo", new String[]{"username", "password"},"username=?", new String[]{username},null,null,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cursor==null){
                Toast.makeText(this, "您的用户名不存在，请重新输入", Toast.LENGTH_SHORT).show();
            }else {
                while (cursor.moveToNext()){
                    String secretPassword=cursor.getString(cursor.getColumnIndex("password"));
                    if (secretPassword.equals(password)){
                        ContentValues values=new ContentValues();
                        String newPassword=et_new_password.getText().toString().trim();
                        values.put("password",newPassword);
                        db.update("UserInfo",values,"username=?", new String[]{username});
                        Toast.makeText(this, "您的密码修改成功，请返回登陆", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "您的密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            cursor.close();
        }
    }
}
