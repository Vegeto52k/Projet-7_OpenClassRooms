package fr.vegeto52.go4lunch.ui.mainActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.data.viewModelFactory.ViewModelFactory;
import fr.vegeto52.go4lunch.databinding.ActivityMainBinding;
import fr.vegeto52.go4lunch.model.User;
import fr.vegeto52.go4lunch.ui.AuthenticationActivity;
import fr.vegeto52.go4lunch.ui.DetailsRestaurantFragment;
import fr.vegeto52.go4lunch.ui.ListViewFragment;
import fr.vegeto52.go4lunch.ui.mapViewFragment.MapViewFragment;
import fr.vegeto52.go4lunch.ui.SettingsFragment;
import fr.vegeto52.go4lunch.ui.WorkmatesViewFragment;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel mMainActivityViewModel;
    private User mCurrentUser = new User();
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavigationView;
    private ImageView mPhotoUserNav;
    private TextView mUsernameNav;
    private TextView mMailUserNav;
    private MapViewFragment mMapViewFragment = null;
    private ListViewFragment mListViewFragment = null;
    private WorkmatesViewFragment mWorkmatesViewFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapViewFragment = new MapViewFragment();
        mListViewFragment = new ListViewFragment();
        mWorkmatesViewFragment = new WorkmatesViewFragment();

        enableMyLocation();
    }

    // Request location permission
    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initUI();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if permission validated
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initUI();
            }
        }
    }

    // Initialize UI
    private void initUI(){
        fr.vegeto52.go4lunch.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViewModel();

        mDrawerLayout = findViewById(R.id.MA_activity_main_drawer_layout);
        mToolbar = findViewById(R.id.MA_activity_main_toolbar);
        mNavigationView = findViewById(R.id.MA_navigation_drawer);
        mBottomNavigationView = findViewById(R.id.MA_bottom_navigation_view);

        View hearderView = mNavigationView.getHeaderView(0);

        mPhotoUserNav = hearderView.findViewById(R.id.MA_photo_user_nav_header);
        mUsernameNav = hearderView.findViewById(R.id.MA_username_nav_header);
        mMailUserNav = hearderView.findViewById(R.id.MA_mail_user_nav_header);
        ImageView backgroundNav = hearderView.findViewById(R.id.MA_background_nav_header);

        Glide.with(this)
                .load("https://media.istockphoto.com/id/1018141890/fr/photo/deux-verres-%C3%A0-vin-vides-assis-dans-un-restaurant-par-un-chaud-apr%C3%A8s-midi-ensoleill%C3%A9.jpg?s=612x612&w=0&k=20&c=gajFyzYO1pxyLkm-P7l9yrj2vm_x1JqC3NMDXiRl46A=")
                .transform(new ColorFilterTransformation(Color.argb(100, 0, 0, 0)), new BlurTransformation(25))
                .into(backgroundNav);

        initToolbar();
        badgeSelected();
        itemNavSelected();

        Fragment fragment = new MapViewFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.MA_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Initialize ViewModel
    private void initViewModel(){
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        mMainActivityViewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);
        mMainActivityViewModel.getCurrentUserLiveData().observe(this, mainActivityViewState -> {
            mCurrentUser = mainActivityViewState.getCurrentUser();
            initInfoUser();
        });
    }

    // Initialize Toolbar
    private void initToolbar(){
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.MA_navigation_drawer_open, R.string.MA_navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Initialize Info user to NavigationView
    private void initInfoUser(){
        mMailUserNav.setText(mCurrentUser.getAdressMail());
        mUsernameNav.setText(mCurrentUser.getUserName());
        Glide.with(MainActivity.this).load(mCurrentUser.getUrlPhoto()).into(mPhotoUserNav);
    }

    // NavigationView Item Selected
    private void itemNavSelected(){
        mNavigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.MA_item_nav_your_lunch){
                itemNavYourLunchSelected();
            } else if (id == R.id.MA_item_nav_settings){
                itemNavSettingsSelected();
            } else if (id == R.id.MA_item_nav_logout){
                itemNavLogOutSelected();
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // NavigationView Item "Your Lunch" Selected
    private void itemNavYourLunchSelected(){
        if (mCurrentUser.getSelectedResto() != null){

            Fragment fragment = new DetailsRestaurantFragment();
            Bundle args = new Bundle();
            args.putString("placeId", mCurrentUser.getSelectedResto());
            fragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MA_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.MA_no_restaurant_selected), Toast.LENGTH_SHORT).show();
        }
    }

    // NavigationView Item "Settings" Selected
    private void itemNavSettingsSelected(){

        Fragment fragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.MA_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // NavigationView Item "LogOut" Selected
    private void itemNavLogOutSelected(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if(account != null){
            GoogleSignIn.getClient(MainActivity.this, gso).signOut();
        }
        Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }


    // BottomNavigationView Item Selected
    private void badgeSelected(){
        mBottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.MA_action_map_view){
                fragment = mMapViewFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.MA_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.MA_action_list_view) {
                fragment = mListViewFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.MA_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.MA_action_workmates) {
                fragment = mWorkmatesViewFragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.MA_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
            return true;
        });
    }
}