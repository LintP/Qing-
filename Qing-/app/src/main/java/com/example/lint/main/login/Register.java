package com.example.lint.main.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lint.main.R;

import org.json.JSONObject;

import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    EditText user_ed,pwd_ed,phone_ed,ver_ed;
    Button sub_btn,ver_btn;

    String user_str,pwd_str,phone_str;
    String code_str,url_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bmob.initialize(this, "bb0112c3f4a5f1534d14ea5f24149f43");
        setContentView(R.layout.activity_register);

        user_ed=findViewById(R.id.user_ed);
        pwd_ed=findViewById(R.id.pwd_ed);
        phone_ed=findViewById(R.id.phone_ed);
        ver_ed=findViewById(R.id.ver_ed);
        sub_btn=findViewById(R.id.sub_btn);
        ver_btn=findViewById(R.id.ver_btn);

        ver_btn.setOnClickListener(new View.OnClickListener() {//获取验证码
            @Override
            public void onClick(View v) {
                if(getInfo()){
                    url_str="http://119.23.47.236:9090/sms/androidJsonSMS@Lint/"+phone_str;
                    SMSTask smsTask=new SMSTask();
                    smsTask.execute();
                }else{
                    Toast("请填写全部信息\n\rPS：用户名不得少于5个字符，密码不得少于10个字符，手机号必须为11位中国大陆手机号码！");
                }
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {//提交注册信息
            @Override
            public void onClick(View v) {

                if(cmpInfo()){
                    Toast("基本信息已修改，请重新获取手机验证码进行验证");
                }else{
                    if(ver_ed.getText().toString().equals(code_str)){
                        Toast("正在注册");
                        User user=new User();
                        user.setUsername(user_str);
                        user.setPassword(pwd_str);
                        user.setMobilePhoneNumber(phone_str);
                        user.setMobilePhoneNumberVerified(true);

                        user.signUp(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e){
                                if(e==null){
                                    Toast("注册成功");
                                    finish();
                                }else{
                                    Toast("注册失败"+e.toString());
                                }
                            }
                        });
                    }else{
                        Toast("请检查手机验证码");
                    }
                }
            }
        });
    }

    protected boolean getInfo(){//获取信息
        user_str=user_ed.getText().toString();
        pwd_str=pwd_ed.getText().toString();
        phone_str=phone_ed.getText().toString();

        if(user_str.length()>4
                &&pwd_str.length()>9
                &&phone_str.length()==11){
            return true;
        }
        return false;
    }

    protected boolean cmpInfo(){//判断信息是否更改 true表示已更改 false表示未更改
        if(user_str.equals(user_ed.getText().toString())
                &&pwd_str.equals(pwd_ed.getText().toString())
                &&phone_str.equals(phone_ed.getText().toString())){
            return false;
        }
        return true;
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
