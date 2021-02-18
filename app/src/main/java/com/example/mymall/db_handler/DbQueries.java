package com.example.mymall.db_handler;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymall.R;
import com.example.mymall.activity.AddAddressActivity;
import com.example.mymall.activity.DeliveryActivity;
import com.example.mymall.activity.NotificationActivity;
import com.example.mymall.activity.ProductDetailsActivity;
import com.example.mymall.adapter.CategoryAdapter;
import com.example.mymall.adapter.HomeAdapter;
import com.example.mymall.adapter.OrderAdapter;
import com.example.mymall.fragment.CartFragment;
import com.example.mymall.fragment.HomeFragment;
import com.example.mymall.fragment.OrderFragment;
import com.example.mymall.fragment.RewardsFragment;
import com.example.mymall.fragment.WishlistFragment;
import com.example.mymall.model.AddressesModel;
import com.example.mymall.model.CartItemModel;
import com.example.mymall.model.CategoryModel;
import com.example.mymall.model.HomeModel;
import com.example.mymall.model.NotificationModel;
import com.example.mymall.model.OrderItemModel;
import com.example.mymall.model.ProductItemModel;
import com.example.mymall.model.RewardsModel;
import com.example.mymall.model.SliderModel;
import com.example.mymall.model.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mymall.activity.ProductDetailsActivity.addToWishlistButton;
import static com.example.mymall.activity.ProductDetailsActivity.initialRating;
import static com.example.mymall.activity.ProductDetailsActivity.productId;

public class DbQueries {

    public static String fullName, email, profile;

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList();

    public static List<List<HomeModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoryNames = new ArrayList<>();

    public static List<String> wishList = new ArrayList<>();
    public static List<WishListModel> wishListModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static List<AddressesModel> addressesModelList = new ArrayList<>();
    public static int selectedAddress = -1;

    public static List<RewardsModel> rewardsModelList = new ArrayList<>();

    public static List<OrderItemModel> orderItemModelList = new ArrayList<>();

    public static List<NotificationModel> notificationModelList = new ArrayList<>();

    private static ListenerRegistration registration;

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {

        categoryModelList.clear();
        firebaseFirestore.collection("categories").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                    }

                    CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadFragmentsData(final RecyclerView homeRecyclerView, final Context context, final int index, String categoryName) {
        firebaseFirestore.collection("categories").document(categoryName)
                .collection("top deals").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if ((long) documentSnapshot.get("view type") == HomeModel.BANNER_SLIDER) {

                                    List<SliderModel> sliderModelList = new ArrayList<SliderModel>();
                                    for (long i = 1; i <= (long) documentSnapshot.get("number of banners"); i++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner" + i).toString()));
                                    }
                                    lists.get(index).add(new HomeModel(HomeModel.BANNER_SLIDER, sliderModelList));

                                } else if ((long) documentSnapshot.get("view type") == HomeModel.STRIP_AD) {

                                    lists.get(index).add(new HomeModel(HomeModel.STRIP_AD, documentSnapshot.get("strip add banner").toString()));

                                } else if ((long) documentSnapshot.get("view type") == HomeModel.HORIZONTAL_PRODUCT_VIEW) {

                                    List<WishListModel> viewAllprodectModelList = new ArrayList<>();
                                    List<ProductItemModel> productItemModelList = new ArrayList<>();
                                    for (long i = 1; i <= (long) documentSnapshot.get("number of products"); i++) {
                                        productItemModelList.add(new ProductItemModel(
                                                documentSnapshot.get("product id " + i).toString(),
                                                documentSnapshot.get("product image " + i).toString(),
                                                documentSnapshot.get("product title " + i).toString(),
                                                documentSnapshot.get("product sub title " + i).toString(),
                                                documentSnapshot.get("product price " + i).toString()));

                                        viewAllprodectModelList.add(new WishListModel(
                                                documentSnapshot.get("product id " + i).toString(),
                                                documentSnapshot.get("product image " + i).toString(),
                                                documentSnapshot.get("product full title " + i).toString(),
                                                (long) documentSnapshot.get("free coupen " + i),
                                                documentSnapshot.get("average rating " + i).toString(),
                                                (long) documentSnapshot.get("total rating " + i),
                                                documentSnapshot.get("product price " + i).toString(),
                                                documentSnapshot.get("cutted price " + i).toString(),
                                                (boolean) documentSnapshot.get("cod " + i),
                                                (boolean) documentSnapshot.get("in stock " + i)));
                                    }
                                    lists.get(index).add(new HomeModel(HomeModel.HORIZONTAL_PRODUCT_VIEW, documentSnapshot.get("layout title").toString(), productItemModelList, viewAllprodectModelList));

                                } else if ((long) documentSnapshot.get("view type") == HomeModel.GRID_PRODUCT_VIEW) {

                                    List<ProductItemModel> productItemModelList = new ArrayList<>();
                                    for (long i = 1; i <= (long) documentSnapshot.get("number of products"); i++) {
                                        productItemModelList.add(new ProductItemModel(
                                                documentSnapshot.get("product id " + i).toString(),
                                                documentSnapshot.get("product image " + i).toString(),
                                                documentSnapshot.get("product title " + i).toString(),
                                                documentSnapshot.get("product sub title " + i).toString(), documentSnapshot.get("product price " + i).toString()));
                                    }
                                    lists.get(index).add(new HomeModel(HomeModel.GRID_PRODUCT_VIEW, documentSnapshot.get("layout title").toString(), productItemModelList));

                                }
                            }

                            HomeAdapter homeAdapter = new HomeAdapter(lists.get(index));
                            homeRecyclerView.setAdapter(homeAdapter);
                            homeAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        } else {
                            Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadWishList(final Context context, final Dialog dialog, final boolean loadProductData) {

        wishList.clear();
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my wishlist")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list size"); x++) {
                        wishList.add(task.getResult().get("product id " + x).toString());

                        if (DbQueries.wishList.contains(ProductDetailsActivity.productId)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                            if (addToWishlistButton != null) {
                                addToWishlistButton.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                            }
                        } else {
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            if (addToWishlistButton != null) {
                                addToWishlistButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                            }
                        }

                        if (loadProductData) {
                            wishListModelList.clear();
                            final String productId = task.getResult().get("product id " + x).toString();
                            firebaseFirestore.collection("products").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        final DocumentSnapshot documentSnapshot = task.getResult();
                                        FirebaseFirestore.getInstance().collection("products").document(productId).collection("quantity").orderBy("time", Query.Direction.ASCENDING).get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock quantity")) {
                                                                wishListModelList.add(new WishListModel(
                                                                        productId,
                                                                        documentSnapshot.get("product image 1").toString(),
                                                                        documentSnapshot.get("product title").toString(),
                                                                        (long) documentSnapshot.get("free coupens"),
                                                                        documentSnapshot.get("average rating").toString(),
                                                                        (long) documentSnapshot.get("total ratings"),
                                                                        documentSnapshot.get("product price").toString(),
                                                                        documentSnapshot.get("cutted price").toString(),
                                                                        (boolean) documentSnapshot.get("cod"),
                                                                        true));
                                                            } else {
                                                                wishListModelList.add(new WishListModel(
                                                                        productId,
                                                                        documentSnapshot.get("product image 1").toString(),
                                                                        documentSnapshot.get("product title").toString(),
                                                                        (long) documentSnapshot.get("free coupens"),
                                                                        documentSnapshot.get("average rating").toString(),
                                                                        (long) documentSnapshot.get("total ratings"),
                                                                        documentSnapshot.get("product price").toString(),
                                                                        documentSnapshot.get("cutted price").toString(),
                                                                        (boolean) documentSnapshot.get("cod"),
                                                                        false));
                                                            }
                                                            WishlistFragment.wishlistAdapter.notifyDataSetChanged();
                                                        } else {
                                                            Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView bedgeCount, final TextView cartTotalAmount) {

        cartList.clear();
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my cart")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list size"); x++) {
                        cartList.add(task.getResult().get("product id " + x).toString());

                        if (DbQueries.cartList.contains(ProductDetailsActivity.productId)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;
                        } else {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                        }

                        if (loadProductData) {
                            cartItemModelList.clear();
                            final String productId = task.getResult().get("product id " + x).toString();
                            firebaseFirestore.collection("products").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {


                                        final DocumentSnapshot documentSnapshot = task.getResult();
                                        FirebaseFirestore.getInstance().collection("products").document(productId).collection("quantity").orderBy("time", Query.Direction.ASCENDING).get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            int index = 0;
                                                            if (cartList.size() >= 2) {
                                                                index = cartList.size() - 2;
                                                            }

                                                            if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock quantity")) {
                                                                cartItemModelList.add(index, new CartItemModel(
                                                                        CartItemModel.CART_ITEM,
                                                                        productId,
                                                                        documentSnapshot.get("product image 1").toString(),
                                                                        documentSnapshot.get("product title").toString(),
                                                                        (long) documentSnapshot.get("free coupens"),
                                                                        documentSnapshot.get("product price").toString(),
                                                                        documentSnapshot.get("cutted price").toString(),
                                                                        1,
                                                                        (long) documentSnapshot.get("offers applied"),
                                                                        0,
                                                                        true,
                                                                        (long) documentSnapshot.get("max quantity"),
                                                                        (long) documentSnapshot.get("stock quantity"),
                                                                        documentSnapshot.getBoolean("cod")));

                                                            } else {
                                                                cartItemModelList.add(index, new CartItemModel(
                                                                        CartItemModel.CART_ITEM,
                                                                        productId,
                                                                        documentSnapshot.get("product image 1").toString(),
                                                                        documentSnapshot.get("product title").toString(),
                                                                        (long) documentSnapshot.get("free coupens"),
                                                                        documentSnapshot.get("product price").toString(),
                                                                        documentSnapshot.get("cutted price").toString(),
                                                                        1,
                                                                        (long) documentSnapshot.get("offers applied"),
                                                                        0,
                                                                        false,
                                                                        (long) documentSnapshot.get("max quantity"),
                                                                        (long) documentSnapshot.get("stock quantity"),
                                                                        documentSnapshot.getBoolean("cod")));
                                                            }
                                                            if (cartList.size() == 1) {
                                                                cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                                parent.setVisibility(View.VISIBLE);
                                                            }
                                                            if (cartList.size() == 0) {
                                                                cartItemModelList.clear();
                                                            }
                                                            CartFragment.cartAdapter.notifyDataSetChanged();
                                                        } else {
                                                            Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    if (cartList.size() != 0) {
                        bedgeCount.setVisibility(View.VISIBLE);
                    } else {
                        bedgeCount.setVisibility(View.INVISIBLE);
                    }
                    if (DbQueries.cartList.size() < 99) {
                        bedgeCount.setText(String.valueOf(DbQueries.cartList.size()));
                    } else {
                        bedgeCount.setText("99");
                    }
                } else {
                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFromWishList(final int index, final Context context) {
        final String removedProductId = wishList.get(index);
        wishList.remove(index);
        Map<String, Object> updateWishList = new HashMap<>();

        for (int x = 0; x < wishList.size(); x++) {
            updateWishList.put("product id " + x, wishList.get(x));
        }
        updateWishList.put("list size", (long) wishList.size());

        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my wishlist")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (wishListModelList.size() != 0) {
                        wishListModelList.remove(index);
                        WishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    if (addToWishlistButton != null) {
                        addToWishlistButton.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                    }
                    wishList.add(index, removedProductId);
                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                ProductDetailsActivity.runningWishlistQuery = false;
            }
        });
    }

    public static void removeFromCart(final int index, final Context context, final TextView cartTotalAmount) {
        final String removedProductId = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();

        for (int x = 0; x < cartList.size(); x++) {
            updateCartList.put("product id " + x, cartList.get(x));
        }
        updateCartList.put("list size", (long) cartList.size());

        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my cart")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (cartItemModelList.size() != 0) {
                        cartItemModelList.remove(index);
                        CartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartList.size() == 0) {
                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        cartItemModelList.clear();
                    }
                    Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    cartList.add(index, removedProductId);
                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                ProductDetailsActivity.runningCartQuery = false;
            }
        });
    }

    public static void loadRatingList(final Context context) {
        if (!ProductDetailsActivity.runningRaringQuery) {
            ProductDetailsActivity.runningRaringQuery = true;
            myRatedIds.clear();
            myRating.clear();

            firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my ratings")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        List<String> orderProductIds = new ArrayList<>();
                        for (int x = 0; x < orderItemModelList.size(); x++) {
                            orderProductIds.add(orderItemModelList.get(x).getProductId());
                        }

                        for (long x = 0; x < (long) task.getResult().get("list size"); x++) {
                            myRatedIds.add(task.getResult().get("product id " + x).toString());
                            myRating.add((Long) task.getResult().get("rating " + x));
                            if (task.getResult().get("product id " + x).toString().equals(productId)) {
                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((Long) task.getResult().get("rating " + x))) - 1;
                                if (ProductDetailsActivity.rateNowContainer != null)
                                    ProductDetailsActivity.setRaring(initialRating);
                            }
                            if (orderProductIds.contains(task.getResult().get("product id " + x).toString())) {
                                orderItemModelList.get(orderProductIds.indexOf(task.getResult().get("product id " + x).toString())).setRating(Integer.parseInt(String.valueOf((Long) task.getResult().get("rating " + x))) - 1);
                            }
                        }
                        if (OrderFragment.orderAdapter != null) {
                            OrderFragment.orderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.runningRaringQuery = false;
                }
            });
        }
    }

    public static void loadAddresses(final Dialog loadingDialog, final Context context, final boolean gotoDeliveryActivity) {

        addressesModelList.clear();

        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my addresses")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Intent deliveryIntent = null;
                    if ((long) task.getResult().get("list size") == 0) {
                        deliveryIntent = new Intent(context, AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    } else {

                        for (long x = 1; x < (long) task.getResult().get("list size") + 1; x++) {
                            addressesModelList.add(new AddressesModel(
                                    task.getResult().getBoolean("selected " + x),
                                    task.getResult().getString("city " + x),
                                    task.getResult().getString("locality " + x),
                                    task.getResult().getString("flatNo " + x),
                                    task.getResult().getString("pinCode " + x),
                                    task.getResult().getString("landMark " + x),
                                    task.getResult().getString("name " + x),
                                    task.getResult().getString("mobileNo " + x),
                                    task.getResult().getString("alternateMobileNo " + x),
                                    task.getResult().getString("state " + x))
                            );
                            if ((boolean) task.getResult().get("selected " + x)) {
                                selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        if (gotoDeliveryActivity) {
                            deliveryIntent = new Intent(context, DeliveryActivity.class);
                        }
                    }
                    if (gotoDeliveryActivity) {
                        context.startActivity(deliveryIntent);
                    }
                } else {
                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    public static void loadRewards(final Context context, final Dialog loadingDialog, final boolean onRewardFragment) {
        rewardsModelList.clear();

        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final Date lastSeenDate = task.getResult().getDate("last seen");

                            firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user rewards").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                    if (documentSnapshot.get("type").toString().equals("discount") && lastSeenDate.before(documentSnapshot.getDate("validity"))) {
                                                        rewardsModelList.add(new RewardsModel(
                                                                documentSnapshot.getId(),
                                                                documentSnapshot.get("type").toString(),
                                                                documentSnapshot.get("lower limit").toString(),
                                                                documentSnapshot.get("upper limit").toString(),
                                                                documentSnapshot.get("percentage").toString(),
                                                                documentSnapshot.get("body").toString(),
                                                                (Date) documentSnapshot.get("validity"),
                                                                (Boolean) documentSnapshot.getBoolean("already used")));
                                                    } else if (documentSnapshot.get("type").toString().equals("Flat Rs.*OFF") && lastSeenDate.before(documentSnapshot.getDate("validity"))) {
                                                        rewardsModelList.add(new RewardsModel(
                                                                documentSnapshot.getId(),
                                                                documentSnapshot.get("type").toString(),
                                                                documentSnapshot.get("lower limit").toString(),
                                                                documentSnapshot.get("upper limit").toString(),
                                                                documentSnapshot.get("amount").toString(),
                                                                documentSnapshot.get("body").toString(),
                                                                (Date) documentSnapshot.get("validity"),
                                                                (Boolean) documentSnapshot.getBoolean("already used")));
                                                    }
                                                }
                                                if (onRewardFragment) {
                                                    RewardsFragment.rewordsAdapter.notifyDataSetChanged();
                                                }
                                            } else {
                                                Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            loadingDialog.dismiss();
                                        }
                                    });

                        } else {
                            Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadOrders(final Context context, @Nullable final OrderAdapter orderAdapter, final Dialog loadingDialog) {

        orderItemModelList.clear();
        firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user orders").orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        firebaseFirestore.collection("orders").document(documentSnapshot.getString("order id")).collection("order items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot orderItems : task.getResult().getDocuments()) {

                                        OrderItemModel orderItemModel = new OrderItemModel(
                                                orderItems.getString("product id"),
                                                orderItems.getString("order status"),
                                                orderItems.getString("address"),
                                                orderItems.getString("coupon id"),
                                                orderItems.getString("cutted price"),
                                                orderItems.getDate("ordered date"),
                                                orderItems.getDate("packed date"),
                                                orderItems.getDate("shipped date"),
                                                orderItems.getDate("delivered date"),
                                                orderItems.getDate("canceled date"),
                                                orderItems.getString("discounted price"),
                                                orderItems.getLong("free coupons"),
                                                orderItems.getString("full name"),
                                                orderItems.getString("order id"),
                                                orderItems.getString("payment method"),
                                                orderItems.getString("pin code"),
                                                orderItems.getString("product price"),
                                                orderItems.getLong("product quantity"),
                                                orderItems.getString("user id"),
                                                orderItems.getString("product image"),
                                                orderItems.getString("product title"),
                                                orderItems.getString("delivery price"),
                                                orderItems.getBoolean("cancellation requested"));
                                        orderItemModelList.add(orderItemModel);
                                    }
                                    loadRatingList(context);
                                    if (orderAdapter != null) {
                                        orderAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });

                    }
                } else {
                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void checkNotifications(boolean remove, @Nullable final TextView notifyCount) {

        if (remove) {
            registration.remove();
        } else {
            registration = firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my notifications").
                    addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                notificationModelList.clear();
                                int unread = 0;
                                for (long x = 0; x < (long) documentSnapshot.get("list size"); x++) {
                                    notificationModelList.add(0,new NotificationModel(documentSnapshot.get("image " + x).toString(), documentSnapshot.get("body " + x).toString(), documentSnapshot.getBoolean("readed " + x)));
                                    if (!documentSnapshot.getBoolean("readed " + x)) {
                                        unread++;
                                        if (notifyCount != null) {
                                            if (unread > 0) {
                                                notifyCount.setVisibility(View.VISIBLE);
                                                if (unread < 99) {
                                                    notifyCount.setText(String.valueOf(unread));
                                                } else {
                                                    notifyCount.setText("99");
                                                }
                                            }else {
                                                notifyCount.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    }
                                }
                                if (NotificationActivity.notificationAdapter != null) {
                                    NotificationActivity.notificationAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });
        }

    }

    public static void clearData() {
        categoryModelList.clear();
        lists.clear();
        loadedCategoryNames.clear();
        wishList.clear();
        wishListModelList.clear();
        cartList.clear();
        cartItemModelList.clear();
        myRatedIds.clear();
        myRating.clear();
        addressesModelList.clear();
        rewardsModelList.clear();
        orderItemModelList.clear();
    }
}












