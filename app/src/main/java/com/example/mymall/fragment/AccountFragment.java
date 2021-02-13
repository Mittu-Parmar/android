package com.example.mymall.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.AddressesActivity;
import com.example.mymall.activity.RegisterActivity;
import com.example.mymall.adapter.AddressesAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.AddressesModel;
import com.example.mymall.model.OrderItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import static com.example.mymall.db_handler.DbQueries.clearData;

public class AccountFragment extends Fragment {

    private Button viewAllButton;
    private CircularImageView profileView, currantOrderImage;
    private TextView name, email, currantOrderStatusTextView, yourRecentOrderTitle;
    private LinearLayout linearContainer, recentOrdersContainer;
    private Dialog loadingDialog;

    private ImageView orderedIndicator, packedIndicator, shippedIndicator, deliveredIndicator;
    private ProgressBar O_P_Progress, P_S_Progress, S_D_Progress;
    private TextView currentAddressFullName, currentAddressAddress, currentAddressPinCode;
    private Button signOutButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        profileView = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);

        linearContainer = view.findViewById(R.id.layout_container);

        currantOrderImage = view.findViewById(R.id.currunt_order_image);
        currantOrderStatusTextView = view.findViewById(R.id.currant_order_status);

        orderedIndicator = view.findViewById(R.id.account_order_indecator);
        packedIndicator = view.findViewById(R.id.account_packed_indecator);
        shippedIndicator = view.findViewById(R.id.account_shipped_indecator);
        deliveredIndicator = view.findViewById(R.id.account_delivered_indecator);
        O_P_Progress = view.findViewById(R.id.account_order_packed_progress);
        P_S_Progress = view.findViewById(R.id.account_packed_shipped_progress);
        S_D_Progress = view.findViewById(R.id.account_shipped_delivered_progress);

        recentOrdersContainer = view.findViewById(R.id.recent_order_container);
        yourRecentOrderTitle = view.findViewById(R.id.your_recent_orders_title);

        currentAddressFullName = view.findViewById(R.id.order_full_name);
        currentAddressAddress = view.findViewById(R.id.order_address);
        currentAddressPinCode = view.findViewById(R.id.order_pin_code);

        signOutButton = view.findViewById(R.id.sign_out_button);

        name.setText(DbQueries.fullName);
        email.setText(DbQueries.email);
        if (!DbQueries.profile.equals("")) {
            Glide.with(getContext()).load(DbQueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile_place_holder)).into(profileView);
        }


        linearContainer.getChildAt(1).setVisibility(View.GONE);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                for (OrderItemModel orderItemModel : DbQueries.orderItemModelList) {
                    if (!orderItemModel.isCancellationRequested()) {
                        if (!orderItemModel.getOrderStatus().equals("delivered") && !orderItemModel.getOrderStatus().equals("cancelled")) {

                            linearContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(orderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into(currantOrderImage);
                            currantOrderStatusTextView.setText(orderItemModel.getOrderStatus());

                            switch (orderItemModel.getOrderStatus()) {

                                case "ordered":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    break;
                                case "packed":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_Progress.setProgress(100);
                                    break;
                                case "shipped":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_Progress.setProgress(100);
                                    P_S_Progress.setProgress(100);
                                    break;
                                case "out for delivery":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_Progress.setProgress(100);
                                    P_S_Progress.setProgress(100);
                                    S_D_Progress.setProgress(100);
                                    break;
                            }

                        }
                    }
                }
                int i = 0;
                for (OrderItemModel myOrderItemModel : DbQueries.orderItemModelList) {
                    if (i < 4) {
                        if (myOrderItemModel.getOrderStatus().equals("delivered")) {
                            Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into((ImageView) recentOrdersContainer.getChildAt(i));
                            i++;
                        }
                    } else {
                        break;
                    }
                }
                if (i == 0) {
                    yourRecentOrderTitle.setText("No recent Orders.");
                }
                if (i < 3) {
                    for (int x = i; x < 4; x++) {
                        recentOrdersContainer.getChildAt(x).setVisibility(View.GONE);
                    }
                }

                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadingDialog.setOnDismissListener(null);
                        if (DbQueries.addressModelList.size() == 0) {
                            currentAddressFullName.setText("No Address");
                            currentAddressAddress.setText("");
                            currentAddressPinCode.setText("");
                        } else {
                            String nameText, mobileNo;
                            nameText = DbQueries.addressModelList.get(DbQueries.selectedAddress).getName();
                            mobileNo = DbQueries.addressModelList.get(DbQueries.selectedAddress).getMobileNo();
                            if (DbQueries.addressModelList.get(DbQueries.selectedAddress).getAlternateMobileNo().equals("")) {
                                currentAddressFullName.setText(nameText + " - " + mobileNo);
                            } else {
                                currentAddressFullName.setText(nameText + " - " + mobileNo + " or " + DbQueries.addressModelList.get(DbQueries.selectedAddress).getAlternateMobileNo());
                            }

                            String flatNo = DbQueries.addressModelList.get(DbQueries.selectedAddress).getFlatNo();
                            String locality = DbQueries.addressModelList.get(DbQueries.selectedAddress).getLocality();
                            String landMark = DbQueries.addressModelList.get(DbQueries.selectedAddress).getLandMark();
                            String city = DbQueries.addressModelList.get(DbQueries.selectedAddress).getCity();
                            String state = DbQueries.addressModelList.get(DbQueries.selectedAddress).getState();

                            if (landMark.equals("")) {
                                currentAddressAddress.setText(flatNo + " " + locality + " " + city + " " + state);
                            } else {
                                currentAddressAddress.setText(flatNo + " " + locality + " " + landMark + " " + city + " " + state);
                            }
                            currentAddressPinCode.setText(DbQueries.addressModelList.get(DbQueries.selectedAddress).getPinCode());


                        }
                    }
                });
                DbQueries.loadAddresses(loadingDialog, getContext(), false);
            }
        });

        DbQueries.loadOrders(getContext(), null, loadingDialog);

        viewAllButton = view.findViewById(R.id.account_view_all_address_btn);
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddressesIntent = new Intent(getContext(), AddressesActivity.class);
                AddressesAdapter.mode = AddressesModel.MANAGE_ADDRESS;
                startActivity(AddressesIntent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                clearData();
                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });

        return view;
    }
}
