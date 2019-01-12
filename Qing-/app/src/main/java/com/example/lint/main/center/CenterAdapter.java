package com.example.lint.main.center;

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
import com.example.lint.main.news.NewsAdapter;
import com.example.lint.main.news.NewsData;
import com.example.lint.main.news.NewsShow;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.MyViewHolder> {
    List<CenterBomb> list;//存放数据
    Context context;

    public CenterAdapter(List<CenterBomb> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public CenterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CenterAdapter.MyViewHolder holder = new CenterAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout
                .center_item, parent, false));
        return holder;
    }

    //在这里可以获得每个子项里面的控件的实例，比如这里的TextView,子项本身的实例是itemView，
    //在这里对获取对象进行操作holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
    //position是点击位置
    @Override
    public void onBindViewHolder(final CenterAdapter.MyViewHolder holder, final int position) {
        //设置textView显示内容为list里的对应项
        //holder.textView.setText(list.get(position));
        if(list.get(position).getState()){
            holder.state.setText("已认领");
        }else {
            holder.state.setText("待认领");
        }

        holder.createdat.setText(list.get(position).getCreatedAt());
        holder.title.setText(list.get(position).getTitle());
        holder.tel.setText(list.get(position).getTel());
        holder.body.setText(list.get(position).getBody());
        holder.user.setText(list.get(position).getUser());
        holder.addr.setText(list.get(position).getAddr());

        //子项的点击事件监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "点击子项" + position+holder.title.getText().toString(), Toast.LENGTH_SHORT).show();
                if(list.get(position).getUser().equals("Lint")){
                    Toast.makeText(context,"测试项目",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(context,CenterEdit.class);
                    intent.putExtra("uid",list.get(position).getObjectId());
                    context.startActivity(intent);
                }
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
        TextView state,title,tel,body,user,createdat,addr;

        public MyViewHolder(View itemView) {
            super(itemView);

            state=itemView.findViewById(R.id.center_state);
            title=itemView.findViewById(R.id.center_title_tx);
            tel=itemView.findViewById(R.id.center_tel_tx);
            body=itemView.findViewById(R.id.center_body_tx);
            user=itemView.findViewById(R.id.center_user_tx);
            createdat=itemView.findViewById(R.id.center_time_tx);
            addr=itemView.findViewById(R.id.center_addr_tx);
        }
    }

    /*之下的方法都是为了方便操作，并不是必须的*/
    //在指定位置插入，原位置的向后移动一格
    public boolean addItem(int position, CenterBomb msg) {
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

}
