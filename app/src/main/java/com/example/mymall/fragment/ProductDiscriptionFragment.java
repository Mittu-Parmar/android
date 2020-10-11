package com.example.mymall.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDiscriptionFragment extends Fragment {

    TextView descriptionBody;
    public String body;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product_discription, container, false);
        descriptionBody=view.findViewById(R.id.product_discription_text_view);
        descriptionBody.setText(body);

        return view;
    }
}
