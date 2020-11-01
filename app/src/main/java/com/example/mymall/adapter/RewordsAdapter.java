package com.example.mymall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymall.R;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.model.RewardsModel;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RewordsAdapter extends RecyclerView.Adapter<RewordsAdapter.ViewHolder> {

    List<RewardsModel> rewardsModelList;
    boolean useMiniLayout = false;

    public RewordsAdapter(List<RewardsModel> rewardsModelList, boolean useMiniLayout) {
        this.rewardsModelList = rewardsModelList;
        this.useMiniLayout = useMiniLayout;
    }

    @NonNull
    @Override
    public RewordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (useMiniLayout) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewords_item_layout, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rewords_item_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RewordsAdapter.ViewHolder holder, int position) {

        String type = rewardsModelList.get(position).getType();
        Date date = rewardsModelList.get(position).getDate();
        String body = rewardsModelList.get(position).getBody();
        String lowerLimit = rewardsModelList.get(position).getLowerLimit();
        String upperLimit = rewardsModelList.get(position).getUpperLimit();
        String disORamt = rewardsModelList.get(position).getDisORamt();

        holder.setDetails(type, date, body, lowerLimit, upperLimit, disORamt);

    }

    @Override
    public int getItemCount() {
        return rewardsModelList.size();
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

        private void setDetails(final String type, final Date validity, final String body, String lowerLimit, String upperLimit, String disORamt) {

            if (type.equals("discount")) {
                this.rewordsTitle.setText(type);
            } else {
                this.rewordsTitle.setText("Flat RS." + disORamt + " OFF");
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
            this.rewordsValidity.setText(" Till "+simpleDateFormat.format(validity));
            this.rewordsBody.setText(body);

            if (useMiniLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetailsActivity.coupenTitle.setText(type);
                        ProductDetailsActivity.coupenExpiryDate.setText(simpleDateFormat.format(validity));
                        ProductDetailsActivity.coupenBody.setText(body);
                        ProductDetailsActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
