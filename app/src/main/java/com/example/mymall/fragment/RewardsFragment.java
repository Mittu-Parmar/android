package com.example.mymall.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.adapter.RewordsAdapter;
import com.example.mymall.model.RewordsModel;

import java.util.ArrayList;
import java.util.List;

public class RewardsFragment extends Fragment {

    RecyclerView rewordsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_rewards, container, false);
        rewordsRecyclerView=view.findViewById(R.id.rewords_recycler_view);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rewordsRecyclerView.setLayoutManager(linearLayoutManager);

        List<RewordsModel>rewordsModelList=new ArrayList<>();
        rewordsModelList.add(new RewordsModel("SELL","Till 22 Aug 2020","Buy above Rs.500 and get Rs.100 flat discount"));
        rewordsModelList.add(new RewordsModel("SELL","Till 22 Aug 2020","Buy above Rs.500 and get Rs.100 flat discount"));
        rewordsModelList.add(new RewordsModel("SELL","Till 22 Aug 2020","Buy above Rs.500 and get Rs.100 flat discount"));
        rewordsModelList.add(new RewordsModel("SELL","Till 22 Aug 2020","Buy above Rs.500 and get Rs.100 flat discount"));

        RewordsAdapter rewordsAdapter=new RewordsAdapter(rewordsModelList,false);
        rewordsRecyclerView.setAdapter(rewordsAdapter);
        rewordsAdapter.notifyDataSetChanged();

        return view;
    }
}
