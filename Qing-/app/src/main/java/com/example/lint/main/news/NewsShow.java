package com.example.lint.main.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.lint.main.R;

public class NewsShow extends AppCompatActivity {
    private String title,url;
    private Button btn;
    private TextView tx;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_news_show);

        Intent intent=getIntent();
        title=intent.getStringExtra("news_title");
        url=intent.getStringExtra("news_url");

        btn=findViewById(R.id.news_fish_btn);
        tx=findViewById(R.id.news_title_tx);
        webView=findViewById(R.id.news_web);

        tx.setText(title);
        webView.loadUrl(url);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
