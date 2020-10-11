package com.example.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapter.HomeAdapter;
import com.example.mymall.model.HomeModel;
import com.example.mymall.model.ProductItemModel;
import com.example.mymall.model.SliderModel;
import com.example.mymall.model.WishListModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.mymall.db_handler.DbQueries.lists;
import static com.example.mymall.db_handler.DbQueries.loadFragmentsData;
import static com.example.mymall.db_handler.DbQueries.loadedCategoryNames;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView categoryRecyclerView;
    List<HomeModel> homeModelFakeList=new ArrayList<>();

    String title;
    HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryRecyclerView=findViewById(R.id.category_item_recycler_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        title=getIntent().getStringExtra("categoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


        LinearLayoutManager testingLinearLayoutManager=new LinearLayoutManager(this);
        testingLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLinearLayoutManager);

        homeAdapter=new HomeAdapter(homeModelFakeList);

        int listPosition=0;
        for (int x = 0; x < loadedCategoryNames.size(); x++) {
            if (loadedCategoryNames.get(x).equals(title.toUpperCase())){
                listPosition=x;
            }
        }
        if (listPosition==0){
            loadedCategoryNames.add(title);
            lists.add(new ArrayList<HomeModel>());
            loadFragmentsData(categoryRecyclerView, this,loadedCategoryNames.size()-1,title);
        }else {
            homeAdapter = new HomeAdapter(lists.get(listPosition));
        }

        categoryRecyclerView.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();

        //    ==============testing testing testing============


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.menu_search) {
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            return true;
        }else if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
