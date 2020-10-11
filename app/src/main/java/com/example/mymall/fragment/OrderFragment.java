package com.example.mymall.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mymall.R;
import com.example.mymall.adapter.OrderAdapter;
import com.example.mymall.model.OrderModel;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {

    RecyclerView ordersRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.order_fragment, container, false);
        ordersRecyclerView=view.findViewById(R.id.my_orders_recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ordersRecyclerView.setLayoutManager(linearLayoutManager);

        List<OrderModel>orderModelList=new ArrayList<>();

        orderModelList.add(new OrderModel(R.drawable.mobile_img,3,"Redmi 5A","deleverd on monday 15 JAN 2020"));
        orderModelList.add(new OrderModel(R.drawable.mobile_img,1,"Redmi 5A","deleverd on monday 15 JAN 2020"));
        orderModelList.add(new OrderModel(R.drawable.mobile_img,0,"Redmi 5A","cancelled"));
        orderModelList.add(new OrderModel(R.drawable.mobile_img,4,"Redmi 5A","deleverd on monday 15 JAN 2020"));

        OrderAdapter orderAdapter=new OrderAdapter(orderModelList);
        ordersRecyclerView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();

        return view;
    }
}
