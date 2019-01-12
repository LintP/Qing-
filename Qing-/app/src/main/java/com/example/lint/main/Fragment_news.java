package com.example.lint.main;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lint.main.news.NewsAdapter;
import com.example.lint.main.news.NewsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Fragment_news extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    NewsAdapter newsAdapter;
    List<NewsData> list, gson_list;
    NewsData newsData, upnewsData;

    public Fragment_news() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        list = new ArrayList<>();

        NewsGsonTask newsGsonTask = new NewsGsonTask();
        newsGsonTask.execute();

        View view = inflater.inflate(R.layout.fragment_news, viewGroup, false);

        newsAdapter = new NewsAdapter(list, getActivity());
        recyclerView = view.findViewById(R.id.recylerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(newsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLACK, Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(list.get(0).url.equals("http://www.plint.top")){
                    Toast.makeText(getActivity(),"暂无更新",Toast.LENGTH_SHORT).show();
                }else{
                    upnewsData=new NewsData();

                    upnewsData.id="青鸽";
                    upnewsData.thumbnail_pic_s="http://www.plint.top/res/back.png";
                    upnewsData.author_name="Lint";
                    upnewsData.title="Qing鸽";
                    upnewsData.url="http://www.plint.top";

                    list.add(0, upnewsData);
                    newsAdapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    class NewsGsonTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            //String url = "http://v.juhe.cn/toutiao/index?type=guonei&key=4333f5e1f5eea138e529302548c03c05";
            String url="http://www.plint.top/res/news.json";

            String result = null;
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                parseJsonWithGson(responseData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String in) {
            super.onPostExecute(in);
            Log.d("新闻获取", in);
        }

        private void parseJsonWithGson(String json) {
            try {
                JSONObject jsonObject_a = new JSONObject(json);
                String reason = jsonObject_a.getString("reason");
                if (reason.equals("成功的返回")) {
                    String result = jsonObject_a.getString("result");
                    JSONObject jsonObject_b = new JSONObject(result);
                    String stat = jsonObject_b.getString("stat");
                    if (stat.equals("1")) {
                        String data = jsonObject_b.getString("data");

                        Gson gson = new Gson();
                        gson_list = gson.fromJson(data, new TypeToken<List<NewsData>>() {
                        }.getType());
                        for (int i = 0; i < gson_list.size(); i++) {
                            newsData = new NewsData();

                            newsData.id = "" + i;
                            newsData.author_name = gson_list.get(i).author_name;
                            newsData.title = gson_list.get(i).title;
                            newsData.url = gson_list.get(i).url;
                            newsData.thumbnail_pic_s = gson_list.get(i).thumbnail_pic_s;
                            newsData.date = gson_list.get(i).date;
                            newsData.uniquekey = gson_list.get(i).uniquekey;
                            newsData.category = gson_list.get(i).category;

                            list.add(newsData);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                for (int i = 0; i < 5; i++) {
                    newsData = new NewsData();

                    newsData.id = "" + i;
                    newsData.author_name = "青鸽";
                    newsData.title = "青鸽";
                    newsData.url = null;
                    newsData.thumbnail_pic_s = null;
                    newsData.date = "2019-01-05 20:00";
                    newsData.uniquekey = null;
                    newsData.category = null;
                }
            }
        }
    }
}
