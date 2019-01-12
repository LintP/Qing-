package com.example.lint.main.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.lint.main.R;
import com.example.lint.main.login.Login;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] imageindex=new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.start};
    private ArrayList<ImageView>imageViewArrayList;
    private LinearLayout llConteiner;
    private ImageView imageView;
    private int paintdis;
    private Button startbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        initView();
    }

    protected void initView() {
        viewPager = findViewById(R.id.vp_guide);
        llConteiner = findViewById(R.id.ll_container);
        imageView = findViewById(R.id.iv_red);
        startbtn = findViewById(R.id.start_btn);

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        initData();
        GuideAdepter adepter = new GuideAdepter();

        viewPager.setAdapter(adepter);

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                paintdis = llConteiner.getChildAt(1).getLeft() - llConteiner.getChildAt(0).getLeft();
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int letfMargin=(int)(paintdis*v+i*paintdis);
                RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
                params.leftMargin=letfMargin;
                imageView.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {
                if(i==imageViewArrayList.size()-1){
                    startbtn.setVisibility(View.VISIBLE);
                }else{
                    startbtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    class GuideAdepter extends PagerAdapter{
        @Override
        public int getCount(){
            return imageViewArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view,Object object){
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container,int position){
            ImageView view=imageViewArrayList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container,int position,Object object){
            container.removeView((View)object);
        }
    }

    protected void initData(){
        imageViewArrayList=new ArrayList<>();
        for(int i=0;i<imageindex.length;i++){
            ImageView view=new ImageView(this);
            view.setBackgroundResource(imageindex[i]);
            imageViewArrayList.add(view);
            ImageView pointView=new ImageView(this);
            pointView.setImageResource(R.drawable.shape_point1);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i>0){
                params.leftMargin=20;
            }
            pointView.setLayoutParams(params);
            llConteiner.addView(pointView);
        }
    }
}
