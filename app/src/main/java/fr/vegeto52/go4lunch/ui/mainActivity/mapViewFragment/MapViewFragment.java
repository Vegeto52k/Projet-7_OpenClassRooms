package fr.vegeto52.go4lunch.ui.mainActivity.mapViewFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.data.viewModelFactory.ViewModelFactory;
import fr.vegeto52.go4lunch.databinding.FragmentMapViewBinding;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;
import fr.vegeto52.go4lunch.ui.mainActivity.MainActivity;
import fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment.DetailsRestaurantFragment;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mMap;
    private ImageButton mButtonCenterMap;
    private LatLng mUserLocation;
    private Location mLocation;
    private List<Restaurant.Results> mListRestaurants;
    private List<User> mListUsers;
    private String mPlaceId;
    private final List<Marker> mMarkerList = new ArrayList<>();
    private BottomNavigationView mBottomNavigationView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMapViewBinding binding = FragmentMapViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mMapView = view.findViewById(R.id.MVF_mapview);
        mButtonCenterMap = view.findViewById(R.id.MVF_button_center_map);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return view;
    }

    // Initialize ViewModel
    private void initViewModel() {
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        MapViewViewModel mapViewViewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewViewModel.class);
        mapViewViewModel.getMapViewLiveData().observe(getViewLifecycleOwner(), mapViewViewState -> {
            mLocation = mapViewViewState.getLocation();
            mListRestaurants = mapViewViewState.getListRestaurants();
            mListUsers = mapViewViewState.getListUsers();
            addUserMarker();
            centerCameraPosition();
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        initViewModel();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            mBottomNavigationView = activity.getBottomNavigationView();
        }
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        if (isAdded() && isVisible()) {
            if (mBottomNavigationView != null) {
                mBottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // Button for center map
    private void centerCameraPosition() {
        mButtonCenterMap.setOnClickListener(view -> mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserLocation)));
    }

    // Add User Marker
    private void addUserMarker() {
        double userLatitude = mLocation.getLatitude();
        double userLongitude = mLocation.getLongitude();
        mUserLocation = new LatLng(userLatitude, userLongitude);
        Marker userMarker = mMap.addMarker(new MarkerOptions().position(mUserLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        assert userMarker != null;
        userMarker.setTag(null);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserLocation));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mUserLocation, 17);
        mMap.animateCamera(cameraUpdate);
        markersToResto();
    }

    // Add Marker Restaurants
    private void markersToResto() {
        List<String> selectedRestos = new ArrayList<>();
        for (User user : mListUsers) {
            String selectedResto = user.getSelectedResto();
            if (selectedResto != null) {
                selectedRestos.add(selectedResto);
            }
        }
        for (Restaurant.Results restaurant : mListRestaurants) {
            mPlaceId = restaurant.getPlace_id();
            LatLng restaurantLocation = new LatLng(restaurant.getGeometry().getLocation().getLat(), restaurant.getGeometry().getLocation().getLng());
            MarkerOptions markerOptions;
            if (selectedRestos.contains(mPlaceId)) {
                markerOptions = new MarkerOptions()
                        .position(restaurantLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                markerOptions = new MarkerOptions()
                        .position(restaurantLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            Marker marker = mMap.addMarker(markerOptions);
            assert marker != null;
            marker.setTag(mPlaceId);
            mMarkerList.add(marker);
        }
        mMap.setOnMarkerClickListener(marker -> {
            if (marker.getTag() != null ){
                mPlaceId = Objects.requireNonNull(marker.getTag()).toString();
                Fragment fragment = new DetailsRestaurantFragment();
                Bundle args = new Bundle();
                args.putString("placeId", mPlaceId);
                fragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.MA_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            return false;
        });
    }


    // Search menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.MS_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() >= 3) {
                    for (Marker marker : mMarkerList) {
                        marker.remove();
                    }
                    mMarkerList.clear();
                    performSearch(s);
                } else {
                    markersToResto();
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Perform Search
    private void performSearch(String text) {
        List<String> selectedRestos = new ArrayList<>();
        for (User user : mListUsers) {
            String selectedResto = user.getSelectedResto();
            if (selectedResto != null) {
                selectedRestos.add(selectedResto);
            }
        }
        for (Restaurant.Results restaurant : mListRestaurants) {
            if (restaurant.getName().toLowerCase().contains(text.toLowerCase())) {
                mPlaceId = restaurant.getPlace_id();
                LatLng restaurantLocation = new LatLng(restaurant.getGeometry().getLocation().getLat(), restaurant.getGeometry().getLocation().getLng());
                MarkerOptions markerOptions;
                if (selectedRestos.contains(mPlaceId)) {
                    markerOptions = new MarkerOptions()
                            .position(restaurantLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    markerOptions = new MarkerOptions()
                            .position(restaurantLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                Marker marker = mMap.addMarker(markerOptions);
                assert marker != null;
                marker.setTag(mPlaceId);
                mMarkerList.add(marker);
            }
            mMap.setOnMarkerClickListener(marker -> {
                if (marker.getTag() != null ){
                    mPlaceId = Objects.requireNonNull(marker.getTag()).toString();
                    Fragment fragment = new DetailsRestaurantFragment();
                    Bundle args = new Bundle();
                    args.putString("placeId", mPlaceId);
                    fragment.setArguments(args);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.MA_fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return false;
            });
        }
    }
}