package com.example.lint.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lint.main.center.CenterAdapter;
import com.example.lint.main.center.CenterAdd;
import com.example.lint.main.center.CenterBomb;
import com.example.lint.main.news.NewsData;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Fragment_center extends Fragment {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CenterAdapter centerAdapter;
    List<CenterBomb> listC;
    CenterBomb centerBomb;
    FrameLayout frameLayout;

    public Fragment_center() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        Toast.makeText(getContext(), "center", Toast.LENGTH_SHORT).show();
        Bmob.initialize(getContext(), "bb0112c3f4a5f1534d14ea5f24149f43");

        final View view = inflater.inflate(R.layout.fragment_center, viewGroup, false);

        listC = new ArrayList<>();
        loadData();

        centerAdapter = new CenterAdapter(listC, getActivity());
        recyclerView = view.findViewById(R.id.recylerView_center);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_center);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(centerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLACK, Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (listC.get(0).getUser().equals("Lint")) {
                    Toast.makeText(getActivity(), "暂无更新", Toast.LENGTH_SHORT).show();
                } else {
                    centerBomb = new CenterBomb();

                    centerBomb.setUser("Lint");
                    centerBomb.setAddr("RUN");
                    centerBomb.setBody("Error Bmob! \n\rExit!!!!\n\rExit!!!!");
                    centerBomb.setTel("119");
                    centerBomb.setTitle("Bmob");

                    listC.add(0,centerBomb);
                    centerAdapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        view.findViewById(R.id.center_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("dddddd",getContext().toString());

                Toast.makeText(getContext(),"FAB",Toast.LENGTH_SHORT).show();
                getContext().startActivity(new Intent(getContext(), CenterAdd.class));
            }
        });

        return view;
    }

    private void loadData() {
        BmobQuery<CenterBomb> bmobQuery = new BmobQuery<CenterBomb>();
        bmobQuery.findObjects(new FindListener<CenterBomb>() {
            @Override
            public void done(List<CenterBomb> list, BmobException e) {
                try{
                    if (e == null) {
                        int size = list.size();
                        for (int i = 0; i < size; i++) {
                            listC.add(list.get(i));
                        }
                    }
                } catch (Exception ex){
                    ex.printStackTrace();

                    centerBomb = new CenterBomb();
                    centerBomb.setUser("Lint");
                    centerBomb.setAddr("RUN");
                    centerBomb.setBody("Error Bmob! \n\rExit!!!!\n\rExit!!!!");
                    centerBomb.setTel("119");
                    centerBomb.setTitle("Bmob");

                    listC.add(centerBomb);
                }
            }
        });
    }
}
