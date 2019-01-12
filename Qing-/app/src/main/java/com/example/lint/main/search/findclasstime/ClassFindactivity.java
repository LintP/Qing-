package com.example.lint.main.search.findclasstime;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lint.main.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClassFindactivity extends AppCompatActivity {

    List<classTime> list;
    classTime classtime;
    ClassecndAdapter classecndAdapter;

    private static final String findurl = "http://www.plint.top:12580/findclass/LintClass/";
    EditText ed;
    Button btn, btn_back;
    String find;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_find_classend);
        Init();
    }

    protected void Init() {
        ed = findViewById(R.id.find_classend_ed);
        btn = findViewById(R.id.find_calssend_btn);
        btn_back = findViewById(R.id.btn_back);

        list = new ArrayList<classTime>();
        classecndAdapter = new ClassecndAdapter(this, R.layout.classend_time_item, list);
        final ListView listView = findViewById(R.id.find_classend_list);
        listView.setAdapter(classecndAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find = ed.getText().toString();
                if (find.equals("")) {
                    Toast.makeText(getApplicationContext(), "班级不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    list.clear();
                    InitData();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ed.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    btn.callOnClick();
                }
                return false;
            }
        });
    }

    private void InitData() {
        FindTask findTask = new FindTask();
        findTask.execute();
    }


    class FindTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(findurl + find)
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();

                result = getJsonObject(responseData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            btn.setEnabled(false);
            btn_back.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String str) {
            btn.setEnabled(true);
            btn_back.setEnabled(true);

        }

        private String getJsonObject(String jsonData) {
            String result = null;

            try {
                JSONObject json = new JSONObject(jsonData);
                if (json.getString("result").equals("succee")) {

                    JSONArray jsonArray = new JSONArray(json.getString("data"));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        classtime = new classTime();
                        classtime.setUid(jsonObject1.getString("课程编号"));
                        classtime.setInstitute(jsonObject1.getString("开课院系"));
                        classtime.setName(jsonObject1.getString("课程名称"));
                        classtime.setClassname(jsonObject1.getString("学生班级"));
                        classtime.setCount(jsonObject1.getString("人数") + " 人");
                        classtime.setDate(jsonObject1.getString("考试日期"));
                        classtime.setTime(jsonObject1.getString("考试时间"));
                        classtime.setAddr(jsonObject1.getString("楼栋") + jsonObject1.getString
                                ("考试地点"));

                        list.add(classtime);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                classtime = new classTime();
                classtime.setUid("检查一下你的班级咯");
                classtime.setInstitute("无");
                classtime.setName("无考试");
                classtime.setClassname("无");
                classtime.setCount("0人");
                classtime.setDate("无");
                classtime.setTime("无");

                list.add(classtime);
            }
            return result;
        }
    }
}
