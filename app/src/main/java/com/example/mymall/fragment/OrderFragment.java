package com.example.mymall.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.adapter.OrderAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.OrderItemModel;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {

    RecyclerView ordersRecyclerView;
    public static OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        ordersRecyclerView = view.findViewById(R.id.my_orders_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ordersRecyclerView.setLayoutManager(linearLayoutManager);


        orderAdapter = new OrderAdapter(DbQueries.orderItemModelList);
        ordersRecyclerView.setAdapter(orderAdapter);

        DbQueries.loadOrders(getContext(), orderAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        orderAdapter.notifyDataSetChanged();
    }
}
