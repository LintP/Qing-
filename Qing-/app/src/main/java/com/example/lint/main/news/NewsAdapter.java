package com.example.lint.main.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lint.main.R;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    List<NewsData> list;//存放数据
    Context context;

    public NewsAdapter(List<NewsData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout
                .news_item, parent, false));
        return holder;
    }

    //在这里可以获得每个子项里面的控件的实例，比如这里的TextView,子项本身的实例是itemView，
    //在这里对获取对象进行操作holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
    //position是点击位置
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //设置textView显示内容为list里的对应项
        //holder.textView.setText(list.get(position));
        holder.id.setText(list.get(position).id);
        holder.date.setText(list.get(position).date);
        holder.author.setText(list.get(position).author_name);
        holder.title.setText(list.get(position).title);

        ImageTask imageTask=new ImageTask(list.get(position).thumbnail_pic_s,holder);
        imageTask.execute();

        //子项的点击事件监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "点击子项" + position+holder.author.getText().toString(), Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),Register.class));
                Intent intent=new Intent(context,NewsShow.class);
                intent.putExtra("news_title",list.get(position).title.toString());
                intent.putExtra("news_url",list.get(position).url.toString());
                context.startActivity(intent);
            }
        });
    }


    //要显示的子项数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id,date,author,title;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            //textView = itemView.findViewById(R.id.id_num);
            id=itemView.findViewById(R.id.id_num);
            date=itemView.findViewById(R.id.news_date);
            title=itemView.findViewById(R.id.news_title);
            author=itemView.findViewById(R.id.news_author);

            imageView=itemView.findViewById(R.id.news_image);
        }
    }

    /*之下的方法都是为了方便操作，并不是必须的*/
    //在指定位置插入，原位置的向后移动一格
    public boolean addItem(int position, NewsData msg) {
        if (position < list.size() && position >= 0) {
            list.add(position, msg);
            notifyItemInserted(position);
            return true;
        } return false;
    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < list.size() && position >= 0) {
            list.remove(position);
            notifyItemRemoved(position);
            return true;
        } return false;
    }

    //清空显示数据
    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    //加载图片
    class ImageTask extends AsyncTask<Void,Integer,Bitmap>{

        String url;
        MyViewHolder holder;

        public ImageTask(String url,MyViewHolder holder){
            this.url=url;
            this.holder=holder;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            Bitmap bitmap=null;
            try{
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url(url)
                        .build();
                Response response=client.newCall(request).execute();
                byte[] in=response.body().bytes();
                bitmap= BitmapFactory.decodeByteArray(in,0,in.length);
            }catch (IOException e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap in){
            super.onPostExecute(in);

            holder.imageView.setImageBitmap(in);
        }
    }
}
