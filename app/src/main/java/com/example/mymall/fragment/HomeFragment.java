package com.example.mymall.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.activity.MainActivity;
import com.example.mymall.adapter.CategoryAdapter;
import com.example.mymall.adapter.HomeAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.model.CategoryModel;
import com.example.mymall.model.HomeModel;
import com.example.mymall.model.ProductItemModel;
import com.example.mymall.model.SliderModel;
import com.example.mymall.model.WishListModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.mymall.db_handler.DbQueries.categoryModelList;
import static com.example.mymall.db_handler.DbQueries.lists;
import static com.example.mymall.db_handler.DbQueries.loadCategories;
import static com.example.mymall.db_handler.DbQueries.loadFragmentsData;
import static com.example.mymall.db_handler.DbQueries.loadedCategoryNames;

public class HomeFragment extends Fragment {

    public static ConnectivityManager connectivityManager;
    public static NetworkInfo networkInfo;
    private TextView noInternet;
    private ImageView noInternetImageView;
    private Button noInternetButton;

    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    RecyclerView homeRecyclerView;
    HomeAdapter homeAdapter;

    public static SwipeRefreshLayout swipeRefreshLayout;
    private List<CategoryModel> categoryModelFakeList =new ArrayList<>();
    List<HomeModel> homeModelFakeList=new ArrayList<>();

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));
        homeRecyclerView = view.findViewById(R.id.home_page_recycler_view);
        noInternet =view.findViewById(R.id.no_internet);
        noInternetButton=view.findViewById(R.id.no_internet_btn);
        noInternetImageView=view.findViewById(R.id.no_internet_img);
        Glide.with(container.getContext()).load(R.drawable.no_internet_img).apply(new RequestOptions().placeholder(R.drawable.home_icon)).into(noInternetImageView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(layoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testingLinearLayoutManager = new LinearLayoutManager(getContext());
        testingLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRecyclerView.setLayoutManager(testingLinearLayoutManager);

//        category Model Fake ListList
        categoryModelFakeList.add(new CategoryModel("null",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));

//        category Model Fake ListList

//        home Fake ListList
        List<SliderModel>sliderModelFakeList=new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null"));
        sliderModelFakeList.add(new SliderModel("null"));
        sliderModelFakeList.add(new SliderModel("null"));
        sliderModelFakeList.add(new SliderModel("null"));
        sliderModelFakeList.add(new SliderModel("null"));

        List<ProductItemModel> productItemModelFakeList=new ArrayList<>();
        productItemModelFakeList.add(new ProductItemModel("","","","",""));
        productItemModelFakeList.add(new ProductItemModel("","","","",""));
        productItemModelFakeList.add(new ProductItemModel("","","","",""));
        productItemModelFakeList.add(new ProductItemModel("","","","",""));
        productItemModelFakeList.add(new ProductItemModel("","","","",""));
        productItemModelFakeList.add(new ProductItemModel("","","","",""));
        productItemModelFakeList.add(new ProductItemModel("","","","",""));
        productItemModelFakeList.add(new ProductItemModel("","","","",""));

        homeModelFakeList.add(new HomeModel(HomeModel.BANNER_SLIDER,sliderModelFakeList));
        homeModelFakeList.add(new HomeModel(HomeModel.STRIP_AD,""));
        homeModelFakeList.add(new HomeModel(HomeModel.HORIZONTAL_PRODUCT_VIEW,"",productItemModelFakeList,new ArrayList<WishListModel>()));
        homeModelFakeList.add(new HomeModel(HomeModel.GRID_PRODUCT_VIEW,"",productItemModelFakeList));
//        home Fake ListList


        categoryAdapter = new CategoryAdapter(categoryModelFakeList);

        homeAdapter = new HomeAdapter(homeModelFakeList);


        connectivityManager= (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isConnected() == true){
            MainActivity.showNavigationItems(true);
            noInternet.setVisibility(View.GONE);
            noInternetButton.setVisibility(View.GONE);
            noInternetImageView.setVisibility(View.GONE);

            categoryRecyclerView.setVisibility(View.VISIBLE);
            homeRecyclerView.setVisibility(View.VISIBLE);


            if (categoryModelList.isEmpty()) {
                loadCategories(categoryRecyclerView, getContext());
            } else {
                categoryAdapter=new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }

            categoryRecyclerView.setAdapter(categoryAdapter);

            if (lists.isEmpty()) {
                loadedCategoryNames.add("Home");
                lists.add(new ArrayList<HomeModel>());
                loadFragmentsData(homeRecyclerView, getContext(),0,"Home");
            } else {
                homeAdapter = new HomeAdapter(lists.get(0));
                homeAdapter.notifyDataSetChanged();
            }

            homeRecyclerView.setAdapter(homeAdapter);

        }
        else {
            MainActivity.showNavigationItems(false);
            noInternet.setVisibility(View.VISIBLE);
            noInternetButton.setVisibility(View.VISIBLE);
            noInternetImageView.setVisibility(View.VISIBLE);

            categoryRecyclerView.setVisibility(View.GONE);
            homeRecyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }

    //==========Refresh Layout=========

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });

        noInternetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });

        return view;
    }
    @SuppressLint("WrongConstant")
    private void reloadPage(){
        connectivityManager= (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

//        categoryModelList.clear();
//        lists.clear();
//        loadedCategoryNames.clear();

        DbQueries.clearData();

        networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isConnected() == true){
            MainActivity.showNavigationItems(true);
            noInternet.setVisibility(View.GONE);
            noInternetButton.setVisibility(View.GONE);
            noInternetImageView.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homeRecyclerView.setVisibility(View.VISIBLE);

            categoryAdapter=new CategoryAdapter(categoryModelFakeList);
            homeAdapter=new HomeAdapter(homeModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homeRecyclerView.setAdapter(homeAdapter);

            loadCategories(categoryRecyclerView, getContext());
            loadedCategoryNames.add("Home");
            lists.add(new ArrayList<HomeModel>());
            loadFragmentsData(homeRecyclerView, getContext(),0,"Home");
        }else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();

            MainActivity.showNavigationItems(false);
            noInternet.setVisibility(View.VISIBLE);
            noInternetButton.setVisibility(View.VISIBLE);
            noInternetImageView.setVisibility(View.VISIBLE);
            categoryRecyclerView.setVisibility(View.GONE);
            homeRecyclerView.setVisibility(View.GONE);

            swipeRefreshLayout.setRefreshing(false);

        }
    }
}














