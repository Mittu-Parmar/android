package com.example.mymall.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mymall.R;
import com.example.mymall.activity.AddressesActivity;
import com.example.mymall.activity.DeliveryActivity;
import com.example.mymall.adapter.AddressesAdapter;
import com.example.mymall.model.AddressesModel;

public class AccountFragment extends Fragment {

    Button viewAllButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_account, container, false);

        viewAllButton=view.findViewById(R.id.account_view_all_address_btn);
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddressesIntent=new Intent(getContext(), AddressesActivity.class);
                AddressesAdapter.mode= AddressesModel.ACCOUNT_FRAGMENT;
                startActivity(AddressesIntent);
            }
        });

        return view;
    }
}
