package com.example.mymall.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.activity.DeliveryActivity;
import com.example.mymall.adapter.OrderAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.OrderItemModel;

import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    public static OrderAdapter orderAdapter;
    private Dialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


        ordersRecyclerView = view.findViewById(R.id.my_orders_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ordersRecyclerView.setLayoutManager(linearLayoutManager);


        orderAdapter = new OrderAdapter(DbQueries.orderItemModelList,loadingDialog);
        ordersRecyclerView.setAdapter(orderAdapter);

        DbQueries.loadOrders(getContext(), orderAdapter,loadingDialog);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        orderAdapter.notifyDataSetChanged();
    }
}
