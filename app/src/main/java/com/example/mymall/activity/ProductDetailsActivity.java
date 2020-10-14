package com.example.mymall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapter.ProductDetailsAdapter;
import com.example.mymall.adapter.ProductImagesAdapter;
import com.example.mymall.adapter.RewordsAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.fragment.ProductSpaeificationFragment;
import com.example.mymall.fragment.SigninFragment;
import com.example.mymall.fragment.SignupFragment;
import com.example.mymall.model.CartItemModel;
import com.example.mymall.model.ProductSpecificationModel;
import com.example.mymall.model.RewordsModel;
import com.example.mymall.model.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mymall.activity.RegisterActivity.setSignUpFregment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static Activity productDetailsActivity;

    public static boolean runningWishlistQuery = false;
    public static boolean runningRaringQuery = false;
    public static boolean runningCartQuery = false;

    //    =============coupen Dialog================
    public static TextView coupenTitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;

    private static RecyclerView coupensRecyclerView;
    private static LinearLayout selectedCoupenLinearLayout;
//    =============coupen Dialog================


    //    ==========sign in up dialog==========
    private Dialog signInOutDialog;
    private Button signIntDialogButton;
    private Button signUpDialogButton;
//    ==========sign in up dialog==========

    private Dialog loadingDialog;


    ViewPager productImagesViewPager;
    TextView productTitle;
    TextView productPrice;
    TextView cuttedtPrice;
    TextView codIndicatorTextView;
    ImageView codIndicatorImageView;
    TextView averageRatingMiniView;
    TextView totalRatingMiniView;
    TabLayout productImagesIndigator;
    public static FloatingActionButton addToWishlistButton;
    TextView rewordTitleTextView;
    TextView rewordBodyTextView;

    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    //==========product discription============
    ConstraintLayout productDetailsOnlyContainer;
    ConstraintLayout productDetailsTabsContainer;
    ViewPager productDetailsViewPager;
    TabLayout productDetailsTabLayout;
    private String productDescription;
    private String productOtherDetails;
    TextView productOnlyDiscriptionBody;
//==========product discription============

    //==========Ratings layoyt============
    public static int initialRating;
    TextView totalRatings;
    TextView totalRatingFigure;
    public static LinearLayout rateNowContainer;
    LinearLayout ratingsNumbersContainer;
    LinearLayout ratingsProgressBarContainer;
    TextView averageRatings;
//==========Ratings layoyt============

    private Button buyNowButton;
    private LinearLayout addToCartButton;

    private LinearLayout coupenRedeemLayout;
    Button coupenRedeemButton;
    FirebaseFirestore firebaseFirestore;
    private static FirebaseUser currentUser;
    private TextView bedgeCount;
    private DocumentSnapshot documentSnapshot;

    public static String productId;

    public static MenuItem cartItem;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_image_view_pager);
        productImagesIndigator = findViewById(R.id.view_pager_indigator);
        addToWishlistButton = findViewById(R.id.add_to_wishlist_button);
        productDetailsViewPager = findViewById(R.id.product_details_view_pager);
        productDetailsTabLayout = findViewById(R.id.product_details_tabLayout);
        buyNowButton = findViewById(R.id.buy_now);
        addToCartButton = findViewById(R.id.add_to_cart_btn);
        coupenRedeemButton = findViewById(R.id.coupen_redeemption_button);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.wishlist_item_star_rating);
        totalRatingMiniView = findViewById(R.id.total_rating_mini_iew);
        productPrice = findViewById(R.id.product_price);
        cuttedtPrice = findViewById(R.id.cutted_price);
        codIndicatorTextView = findViewById(R.id.cod_indigator_text_ciew);
        codIndicatorImageView = findViewById(R.id.cod_indigator_image_view);
        rewordTitleTextView = findViewById(R.id.reword_title);
        rewordBodyTextView = findViewById(R.id.reword_body);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productDetailsTabsContainer = findViewById(R.id.product_details_tab_container);
        productOnlyDiscriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNumbersContainer = findViewById(R.id.tstimgs_numbers_container);
        totalRatingFigure = findViewById(R.id.total_ratings__feagure);
        ratingsProgressBarContainer = findViewById(R.id.rating_progress_bar_container);
        averageRatings = findViewById(R.id.average_rating);
        coupenRedeemLayout = findViewById(R.id.coupen_redemption_layout);

        initialRating = -1;

        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.layout_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImagesList = new ArrayList<>();

        productId = getIntent().getStringExtra("product id");
        firebaseFirestore.collection("products").document(productId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    for (long i = 1; i < (long) documentSnapshot.get("number of product images") + 1; i++) {
                        productImagesList.add(documentSnapshot.get("product image " + i).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImagesList);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product title").toString());
                    averageRatingMiniView.setText(documentSnapshot.get("average rating").toString());
                    totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total ratings") + " ratings)");
                    productPrice.setText("Rs." + documentSnapshot.get("product price").toString() + "/");
                    cuttedtPrice.setText("Rs." + documentSnapshot.get("cutted price").toString() + "/");
                    if ((boolean) documentSnapshot.get("cod")) {
                        codIndicatorImageView.setVisibility(View.VISIBLE);
                        codIndicatorTextView.setVisibility(View.VISIBLE);
                    } else {
                        codIndicatorImageView.setVisibility(View.INVISIBLE);
                        codIndicatorTextView.setVisibility(View.INVISIBLE);
                    }
                    rewordTitleTextView.setText((long) documentSnapshot.get("free coupens") + " " + documentSnapshot.get("free coupen title").toString());
                    rewordBodyTextView.setText(documentSnapshot.get("free coupen body").toString());
                    if ((boolean) documentSnapshot.get("use tab layout")) {
                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        productDetailsOnlyContainer.setVisibility(View.GONE);

                        productDescription = documentSnapshot.get("product discription").toString();
                        productOtherDetails = documentSnapshot.get("product other details").toString();
                        productSpecificationModelList = new ArrayList<>();

                        for (long x = 1; x < (long) documentSnapshot.get("total specification titles") + 1; x++) {
                            productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("specification title " + x).toString()));
                            for (long y = 1; y < (long) documentSnapshot.get("specification title " + x + " total fields ") + 1; y++) {
                                productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("specification title " + x + " field " + y + " name").toString(), documentSnapshot.get("specification title " + x + " field " + y + " value").toString()));
                            }
                        }

                    } else {
                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDiscriptionBody.setText(documentSnapshot.get("product discription").toString());
                    }

                    totalRatings.setText((long) documentSnapshot.get("total ratings") + " Ratings");
                    for (int i = 0; i < 5; i++) {

                        TextView rating = (TextView) ratingsNumbersContainer.getChildAt(i);
                        rating.setText(String.valueOf(documentSnapshot.get(5 - i + " star")));

                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(i);
                        progressBar.setMax(Integer.parseInt(String.valueOf(documentSnapshot.get("total ratings"))));
                        progressBar.setProgress(Integer.parseInt(String.valueOf(documentSnapshot.get(5 - i + " star"))));
                    }
                    totalRatingFigure.setText(String.valueOf((long) documentSnapshot.get("total ratings")));

                    averageRatings.setText(documentSnapshot.get("average rating").toString());
                    productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));

                    if (currentUser != null) {
                        if (DbQueries.myRating.size() == 0) {
                            DbQueries.loadRatingList(ProductDetailsActivity.this);
                        }
                        if (DbQueries.cartList.size() == 0) {
                            DbQueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false,bedgeCount,new TextView(ProductDetailsActivity.this));
                        }
                        if (DbQueries.wishList.size() == 0) {
                            DbQueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                        } else {
                            loadingDialog.dismiss();
                        }
                    } else {
                        loadingDialog.dismiss();
                    }

                    if (DbQueries.myRatedIds.contains(productId)) {
                        int index = DbQueries.myRatedIds.indexOf(productId);
                        initialRating = Integer.parseInt(String.valueOf(DbQueries.myRating.get(index))) - 1;
                        setRaring(initialRating);
                    }

                    if (DbQueries.cartList.contains(productId)) {
                        ALREADY_ADDED_TO_CART = true;
                    } else {
                        ALREADY_ADDED_TO_CART = false;
                    }

                    if (DbQueries.wishList.contains(productId)) {
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishlistButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                    } else {
                        ALREADY_ADDED_TO_WISHLIST = false;
                        addToWishlistButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                    }
                    if ((boolean) documentSnapshot.get("in stock")) {

                        addToCartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (currentUser == null) {
                                    signInOutDialog.show();
                                } else {
                                    if (!runningCartQuery) {
                                        runningCartQuery = true;
                                        if (ALREADY_ADDED_TO_CART) {
                                            runningCartQuery = false;
                                            Toast.makeText(ProductDetailsActivity.this, "Already Added to cart", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Map<String, Object> addProduct = new HashMap<>();
                                            addProduct.put("product id " + DbQueries.cartList.size(), productId);
                                            addProduct.put("list size", (long) (DbQueries.cartList.size() + 1));

                                            firebaseFirestore.collection("users").document(currentUser.getUid()).collection("user data").document("my cart")
                                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (DbQueries.cartItemModelList.size() != 0) {
                                                            DbQueries.cartItemModelList.add(0,new CartItemModel(
                                                                    CartItemModel.CART_ITEM,
                                                                    productId,
                                                                    documentSnapshot.get("product image 1").toString(),
                                                                    documentSnapshot.get("product title").toString(),
                                                                    (long) documentSnapshot.get("free coupens"),
                                                                    documentSnapshot.get("product price").toString(),
                                                                    documentSnapshot.get("cutted price").toString(),
                                                                    1,
                                                                    0,
                                                                    0,
                                                                    (boolean) documentSnapshot.get("cod")));
                                                        }

                                                        ALREADY_ADDED_TO_CART = true;
                                                        DbQueries.cartList.add(productId);
                                                        Toast.makeText(ProductDetailsActivity.this, "Added to Cart successfully", Toast.LENGTH_SHORT).show();
                                                        invalidateOptionsMenu();
                                                        runningCartQuery = false;
                                                    } else {
                                                        runningCartQuery = false;
                                                        Toast.makeText(ProductDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }

                            }
                        });

                    } else {
                        buyNowButton.setVisibility(View.GONE);
                        TextView outOfStock = (TextView) addToCartButton.getChildAt(0);
                        outOfStock.setText("Out of stock");
                        outOfStock.setTextColor(getResources().getColor(R.color.colorPrimary));
                        outOfStock.setCompoundDrawables(null, null, null, null);
                    }

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(ProductDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        productImagesIndigator.setupWithViewPager(productImagesViewPager, true);

        addToWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInOutDialog.show();
                } else {
                    if (!runningWishlistQuery) {
                        runningWishlistQuery = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DbQueries.wishList.indexOf(productId);
                            DbQueries.removeFromWishList(index, ProductDetailsActivity.this);
                            addToWishlistButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                        } else {
                            addToWishlistButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product id " + DbQueries.wishList.size(), productId);
                            addProduct.put("list size", (long) (DbQueries.wishList.size() + 1));

                            firebaseFirestore.collection("users").document(currentUser.getUid()).collection("user data").document("my wishlist")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        if (DbQueries.wishListModelList.size() != 0) {
                                            DbQueries.wishListModelList.add(new WishListModel(
                                                    productId,
                                                    documentSnapshot.get("product image 1").toString(),
                                                    documentSnapshot.get("product full title").toString(),
                                                    (long) documentSnapshot.get("free coupen"),
                                                    documentSnapshot.get("average rating").toString(),
                                                    (long) documentSnapshot.get("total rating"),
                                                    documentSnapshot.get("product price").toString(),
                                                    documentSnapshot.get("cutted price").toString(),
                                                    (boolean) documentSnapshot.get("cod"),
                                                    (boolean) documentSnapshot.get("in stock")));
                                        }

                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        addToWishlistButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                        DbQueries.wishList.add(productId);
                                        Toast.makeText(ProductDetailsActivity.this, "Added to WishList successfully", Toast.LENGTH_SHORT).show();

                                    } else {
                                        addToWishlistButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
                                        Toast.makeText(ProductDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    runningWishlistQuery = false;
                                }
                            });
                        }
                    }
                }
            }
        });


        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //================rateing layout=========
        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInOutDialog.show();
                    } else {
                        if (starPosition != initialRating) {
                            if (!runningRaringQuery) {
                                runningRaringQuery = true;
                                setRaring(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DbQueries.myRatedIds.contains(productId)) {

                                    TextView oldRating = (TextView) ratingsNumbersContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNumbersContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initialRating + 1 + " star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + " star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average rating", calculateAverageRating(starPosition - initialRating, true));
                                } else {
                                    updateRating.put(starPosition + 1 + " star", (long) documentSnapshot.get(starPosition + 1 + " star") + 1);
                                    updateRating.put("average rating", calculateAverageRating(starPosition + 1, false));
                                    updateRating.put("total ratings", (long) documentSnapshot.get("total ratings") + 1);
                                }

                                firebaseFirestore.collection("products").document(productId)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Map<String, Object> myRating = new HashMap<>();
                                            if (DbQueries.myRatedIds.contains(productId)) {
                                                myRating.put("rating " + DbQueries.myRatedIds.indexOf(productId), (long) starPosition + 1);
                                            } else {
                                                myRating.put("list size", (long) DbQueries.myRatedIds.size() + 1);
                                                myRating.put("product id " + DbQueries.myRatedIds.size(), productId);
                                                myRating.put("rating " + DbQueries.myRatedIds.size(), (long) starPosition + 1);
                                            }
                                            firebaseFirestore.collection("users").document(currentUser.getUid()).collection("user data").document("my ratings")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (DbQueries.myRatedIds.contains(productId)) {

                                                            DbQueries.myRating.set(DbQueries.myRatedIds.indexOf(productId), (long) starPosition + 1);

                                                            TextView oldRating = (TextView) ratingsNumbersContainer.getChildAt(5 - initialRating - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));

                                                            TextView finalRating = (TextView) ratingsNumbersContainer.getChildAt(5 - starPosition - 1);
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                        } else {
                                                            DbQueries.myRatedIds.add(productId);
                                                            DbQueries.myRating.add((long) (starPosition + 1));

                                                            TextView rating = (TextView) ratingsNumbersContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));
                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total ratings") + 1) + " ratings)");
                                                            totalRatings.setText((long) documentSnapshot.get("total ratings") + 1 + " Ratings");
                                                            totalRatingFigure.setText(String.valueOf((long) documentSnapshot.get("total ratings") + 1));
                                                            Toast.makeText(ProductDetailsActivity.this, "Thank you for Rating", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int i = 0; i < 5; i++) {
                                                            TextView ratingFigures = (TextView) ratingsNumbersContainer.getChildAt(i);
                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(i);
                                                            progressBar.setMax(Integer.parseInt(totalRatingFigure.getText().toString()));
                                                            progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                                                        }
                                                        initialRating = starPosition;
                                                        averageRatings.setText(calculateAverageRating(0, true));
                                                        averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                        if (DbQueries.wishList.contains(productId) && DbQueries.wishListModelList.size() != 0) {
                                                            int index = DbQueries.wishList.indexOf(productId);
                                                            DbQueries.wishListModelList.get(index).setRating(averageRatings.getText().toString());
                                                            DbQueries.wishListModelList.get(index).setTotalRatings(Long.parseLong(totalRatingFigure.getText().toString()));
                                                        }

                                                    } else {
                                                        setRaring(initialRating);
                                                        Toast.makeText(ProductDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    runningRaringQuery = false;
                                                }
                                            });

                                        } else {
                                            runningRaringQuery = false;
                                            setRaring(initialRating);
                                            Toast.makeText(ProductDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    }
                }
            });
        }
        //================rateing layout=========

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser == null) {
                    signInOutDialog.show();
                } else {
                    DeliveryActivity.fromCart=false;
                    loadingDialog.show();
                    productDetailsActivity=ProductDetailsActivity.this;
                    DeliveryActivity.cartItemModelList =new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(
                            CartItemModel.CART_ITEM,
                            productId,
                            documentSnapshot.get("product image 1").toString(),
                            documentSnapshot.get("product title").toString(),
                            (long) documentSnapshot.get("free coupens"),
                            documentSnapshot.get("product price").toString(),
                            documentSnapshot.get("cutted price").toString(),
                            1,
                            0,
                            0,
                            (boolean) documentSnapshot.get("in stock")));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                    if (DbQueries.addressModelList.size()==0) {
                        DbQueries.loadAddresses(loadingDialog, ProductDetailsActivity.this);
                    }else {
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });

        coupenRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog checkCoupenPriceDialog = new Dialog(ProductDetailsActivity.this);
                checkCoupenPriceDialog.setContentView(R.layout.coupem_redeem_dialog);
                checkCoupenPriceDialog.setCancelable(true);
                checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                ImageView toggalRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggal_recycler_view);
                coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recycler_view);
                selectedCoupenLinearLayout = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);

                coupenTitle = checkCoupenPriceDialog.findViewById(R.id.rewords_item_coupen_title);
                coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.rewords_item_coupen_validity);
                coupenBody = checkCoupenPriceDialog.findViewById(R.id.rewords_item_coupen_body);

                TextView originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
                TextView discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);

                LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                coupensRecyclerView.setLayoutManager(layoutManager);

                List<RewordsModel> rewordsModelList = new ArrayList<>();
                rewordsModelList.add(new RewordsModel("SELL", "Till 22 Aug 2020", "Buy above Rs.500 and get Rs.100 flat discount"));
                rewordsModelList.add(new RewordsModel("SELL", "Till 3 mar 2020", "Buy above Rs.300 and get Rs.50 flat discount"));
                rewordsModelList.add(new RewordsModel("SELL", "Till 22 Aug 2020", "Buy above Rs.500 and get Rs.100 flat discount"));
                rewordsModelList.add(new RewordsModel("DISCOUNT", "Till 1 Aug 2020", "Buy above Rs.1000 and get Rs.200 flat discount"));

                RewordsAdapter rewordsAdapter = new RewordsAdapter(rewordsModelList, true);
                coupensRecyclerView.setAdapter(rewordsAdapter);
                rewordsAdapter.notifyDataSetChanged();

                toggalRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogRecyclerView();
                    }
                });

                checkCoupenPriceDialog.show();
            }
        });

//        ==========sign in up dialog=========

        signInOutDialog = new Dialog(ProductDetailsActivity.this);
        signInOutDialog.setContentView(R.layout.sign_in_dialog);
        signInOutDialog.setCancelable(true);
        signInOutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        signIntDialogButton = signInOutDialog.findViewById(R.id.sign_In_dialog_btn);
        signUpDialogButton = signInOutDialog.findViewById(R.id.sign_up_dialog_btn);
        final Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);

        signIntDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SigninFragment.disableCloseButton = true;
                SignupFragment.disableCloseButton = true;

                signInOutDialog.dismiss();
                setSignUpFregment = false;
                startActivity(registerIntent);
            }
        });

        signUpDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignupFragment.disableCloseButton = true;
                SigninFragment.disableCloseButton = true;

                signInOutDialog.dismiss();
                setSignUpFregment = true;
                startActivity(registerIntent);
            }
        });

//        ==========sign in up dialog=========


    }

    public static void showDialogRecyclerView() {

        if (coupensRecyclerView.getVisibility() == View.GONE) {
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupenLinearLayout.setVisibility(View.GONE);
        } else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupenLinearLayout.setVisibility(View.VISIBLE);
        }

    }

    //================rateing layout=========
    public static void setRaring(int starPosition) {

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
            } else {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#C5C5C5")));
            }
        }
    }

    private String calculateAverageRating(long curruntUserRating, boolean update) {
        double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNumber = (TextView) ratingsNumbersContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNumber.getText().toString()) * x);
        }
        totalStars = totalStars + curruntUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }
    //================rateing layout=========

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            coupenRedeemLayout.setVisibility(View.GONE);
        } else {
            coupenRedeemLayout.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (DbQueries.myRating.size() == 0) {
                DbQueries.loadRatingList(ProductDetailsActivity.this);
            }

            if (DbQueries.wishList.isEmpty()) {
                DbQueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
            } else {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }

        if (DbQueries.myRatedIds.contains(productId)) {
            int index = DbQueries.myRatedIds.indexOf(productId);
            initialRating = Integer.parseInt(String.valueOf(DbQueries.myRating.get(index))) - 1;
            setRaring(initialRating);
        }

        if (DbQueries.cartList.contains(productId)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }

        if (DbQueries.wishList.contains(productId)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        } else {
            ALREADY_ADDED_TO_WISHLIST = false;
            addToWishlistButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#ACACAC")));
        }
        invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);


        cartItem = menu.findItem(R.id.menu_cart);
        cartItem.setActionView(R.layout.badge_layout);
        ImageView bedgeIcon = cartItem.getActionView().findViewById(R.id.bedge_icon);
        bedgeIcon.setImageResource(R.drawable.cart_icon);
        bedgeCount = cartItem.getActionView().findViewById(R.id.bedge_count);

        if (currentUser != null) {
            if (DbQueries.cartList.size() == 0) {
                DbQueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, bedgeCount,new TextView(ProductDetailsActivity.this));
            }else {
                bedgeCount.setVisibility(View.VISIBLE);
                if (DbQueries.cartList.size() < 99) {
                    bedgeCount.setText(String.valueOf(DbQueries.cartList.size()));
                } else {
                    bedgeCount.setText("99");
                }
            }
        }

        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInOutDialog.show();
                } else {
                    Intent cartFragmentActivityIntent = new Intent(ProductDetailsActivity.this, CartFragmentActivity.class);
                    startActivity(cartFragmentActivityIntent);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            productDetailsActivity=null;
            finish();
            return true;
        } else if (id == R.id.menu_search) {

            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_cart) {
            if (currentUser == null) {
                signInOutDialog.show();
            } else {
                SigninFragment.disableCloseButton = true;
                SignupFragment.disableCloseButton = true;
                Intent cartFragmentActivityIntent = new Intent(ProductDetailsActivity.this, CartFragmentActivity.class);
                startActivity(cartFragmentActivityIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity=null;
        super.onBackPressed();
    }
}
