package com.example.mymall.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymall.R;
import com.example.mymall.activity.AddAddressActivity;
import com.example.mymall.activity.AddressesActivity;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.AddressesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    public static int mode;
    private int preSelectedPosition;
    private List<AddressesModel> addressesModelList;
    private boolean refresh = false;
    private Dialog loadingDialog;


    public AddressesAdapter(List<AddressesModel> addressesModelList, Dialog loadingDialog) {
        this.addressesModelList = addressesModelList;
        this.preSelectedPosition = DbQueries.selectedAddress;
        this.loadingDialog = loadingDialog;
    }

    @NonNull
    @Override
    public AddressesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.ViewHolder holder, int position) {

        String name = addressesModelList.get(position).getName();
        String mobileNo = addressesModelList.get(position).getMobileNo();
        String alternateMobileNo = addressesModelList.get(position).getAlternateMobileNo();
        String flatNo = addressesModelList.get(position).getFlatNo();
        String locality = addressesModelList.get(position).getLocality();
        String landMark = addressesModelList.get(position).getLandMark();
        String city = addressesModelList.get(position).getCity();
        String state = addressesModelList.get(position).getState();
        String pinCode = addressesModelList.get(position).getPinCode();
        boolean selected = addressesModelList.get(position).isSelected();

        holder.setDetails(name, mobileNo, alternateMobileNo, flatNo, locality, landMark, city, state, pinCode, selected, position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        TextView pinCode;
        ImageView writeImageView;
        ImageView icon;
        LinearLayout optionContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.addresses_item_name);
            address = itemView.findViewById(R.id.addresses_item_address);
            pinCode = itemView.findViewById(R.id.addresses_item_pin_code);
            writeImageView = itemView.findViewById(R.id.addresses_write_image_view);
            icon = itemView.findViewById(R.id.addreses_three_dits);
            optionContainer = itemView.findViewById(R.id.option_ontainer);
        }

        private void setDetails(String name, String mobileNo, String alternateMobileNo, String flatNo, String locality, String landMark, String city, String state, String pinCode, boolean selected, final int position) {

            if (DbQueries.addressesModelList.get(DbQueries.selectedAddress).getAlternateMobileNo().equals("")) {
                this.name.setText(name + " - " + mobileNo);
            } else {
                this.name.setText(name + " - " + mobileNo + " or " + alternateMobileNo);
            }

            if (landMark.equals("")) {
                this.address.setText(flatNo + " " + locality + " " + city + " " + state);
            } else {
                this.address.setText(flatNo + " " + locality + " " + landMark + " " + city + " " + state);
            }
            this.pinCode.setText(pinCode);


            if (mode == AddressesModel.SELECT_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                if (selected) {
                    writeImageView.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {
                    writeImageView.setVisibility(View.GONE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preSelectedPosition != position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preSelectedPosition).setSelected(false);
                            AddressesActivity.refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DbQueries.selectedAddress = position;
                        }
                    }
                });

            } else if (mode == AddressesModel.MANAGE_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                optionContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { ///Edit Address
                        Intent addAddressIntent = new Intent(itemView.getContext(), AddAddressActivity.class);
                        addAddressIntent.putExtra("INTENT", "update address");
                        addAddressIntent.putExtra("index", position);
                        itemView.getContext().startActivity(addAddressIntent);
                        refresh = false;
                    }
                });
                optionContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { ///Remove Address

                        loadingDialog.show();

                        Map<String, Object> addresses = new HashMap<>();
                        int x = 0;
                        int selectedPosition = -1;
                        for (int i = 0; i < addressesModelList.size(); i++) {
                            if (i != position) {

                                x++;
                                addresses.put("city " + x, addressesModelList.get(i).getCity());
                                addresses.put("locality " + x, addressesModelList.get(i).getLocality());
                                addresses.put("flatNo " + x, addressesModelList.get(i).getFlatNo());
                                addresses.put("pinCode " + x, addressesModelList.get(i).getPinCode());
                                addresses.put("landMark " + x, addressesModelList.get(i).getLandMark());
                                addresses.put("name " + x, addressesModelList.get(i).getName());
                                addresses.put("mobileNo " + x, addressesModelList.get(i).getMobileNo());
                                addresses.put("alternateMobileNo " + x, addressesModelList.get(i).getAlternateMobileNo());
                                addresses.put("state " + x, addressesModelList.get(i).getState());
                                if (addressesModelList.get(position).isSelected()) {
                                    if (position - 1 >= 0) {
                                        if (x == position) {
                                            addresses.put("selected " + x, true);
                                            selectedPosition = x;
                                        } else {
                                            addresses.put("selected " + x, addressesModelList.get(i).isSelected());
                                        }
                                    } else {
                                        if (x == 1) {
                                            addresses.put("selected " + x, true);
                                            selectedPosition = x;
                                        } else {
                                            addresses.put("selected " + x, addressesModelList.get(i).isSelected());
                                        }
                                    }
                                } else {
                                    addresses.put("selected " + x, addressesModelList.get(i).isSelected());
                                    if (addressesModelList.get(i).isSelected()) {
                                        selectedPosition = x;
                                    }
                                }
                            }
                        }
                        addresses.put("list size", x);

                        final int finalSelectedPosition = selectedPosition;
                        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my addresses")
                                .set(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DbQueries.addressesModelList.remove(position);
                                    if (finalSelectedPosition != -1) {
                                        DbQueries.selectedAddress = finalSelectedPosition - 1;
                                        DbQueries.addressesModelList.get(finalSelectedPosition - 1).setSelected(true);
                                    }else if (DbQueries.addressesModelList.size()==0){
                                        DbQueries.selectedAddress=-1;
                                    }
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(itemView.getContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });

                        refresh = false;
                    }
                });
                icon.setVisibility(View.VISIBLE);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionContainer.setVisibility(View.VISIBLE);
                        if (refresh) {
                            AddressesActivity.refreshItem(preSelectedPosition, -1);
                        } else {
                            refresh = true;
                        }
                        preSelectedPosition = position;
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddressesActivity.refreshItem(preSelectedPosition, -1);
                        preSelectedPosition = -1;
                    }
                });
            } else {
                optionContainer.setVisibility(View.GONE);
            }
        }
    }
}
