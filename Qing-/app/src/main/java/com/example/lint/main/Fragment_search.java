package com.example.lint.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.lint.main.news.NewsShow;
import com.example.lint.main.search.Search;
import com.example.lint.main.search.SearchAdapter;
import com.example.lint.main.search.findclasstime.ClassFindactivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_search extends Fragment{

    List<Search> list;
    Search search;

    private static final int BAIDU_READ_PHONE_STATE =100;
    private static final String weather_Url="http://wthrcdn.etouch.cn/weather_mini?city=";

    private LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;

    private TextView weather_city_tx,weather_high_tx,weather_low_tx,weather_fun_tx,weather_fun_s_tx,weather_mao_tx,weather_tx;
    private ImageView weather_image;

    private String weather_city_str,weather_high_str,weather_low_str,weather_fun_str,weather_fun_s_str,weather_mao_str,weather_tx_str;

    View view;
    public Fragment_search(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup,
                             Bundle savedInstanceState){

        Toast.makeText(getContext(),"search",Toast.LENGTH_SHORT).show();
        view=inflater.inflate(R.layout.fragment_search,viewGroup,false);

        InitView();

        doLocation();
        mLocationClient.start();

        return view;
    }

    private void InitView(){
        weather_city_tx=view.findViewById(R.id.weather_city_tx);
        weather_high_tx=view.findViewById(R.id.weather_high_tx);
        weather_low_tx=view.findViewById(R.id.weather_low_tx);
        weather_fun_tx=view.findViewById(R.id.weather_fun_tx);
        weather_fun_s_tx=view.findViewById(R.id.weather_fun_s_tx);
        weather_mao_tx=view.findViewById(R.id.weather_mao_tx);
        weather_tx=view.findViewById(R.id.weather_tx);

        weather_image=view.findViewById(R.id.weather_image);

        list=new ArrayList<Search>();

        Init_item();

        SearchAdapter searchAdapter=new SearchAdapter(getContext(),R.layout.search_item,list);
        final ListView listView=view.findViewById(R.id.list_view);
        listView.setAdapter(searchAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("listclick",position+"");

                if(position==0){
                    startActivity(new Intent(getActivity(),ClassFindactivity.class));
                }else{
                    Intent intent=new Intent(getActivity(),NewsShow.class);
                    intent.putExtra("news_title",list.get(position).getInfo());
                    intent.putExtra("news_url",list.get(position).getUrl());
                    startActivity(intent);
                }
            }
        });
    }

    private void Init_item(){
        search=new Search("HNIT","HNIT-期末考试考场查询",R.drawable.findclass);
        list.add(search);
        search=new Search("http://www.weather.com.cn","天气查询",R.drawable.search_weather);
        list.add(search);
        search=new Search("http://ip.tool.chinaz.com/","IP查询",R.drawable.search_ip);
        list.add(search);
        search=new Search("http://www.kuaidi100.com/","快递查询",R.drawable.search_ex);
        list.add(search);
        search=new Search("http://www.ip138.com/sj/","手机归属地查询",R.drawable.search_phone);
        list.add(search);
    }

    public void doLocation() { //声明LocationClient类
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        myListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //初始化定位
        initLocation(); //调用initLocation方法
    }

    private void initLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager
                    .PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission
                    .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) { // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,}, BAIDU_READ_PHONE_STATE);
            }

        } else {
            Log.e("Permissions", "已经有权限了");
        }

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) { //获取定位结果

            weather_city_str = location.getDistrict();
            Log.d("location.getAddress()",location.getAddrStr());
            Log.d("location.getAddress()",location.getCity());
            Log.d("location.getDistrict()",location.getDistrict());

            weather_city_tx.setText(weather_city_str);

            WeatherTask weatherTask=new WeatherTask();
            weatherTask.execute();
        }
    }

    class WeatherTask extends AsyncTask<Void,Integer,String> {

        @Override
        protected String doInBackground(Void... voids) {
            String result=null;

            try{
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url(weather_Url+weather_city_str)
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

        }

        @Override
        protected void onPostExecute(String str){
            weather_tx.setText(weather_tx_str);
            weather_high_tx.setText(weather_high_str);
            weather_low_tx.setText(weather_low_str);
            weather_fun_tx.setText(weather_fun_str);
            weather_fun_s_tx.setText(weather_fun_s_str);
            weather_mao_tx.setText(weather_mao_str);

            if(weather_tx_str.equals("多云")){
                weather_image.setImageResource(R.mipmap.weather_cloudy);
            }else if(weather_tx_str.equals("小雨")){
                weather_image.setImageResource(R.mipmap.weather_rain);
            }else if(weather_tx_str.equals("中雨")){
                weather_image.setImageResource(R.mipmap.weather_rain_s);
            }else if(weather_tx_str.equals("大雨")){
                weather_image.setImageResource(R.mipmap.weather_rain_l);
            }else if(weather_tx_str.equals("晴")){
                weather_image.setImageResource(R.mipmap.weather_sum);
            }else{
                weather_image.setImageResource(R.mipmap.unweather);
            }
        }

        private String getJsonObject(String jsonData){
            String result=null;

            try{
                JSONObject json=new JSONObject(jsonData);
                if(json.getString("desc").equals("OK")){
                    JSONObject jsonObject=new JSONObject(json.getString("data"));

                    JSONArray forecastArray=jsonObject.getJSONArray("forecast");
                    JSONObject forecastObject=forecastArray.getJSONObject(0);

                    weather_tx_str=forecastObject.getString("type");
                    weather_high_str=forecastObject.getString("high");
                    weather_low_str=forecastObject.getString("low");
                    weather_fun_str=forecastObject.getString("fengxiang");
                    weather_fun_s_str=forecastObject.getString("fengli");

                    weather_fun_s_str=reStr(weather_fun_s_str);

                    weather_mao_str=jsonObject.getString("ganmao");
                }else{
                    weather_tx_str=null;
                    weather_high_str=null;
                    weather_low_str=null;
                    weather_fun_str=null;
                    weather_fun_s_str=null;

                    weather_mao_str=null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }
    }
    public String reStr(String str){
        //String fondStr="<![CDATA[]]>"
        String reStr=str.replaceAll("<!\\[CDATA\\[","");
        reStr=reStr.replaceAll("]]>","");
        return reStr;
    }
}
