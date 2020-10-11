package com.example.mymall.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.activity.AddAddressActivity;
import com.example.mymall.activity.DeliveryActivity;
import com.example.mymall.activity.MainActivity;
import com.example.mymall.adapter.CartAdapter;
import com.example.mymall.adapter.WishlistAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.CartItemModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.mymall.activity.MainActivity.bedgeCount;

public class CartFragment extends Fragment {

    RecyclerView cartItemsRecyclerView;
    Button continueButton;

    private Dialog loadingDialog;
    public static CartAdapter cartAdapter;

    private TextView totalAmount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recycler_view);
        continueButton = view.findViewById(R.id.cart_continue_button);
        totalAmount = view.findViewById(R.id.total_cart_amount);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);

        if (DbQueries.cartItemModelList.size() == 0) {
            DbQueries.cartList.clear();
            DbQueries.loadCartList(getContext(), loadingDialog, true, bedgeCount);
        } else {
            if (DbQueries.cartItemModelList.get(DbQueries.cartItemModelList.size()-1).getType()== CartItemModel.TOTAL_AMOUNT){
                LinearLayout parent= (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }

        cartAdapter = new CartAdapter(DbQueries.cartItemModelList, totalAmount, getActivity(), true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeliveryActivity.cartItemModelList=new ArrayList<>();
                for (int x = 0; x < DeliveryActivity.cartItemModelList.size() ; x++) {
                    CartItemModel cartItemModel =DbQueries.cartItemModelList.get(x);
                    if (cartItemModel.isInStock()){
                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                loadingDialog.show();
                DbQueries.loadAddresses(loadingDialog, getContext());
            }
        });
        return view;
    }

}
