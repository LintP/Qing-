package com.example.lint.main.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lint.main.MainActivity;
import com.example.lint.main.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends AppCompatActivity {

    TextView register,phonelogin;
    EditText user_ed,pwd_ed;
    CheckBox cb;
    Button sub_btn;

    String user_str,pwd_str;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bmob.initialize(this, "bb0112c3f4a5f1534d14ea5f24149f43");
        setContentView(R.layout.activity_login);

        register=findViewById(R.id.register);
        phonelogin=findViewById(R.id.phonelogin);
        user_ed=findViewById(R.id.user_ed);
        pwd_ed=findViewById(R.id.pwd_ed);
        cb=findViewById(R.id.cb);
        sub_btn=findViewById(R.id.sub_btn);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();

        boolean remenber=sharedPreferences.getBoolean("remember",false);
        if(remenber){
            user_ed.setText(sharedPreferences.getString("user",""));
            pwd_ed.setText(sharedPreferences.getString("pwd",""));
            cb.setChecked(true);
        }

        register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//添加下划线

        register.setOnClickListener(new View.OnClickListener() {//调用注册
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        phonelogin.setOnClickListener(new View.OnClickListener() {//手机号登陆
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), phoneLogin.class));
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_str=user_ed.getText().toString();
                pwd_str=pwd_ed.getText().toString();
                if(user_str.length()>4&&pwd_str.length()>9){

                    Toast("登陆中！");

                    if(cb.isChecked()){
                        editor.putBoolean("remember",true);
                        editor.putString("user",user_str);
                        editor.putString("pwd",pwd_str);
                    }else{
                        editor.clear();
                    }
                    editor.apply();

                    User user=new User();
                    user.setUsername(user_str);
                    user.setPassword(pwd_str);
                    Toast("dd");

                    user.login(new SaveListener<User>() {
                        @Override
                        public void done(User bmobUser, BmobException e){
                            if(e==null){
                                Toast("登陆成功");

                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("user_name",user_str);
                                if(user_str.equals("admin")||user_str.equals("LintQG")){
                                    intent.putExtra("user_type","管理员");
                                }else {
                                    intent.putExtra("user_type","普通用户");
                                }

                                startActivity(intent);
                                finish();
                            }else{
                                Toast(e.toString());
                            }
                        }
                    });

                }else{
                    Toast("请检查用户名或密码！");
                }
            }
        });
    }
    protected void Toast(String body){
        Toast.makeText(this.getApplicationContext(),body,Toast.LENGTH_SHORT).show();
    }
}
