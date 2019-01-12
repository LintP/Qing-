package com.example.lint.main.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lint.main.R;

public class MainActivity extends AppCompatActivity {

    private boolean isFristUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences preferences=getSharedPreferences("isFirstUse",0);
        isFristUse=preferences.getBoolean("isFirstUse",true);

        if(isFristUse){
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isFirstUse",false);
            editor.commit();

            startActivity(new Intent(getApplicationContext(), GuideActivity.class));
        }else{
            startActivity(new Intent(getApplicationContext(), HandlerActivity.class));
        }
        finish();
    }
}
