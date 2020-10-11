package com.example.mymall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.model.RewordsModel;

import java.util.List;

public class RewordsAdapter extends RecyclerView.Adapter<RewordsAdapter.ViewHolder> {

    List<RewordsModel>rewordsModelList;
    boolean useMiniLayout =false;

    public RewordsAdapter(List<RewordsModel> rewordsModelList,boolean useMiniLayout) {
        this.rewordsModelList = rewordsModelList;
        this.useMiniLayout=useMiniLayout;
    }

    @NonNull
    @Override
    public RewordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (useMiniLayout){
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewords_item_layout,parent,false));
        }else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rewords_item_layout,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RewordsAdapter.ViewHolder holder, int position) {

        holder.setDetails(rewordsModelList.get(position).getRewordsTitle(),rewordsModelList.get(position).getRewordsValidity(),rewordsModelList.get(position).getRewordsBody());

    }

    @Override
    public int getItemCount() {
        return rewordsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView rewordsTitle;
        TextView rewordsValidity;
        TextView rewordsBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.rewordsTitle = itemView.findViewById(R.id.rewords_item_coupen_title);
            this.rewordsValidity = itemView.findViewById(R.id.rewords_item_coupen_validity);
            this.rewordsBody = itemView.findViewById(R.id.rewords_item_coupen_body);
        }
        private void setDetails(final String rewordsTitle, final String rewordsValidity, final String rewordsBody){
            this.rewordsTitle.setText(rewordsTitle);
            this.rewordsValidity.setText(rewordsValidity);
            this.rewordsBody.setText(rewordsBody);

            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetailsActivity.coupenTitle.setText(rewordsTitle);
                        ProductDetailsActivity.coupenExpiryDate.setText(rewordsValidity);
                        ProductDetailsActivity.coupenBody.setText(rewordsBody);
                        ProductDetailsActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
