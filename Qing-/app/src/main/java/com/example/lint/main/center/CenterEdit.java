package com.example.lint.main.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lint.main.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class CenterEdit extends AppCompatActivity {
    String uid;
    TextView state_tx,time_tx,addr_tx,title_tx,body_tx,user_tx,tel_tx;
    Button sub_btn,filish_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");

        setContentView(R.layout.activity_center_edit);

        InitView();
        getData();

        filish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void InitView(){
        state_tx=findViewById(R.id.center_edit_state_tx);
        time_tx=findViewById(R.id.center_edit_time_tx);
        addr_tx=findViewById(R.id.center_edit_addr_tx);
        title_tx=findViewById(R.id.center_edit_title_tx);
        body_tx=findViewById(R.id.center_edit_boby_tx);
        user_tx=findViewById(R.id.center_edit_user_tx);
        tel_tx=findViewById(R.id.center_edit_tel_tx);

        filish_btn=findViewById(R.id.center_edit_fish_btn);
        sub_btn=findViewById(R.id.center_edit_sub_btn);
    }

    public void getData(){
        BmobQuery<CenterBomb> bmobQuery=new BmobQuery<>();
        bmobQuery.getObject(uid, new QueryListener<CenterBomb>() {
            @Override
            public void done(CenterBomb test, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"查询数据成功",Toast.LENGTH_SHORT).show();
                    setInfo(test);
                }
                else{
                    Toast.makeText(getApplicationContext(),"查询数据失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setInfo(CenterBomb centerBomb){

        if(centerBomb.getState()){
            state_tx.setText("已认领");
            sub_btn.setEnabled(false);
        }else {
            state_tx.setText("待认领");
        }

        time_tx.setText(centerBomb.getCreatedAt());
        title_tx.setText(centerBomb.getTitle());
        tel_tx.setText(centerBomb.getTel());
        body_tx.setText(centerBomb.getBody());
        user_tx.setText(centerBomb.getUser());
        addr_tx.setText(centerBomb.getAddr());
    }

    private void update() {
        CenterBomb category = new CenterBomb();
        category.setState(true);

        category.update(uid, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplication(),"认领成功",Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("BMOB", e.toString());
                    Toast.makeText(getApplication(),"认领失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
