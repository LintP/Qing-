package com.example.lint.main.search.findclasstime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lint.main.R;
import com.example.lint.main.search.Search;

import java.util.List;

public class ClassecndAdapter extends ArrayAdapter {
    private final int resourceId;

    public ClassecndAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int index, View view, ViewGroup group) {
        classTime classtime = (classTime) getItem(index);
        View view1 = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView textView1 = view1.findViewById(R.id.classendtime_uid_tx);
        TextView textView2 = view1.findViewById(R.id.classendtime_uname_tx);
        TextView textView3 = view1.findViewById(R.id.classendtime_name_tx);
        TextView textView4 = view1.findViewById(R.id.classendtime_class_tx);
        TextView textView5 = view1.findViewById(R.id.classendtime_count_tx);
        TextView textView6 = view1.findViewById(R.id.classendtime_data_tx);
        TextView textView7 = view1.findViewById(R.id.classendtime_time_tx);
        TextView textView8 = view1.findViewById(R.id.classendtime_addr_tx);

        textView1.setText(classtime.getUid());
        textView2.setText(classtime.getInstitute());
        textView3.setText(classtime.getName());
        textView4.setText(classtime.getClassname());
        textView5.setText(classtime.getCount());
        textView6.setText(classtime.getDate());
        textView7.setText(classtime.getTime());
        textView8.setText(classtime.getAddr());

        return view1;
    }
}
