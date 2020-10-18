package com.example.mymall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymall.R;
import com.example.mymall.activity.AddressesActivity;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.AddressesModel;

import java.util.List;

import static android.view.Gravity.RIGHT;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    public static int mode;
    int oldItemSelectedPosition;

    List<AddressesModel> addressesModelList;

    public AddressesAdapter(List<AddressesModel> addressesModelList) {
        this.addressesModelList = addressesModelList;
        this.oldItemSelectedPosition= DbQueries.selectedAddress;
    }

    @NonNull
    @Override
    public AddressesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.ViewHolder holder, int position) {
        holder.setDetails(addressesModelList.get(position).getName(), addressesModelList.get(position).getAddress(), addressesModelList.get(position).getPinCode(), addressesModelList.get(position).isSelected(),addressesModelList.get(position).getMobileNo(), position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView address;
        TextView pinCode;
        ImageView writeImageView;
        ImageView threeDots;
        LinearLayout addOrRemoveLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.addresses_item_name);
            address = itemView.findViewById(R.id.addresses_item_address);
            pinCode = itemView.findViewById(R.id.addresses_item_pin_code);
            writeImageView = itemView.findViewById(R.id.addresses_write_image_view);
            threeDots=itemView.findViewById(R.id.addreses_three_dits);
            addOrRemoveLayout = itemView.findViewById(R.id.add_or_remove_layout);
        }

        private void setDetails(String Name, String address, String pinCode, boolean selected,String mobileNo, final int position) {
            this.Name.setText(Name+" - "+mobileNo);
            this.address.setText(address);
            this.pinCode.setText(pinCode);

            if (mode==AddressesModel.DELIVERY_ACTIVITY) {
                addOrRemoveLayout.setVisibility(View.GONE);
                if (selected) {
                    writeImageView.setVisibility(View.VISIBLE);
                    oldItemSelectedPosition = position;
                } else {
                    writeImageView.setVisibility(View.GONE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (oldItemSelectedPosition != position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(oldItemSelectedPosition).setSelected(false);
                            AddressesActivity.refreshItem(oldItemSelectedPosition, position);
                            oldItemSelectedPosition = position;
                            DbQueries.selectedAddress=position;
                        }
                    }
                });

            } else if (mode==AddressesModel.ACCOUNT_FRAGMENT) {
                addOrRemoveLayout.setVisibility(View.GONE);
                threeDots.setVisibility(View.VISIBLE);

                threeDots.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addOrRemoveLayout.setVisibility(View.VISIBLE);
                        AddressesActivity.refreshItem(oldItemSelectedPosition,-1);
                        oldItemSelectedPosition=position;
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddressesActivity.refreshItem(oldItemSelectedPosition,-1);
                        oldItemSelectedPosition=-1;
                    }
                });
            } else {
                addOrRemoveLayout.setVisibility(View.GONE);
            }
        }
    }
}
