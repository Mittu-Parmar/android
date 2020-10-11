package com.example.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.mymall.R;
import com.example.mymall.adapter.GridProductLayoutAdapter;
import com.example.mymall.adapter.WishlistAdapter;
import com.example.mymall.model.ProductItemModel;
import com.example.mymall.model.WishListModel;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    RecyclerView viewAllItemRecyclerView;
    GridView viewAllGridView;
    public static List<ProductItemModel> productItemModelList;
    public static List<WishListModel> wishListModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getIntExtra("Layout_code", -1) == 0) {

            viewAllItemRecyclerView = findViewById(R.id.view_all_recycler_view);
            viewAllItemRecyclerView.setVisibility(View.VISIBLE);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAllActivity.this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            viewAllItemRecyclerView.setLayoutManager(linearLayoutManager);

            WishlistAdapter wishlistAdapter = new WishlistAdapter(wishListModelList, true);
            viewAllItemRecyclerView.setAdapter(wishlistAdapter);
            wishlistAdapter.notifyDataSetChanged();
        } else if (getIntent().getIntExtra("Layout_code", -1) == 1) {

            viewAllGridView = findViewById(R.id.view_all_btn_grid_viw);
            viewAllGridView.setVisibility(View.VISIBLE);

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(productItemModelList);
            viewAllGridView.setAdapter(gridProductLayoutAdapter);
            gridProductLayoutAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
