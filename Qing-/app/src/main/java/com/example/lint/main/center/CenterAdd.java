package com.example.lint.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lint.main.R;

import org.w3c.dom.Text;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CenterAdd extends AppCompatActivity {
    
    EditText addr_ed, title_ed, body_ed, user_ed, tel_ed;
    Button sub_btn, filish_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_center_add);

        InitView();

        filish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    private void InitView() {

        addr_ed = findViewById(R.id.center_add_addr_tx);
        title_ed = findViewById(R.id.center_add_title_tx);
        body_ed = findViewById(R.id.center_add_boby_tx);
        user_ed = findViewById(R.id.center_add_user_tx);
        tel_ed = findViewById(R.id.center_add_tel_tx);

        filish_btn = findViewById(R.id.center_add_fish_btn);
        sub_btn = findViewById(R.id.center_add_sub_btn);
    }

    public void addData(){

        //addr_ed, title_ed, body_ed, user_ed, tel_ed;
        String addr,title,body,user,tel;

        addr=addr_ed.getText().toString();
        title=title_ed.getText().toString();
        body=body_ed.getText().toString();
        user=user_ed.getText().toString();
        tel=tel_ed.getText().toString();

        if(addr.equals("")
                ||title.equals("")
                ||body.equals("")
                ||user.equals("")
                ||tel.equals("")){
            Toast.makeText(getApplication(),"请填写全部信息",Toast.LENGTH_SHORT).show();
        }else{
            CenterBomb bean=new CenterBomb();

            bean.setTitle(title);
            bean.setBody(body);
            bean.setTel(tel);
            bean.setAddr(addr);
            bean.setUser(user);
            bean.setState(false);

            bean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        Toast.makeText(getApplicationContext(),"发布成功"+s,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"发布失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
