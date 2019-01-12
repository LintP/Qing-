package com.example.lint.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Instrumentation;

import com.example.lint.main.login.User;
import com.example.lint.main.news.NewsShow;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Fragment fragment_news, fragment_center, fragment_search;
    private LinearLayout linearLayout_news, linearLayout_center, linearLayout_search;
    private ImageView imageView_news, imageView_center, imageView_search, top_img;
    private TextView textView_news, textView_center, textView_search,user_name_tx,user_email_tx,user_type_tx;
    private DrawerLayout drawer;

    private int back_count;

    private String user_name,user_type,user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        InitView();//初始化控件
        InitEvent();//绑定事件
        InitFragment(2);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);//还原图标颜色


        try {
            Intent intent=getIntent();
            user_name=intent.getStringExtra("user_name");
            user_type=intent.getStringExtra("name_type");

            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),"Hello "+user_name,Snackbar.LENGTH_LONG).show();

            if(BmobUser.isLogin()){
                User user=BmobUser.getCurrentUser(User.class);
                user_email=user.getEmail();
            }
        }catch (Exception e){
            user_name=null;
            user_type=null;
            user_email=null;

            e.printStackTrace();
        }

        try{
            user_name_tx.setText(user_name);
            user_type_tx.setText(user_type);
            user_email_tx.setText(user_email);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.self) {//个人中心
            Toast("个人中心");
        } else if (id == R.id.find) {//我找到的
            Toast("我找到的");
        } else if (id == R.id.lose) {//我失去的
            Toast("我失去的");
        } else if (id == R.id.help) {
            Toast("帮助");
        } else if (id == R.id.info) {
            Toast("版本 V1.0");
        } else if (id == R.id.exit) {//退出
            Toast("退出");
            finish();
        } else if (id == R.id.by_home) {//官网
            Toast("官网");
            Intent intent=new Intent(this,NewsShow.class);
            intent.putExtra("news_title","Lint");
            intent.putExtra("news_url","http://www.plint.top");
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_linear_news:
                InitFragment(1);
                break;
            case R.id.btn_linear_center:
                InitFragment(2);
                break;
            case R.id.btn_linear_search:
                InitFragment(3);
                break;
        }

    }

    @Override
    public void onBackPressed() {//重写返回键操作
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawers();
            back_count=0;
        }else{
            if(back_count==0){
                Toast("再按一次返回键退出");
                back_count=1;
            }else{
                back_count=0;
                finish();
            }
        }
    }

    public void Toast(String body) {
        Toast.makeText(this, body, Toast.LENGTH_SHORT).show();
    }

    private void InitView() {
        drawer = findViewById(R.id.drawer_layout);

        linearLayout_news = findViewById(R.id.btn_linear_news);
        linearLayout_center = findViewById(R.id.btn_linear_center);
        linearLayout_search = findViewById(R.id.btn_linear_search);

        imageView_news = findViewById(R.id.btn_news_png);
        imageView_center = findViewById(R.id.btn_center_png);
        imageView_search = findViewById(R.id.btn_search_png);

        top_img = findViewById(R.id.top_img);

        textView_news = findViewById(R.id.btn_news_tx);
        textView_center = findViewById(R.id.btn_center_tx);
        textView_search = findViewById(R.id.btn_search_tx);

        user_name_tx=findViewById(R.id.user_name_tx);
        user_email_tx=findViewById(R.id.user_email_tx);
        user_type_tx=findViewById(R.id.user_type_tx);
    }

    private void InitFragment(int index) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideAllFragment(transaction);
        restartButton();

        switch (index) {
            case 1: {
                imageView_news.setImageResource(R.mipmap.news);
                textView_news.setTextColor(getResources().getColor(R.color.guse));

                top_img.setImageResource(R.mipmap.news_io);

                if (fragment_news == null) {
                    fragment_news = new Fragment_news();
                    transaction.add(R.id.frame_content, fragment_news);
                }
                transaction.show(fragment_news);
                Toast("news show");
            } break;
            case 2: {
                imageView_center.setImageResource(R.mipmap.center);
                textView_center.setTextColor(getResources().getColor(R.color.guse));

                top_img.setImageResource(R.mipmap.center_io);

                if (fragment_center == null) {
                    fragment_center = new Fragment_center();
                    transaction.add(R.id.frame_content, fragment_center);
                }
                transaction.show(fragment_center);
                Toast("center show");
            } break;
            case 3: {
                imageView_search.setImageResource(R.mipmap.search);
                textView_search.setTextColor(getResources().getColor(R.color.guse));

                top_img.setImageResource(R.mipmap.search_io);

                if (fragment_search == null) {
                    fragment_search = new Fragment_search();
                    transaction.add(R.id.frame_content, fragment_search);
                }
                transaction.show(fragment_search);
                Toast("search show");
            } break;
        }
        transaction.commit();//提交事务之后fragment才会更新
    }

    private void hideAllFragment(android.support.v4.app.FragmentTransaction transaction) {
        if (fragment_news != null) {
            transaction.hide(fragment_news);
        }
        if (fragment_center != null) {
            transaction.hide(fragment_center);
        }
        if (fragment_search != null) {
            transaction.hide(fragment_search);
        }
    }

    private void InitEvent() {
        linearLayout_news.setOnClickListener(this);
        linearLayout_center.setOnClickListener(this);
        linearLayout_search.setOnClickListener(this);
    }

    private void restartButton() {
        imageView_news.setImageResource(R.mipmap.unnews);
        imageView_center.setImageResource(R.mipmap.uncenter);
        imageView_search.setImageResource(R.mipmap.unsearch);

        textView_news.setTextColor(getResources().getColor(R.color.gnoumal));
        textView_center.setTextColor(getResources().getColor(R.color.gnoumal));
        textView_search.setTextColor(getResources().getColor(R.color.gnoumal));
    }
}
