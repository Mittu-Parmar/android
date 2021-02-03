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
import com.example.mymall.adapter.RewordsAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.RewardsModel;

import java.util.ArrayList;
import java.util.List;

public class RewardsFragment extends Fragment {

    RecyclerView rewordsRecyclerView;
    Dialog loadingDialog;
    public static RewordsAdapter rewordsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_rewards, container, false);
        rewordsRecyclerView=view.findViewById(R.id.rewords_recycler_view);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rewordsRecyclerView.setLayoutManager(linearLayoutManager);
        if (DbQueries.rewardsModelList.size()==0){
            DbQueries.loadRewards(getContext(),loadingDialog,true);
        }else {
            loadingDialog.dismiss();
        }
        rewordsAdapter=new RewordsAdapter(DbQueries.rewardsModelList,false);
        rewordsRecyclerView.setAdapter(rewordsAdapter);
        rewordsAdapter.notifyDataSetChanged();

        return view;
    }
}
