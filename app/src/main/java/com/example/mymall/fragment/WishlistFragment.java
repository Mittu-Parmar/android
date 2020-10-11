package com.example.mymall.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.adapter.WishlistAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.WishListModel;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

    RecyclerView wishlistItemRecyclerView;
    private Dialog loadingDialog;
    public static WishlistAdapter wishlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        wishlistItemRecyclerView = view.findViewById( R.id.wishlist_item_recycler_view);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        wishlistItemRecyclerView.setLayoutManager(linearLayoutManager);

        if (DbQueries.wishListModelList.size()==0){
            DbQueries.wishList.clear();
            DbQueries.loadWishList(getContext(),loadingDialog,true);
        }else {
            loadingDialog.dismiss();
        }

        wishlistAdapter=new WishlistAdapter(DbQueries.wishListModelList,false);
        wishlistItemRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();

        return view;
    }
}
