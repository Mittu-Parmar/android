package com.example.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.adapter.WishlistAdapter;
import com.example.mymall.model.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.text_view);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        final List<WishListModel> list = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        final Adapter adapter = new Adapter(list, false);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {

                list.clear();
                ids.clear();

                final String[] tags = s.toLowerCase().split(" ");
                for (final String tag : tags) {
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("products").whereArrayContains("tags", tag).
                            get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshots : task.getResult().getDocuments()) {

                                    WishListModel model = new WishListModel(
                                            documentSnapshots.getId(),
                                            documentSnapshots.get("product image 1").toString(),
                                            documentSnapshots.get("product title").toString(),
                                            (long) documentSnapshots.get("free coupens"),
                                            documentSnapshots.get("average rating").toString(),
                                            (long) documentSnapshots.get("total ratings"),
                                            documentSnapshots.get("product price").toString(),
                                            documentSnapshots.get("cutted price").toString(),
                                            (boolean) documentSnapshots.get("cod"),
                                            true);

                                    if (!ids.contains(model.getProductId())) {
                                        list.add(model);
                                        ids.add(model.getProductId());
                                    }
                                }
                                if (tag.equals(tags[tags.length - 1])) {
                                    if (list.size() == 0) {
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    } else {
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter.getFilter().filter(s);
                                    }
                                }
                            } else {
                                Toast.makeText(SearchActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    class Adapter extends WishlistAdapter implements Filterable {

        public Adapter(List<WishListModel> wishListModelList, boolean isFromViewAll) {
            super(wishListModelList, isFromViewAll);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    // TODO: 16-02-2021 Filter logic

                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    notifyDataSetChanged();

                }
            };
        }
    }

}
