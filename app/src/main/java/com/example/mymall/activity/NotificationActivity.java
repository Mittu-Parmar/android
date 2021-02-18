package com.example.mymall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mymall.R;
import com.example.mymall.adapter.NotificationAdapter;
import com.example.mymall.db_handler.DbQueries;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static NotificationAdapter notificationAdapter;
    private boolean runQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.notification_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        notificationAdapter = new NotificationAdapter(DbQueries.notificationModelList);
        recyclerView.setAdapter(notificationAdapter);

        Map<String, Object> readMap = new HashMap<>();

        for (int i = 0; i < DbQueries.notificationModelList.size(); i++) {
            if (!DbQueries.notificationModelList.get(i).isReaded()) {
                runQuery = true;
            }
            readMap.put("readed " + i, true);
        }
        if (runQuery) {
            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).collection("user data").document("my notifications").
                    update(readMap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < DbQueries.notificationModelList.size(); i++) {
            DbQueries.notificationModelList.get(i).setReaded(true);
        }
    }
}
