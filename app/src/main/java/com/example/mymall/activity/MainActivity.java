package com.example.mymall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.db_handler.DbQueries;
import com.example.mymall.fragment.SigninFragment;
import com.example.mymall.fragment.SignupFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.example.mymall.activity.RegisterActivity.setSignUpFregment;
import static com.example.mymall.db_handler.DbQueries.clearData;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Dialog signInOutDialog;
    private Button signIntDialogButton;
    private Button signUpDialogButton;

    public static DrawerLayout drawer;
    public static NavigationView navigationView;

    private static FirebaseUser currentUser;
    private TextView bedgeCount;
    public static Activity mainActivity;
    public static NavController navController;
    public static boolean resetMainActivity = false;

    private CircularImageView profileImageView;
    private TextView fullName, email;
    private ImageView addProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = MainActivity.this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_account, R.id.nav_wishlist, R.id.nav_order, R.id.nav_rewards, R.id.nav_account, R.id.nav_cart, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().getItem(0).setChecked(true);

        profileImageView = navigationView.getHeaderView(0).findViewById(R.id.main_profile_image);
        fullName = navigationView.getHeaderView(0).findViewById(R.id.main_full_name);
        email = navigationView.getHeaderView(0).findViewById(R.id.main_email);
        addProfileIcon = navigationView.getHeaderView(0).findViewById(R.id.add_profile_icon);

        signInOutDialog = new Dialog(MainActivity.this);
        signInOutDialog.setContentView(R.layout.sign_in_dialog);
        signInOutDialog.setCancelable(true);
        signInOutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        signIntDialogButton = signInOutDialog.findViewById(R.id.sign_In_dialog_btn);
        signUpDialogButton = signInOutDialog.findViewById(R.id.sign_up_dialog_btn);
        final Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);

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

                SigninFragment.disableCloseButton = true;
                SignupFragment.disableCloseButton = true;

                signInOutDialog.dismiss();
                setSignUpFregment = true;
                startActivity(registerIntent);
            }
        });

        navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseAuth.getInstance().signOut();
                clearData();
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            for (int i = 1; i < navigationView.getMenu().size() - 1; i++) {
                navigationView.getMenu().getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        drawer.closeDrawer(GravityCompat.START);
                        signInOutDialog.show();
                        return true;
                    }
                });
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {

            if (DbQueries.email == null) {
                FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DbQueries.fullName = task.getResult().getString("full name");
                            DbQueries.email = task.getResult().getString("email");
                            DbQueries.profile = task.getResult().getString("profile");

                            fullName.setText(DbQueries.fullName);
                            email.setText(DbQueries.email);
                            if (DbQueries.profile.equals("")) {
                                addProfileIcon.setVisibility(View.VISIBLE);
                            } else {
                                addProfileIcon.setVisibility(View.INVISIBLE);
                                Glide.with(MainActivity.this).load(DbQueries.profile).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into(profileImageView);
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                fullName.setText(DbQueries.fullName);
                email.setText(DbQueries.email);
                if (DbQueries.profile.equals("")) {
                    profileImageView.setImageResource(R.drawable.profile_place_holder);
                    addProfileIcon.setVisibility(View.VISIBLE);
                } else {
                    addProfileIcon.setVisibility(View.INVISIBLE);
                    Glide.with(MainActivity.this).load(DbQueries.profile).apply(new RequestOptions().placeholder(R.drawable.place_holder_icon)).into(profileImageView);
                }
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
        if (resetMainActivity) {
            resetMainActivity = false;
            navController.navigate(R.id.nav_home);
        }

        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DbQueries.checkNotifications(true, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem cartItem = menu.findItem(R.id.menu_cart);
        cartItem.setActionView(R.layout.badge_layout);
        ImageView bedgeIcon = cartItem.getActionView().findViewById(R.id.bedge_icon);
        bedgeIcon.setImageResource(R.drawable.cart_icon);
        bedgeCount = cartItem.getActionView().findViewById(R.id.bedge_count);

        if (currentUser != null) {
            if (DbQueries.cartList.size() == 0) {
                DbQueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this), false, bedgeCount, new TextView(MainActivity.this));
            } else {
                bedgeCount.setVisibility(View.VISIBLE);
                if (DbQueries.cartList.size() < 99) {
                    bedgeCount.setText(String.valueOf(DbQueries.cartList.size()));
                } else {
                    bedgeCount.setText("99");
                }
            }

            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInOutDialog.show();
                    } else {
                        Intent cartFragmentActivityIntent = new Intent(MainActivity.this, CartFragmentActivity.class);
                        startActivity(cartFragmentActivityIntent);
                    }
                }
            });

            MenuItem notifyItem = menu.findItem(R.id.menu_notification);
            notifyItem.setActionView(R.layout.badge_layout);
            ImageView notifyIcon = notifyItem.getActionView().findViewById(R.id.bedge_icon);
            notifyIcon.setImageResource(R.drawable.bell_icon);
            TextView notifyCount = notifyItem.getActionView().findViewById(R.id.bedge_count);

            DbQueries.checkNotifications(false,notifyCount);

            notifyItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent NotificationIntent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(NotificationIntent);
                }
            });

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_search && currentUser != null) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == R.id.menu_search && currentUser == null) {
            signInOutDialog.show();
            return false;
        } else if (id == R.id.menu_notification && currentUser != null) {

            Intent NotificationIntent = new Intent(this, NotificationActivity.class);
            startActivity(NotificationIntent);

            return true;
        } else if (id == R.id.menu_notification && currentUser == null) {
            signInOutDialog.show();
            return false;
        } else if (id == R.id.menu_cart && currentUser != null) {
            Intent cartFragmentActivityIntent = new Intent(MainActivity.this, CartFragmentActivity.class);
            startActivity(cartFragmentActivityIntent);
            return true;
        } else if (id == R.id.menu_cart && currentUser == null) {
            signInOutDialog.show();
            return false;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static void showNavigationItems(boolean show) {

        if (currentUser == null) {
            for (int i = 1; i < navigationView.getMenu().size() - 1; i++) {
                navigationView.getMenu().getItem(i).setEnabled(show);
            }
        } else {
            for (int i = 1; i < navigationView.getMenu().size(); i++) {
                navigationView.getMenu().getItem(i).setEnabled(show);
            }
        }
    }
}
