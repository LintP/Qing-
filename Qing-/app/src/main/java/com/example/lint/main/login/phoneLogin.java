package com.example.lint.main.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lint.main.MainActivity;
import com.example.lint.main.R;

import org.json.JSONObject;

import java.io.IOException;

import cn.bmob.v3.Bmob;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class phoneLogin extends AppCompatActivity {

    TextView userlogin;
    EditText phone_ed,code_ed;
    Button ver_btn,sub_btn;

    String phone_str,code_str,url_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bmob.initialize(this, "bb0112c3f4a5f1534d14ea5f24149f43");
        setContentView(R.layout.activity_phonelogin);

        userlogin=findViewById(R.id.userlogin);
        phone_ed=findViewById(R.id.phone_ed);
        code_ed=findViewById(R.id.ver_ed);
        ver_btn=findViewById(R.id.ver_btn);
        sub_btn=findViewById(R.id.sub_btn);

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_str=phone_ed.getText().toString();
                if(phone_str.length()==11){
                    url_str="http://119.23.47.236:9090/sms/androidJsonSMS@Lint/"+phone_str;
                    SMSTask smsTask=new SMSTask();
                    smsTask.execute();
                }else{
                    Toast("请检查手机号");
                }
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code_ed.getText().toString().equals(code_str)){
                    Toast("验证码校验成功");

                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("user_name",phone_str);
                    intent.putExtra("user_type","手机用户");

                    startActivity(intent);
                    finish();
                }else{
                    Toast("请检查验证码");
                }
            }
        });
    }

    protected void Toast(String body){
        Toast.makeText(this.getApplicationContext(),body,Toast.LENGTH_SHORT).show();
    }
    class SMSTask extends AsyncTask<Void,Integer,String> {//验证码
        @Override
        protected String doInBackground(Void[] params){
            String result=null;
            try{
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url(url_str)
                        .build();
                Response response=client.newCall(request).execute();
                String responseData=response.body().string();

                result=getJsonObject(responseData);
            }catch (IOException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            ver_btn.setText("请稍后");
            ver_btn.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String str){
            ver_btn.setText("获取验证码");
            ver_btn.setEnabled(true);
            Toast(str);//测试
        }

        private String getJsonObject(String jsonData){
            String result=null;
            String phone,code,state;

            try{
                JSONObject jsonObject=new JSONObject(jsonData);
                phone=jsonObject.getString("phone");
                code=jsonObject.getString("code");
                state=jsonObject.getString("errmsg");

                code_str=code;//取得验证码

                result=phone+" "+code+" "+state+"\n\r";
            }catch (Exception e){
                e.printStackTrace();

            }
            return result;
        }
    }

}
