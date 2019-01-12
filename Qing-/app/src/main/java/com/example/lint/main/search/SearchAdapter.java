package com.example.lint.main.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lint.main.R;

import java.util.List;

public class SearchAdapter extends ArrayAdapter {
    private final int resourceId;

    public SearchAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int index, View view, ViewGroup group) {
        Search search = (Search) getItem(index);
        View view1 = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView imageView = view1.findViewById(R.id.search_item_img);
        TextView textView1 = view1.findViewById(R.id.search_item_tx);
        TextView textView2 = view1.findViewById(R.id.search_item_url_tx);

        imageView.setImageResource(search.getImageID());
        textView1.setText(search.getInfo());
        textView2.setText(search.getUrl());

        return view1;
    }
}


