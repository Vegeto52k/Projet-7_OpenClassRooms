package fr.vegeto52.go4lunch.data.repository;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.vegeto52.go4lunch.data.api.NearbySearchApi;
import fr.vegeto52.go4lunch.data.api.RetrofitService;
import fr.vegeto52.go4lunch.model.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vegeto52-PC on 25/07/2023.
 */
public class NearbySearchRepository {

    double mCurrentLatitude;
    double mCurrentLongitude;
    String mLatLng;
    int radius = 1500;
    String type = "restaurant";
    String map_key = "AIzaSyCuiuuSU0l2xgi-bWz_gYHM1EkZJeuWmx4";
    private final MutableLiveData<List<Restaurant.Results>> mListRestaurantMutableLiveData = new MutableLiveData<>();


    public NearbySearchRepository(){

    }

    // Get List Restaurants
    public void getNearbySearch(Location location){
        NearbySearchApi nearbySearchApi = RetrofitService.getRetrofitInstance().create(NearbySearchApi.class);
        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();
        mLatLng = "" + mCurrentLatitude + "," + mCurrentLongitude;

        nearbySearchApi.getObjectRestaurant(mLatLng, radius, type, map_key).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(@NonNull Call<Restaurant> call, @NonNull Response<Restaurant> response) {
                assert response.body() != null;
                mListRestaurantMutableLiveData.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(@NonNull Call<Restaurant> call, @NonNull Throwable t) {
                mListRestaurantMutableLiveData.setValue(null);
            }
        });
    }

    // LiveData for List Restaurants
    public LiveData<List<Restaurant.Results>> getListRestaurantLiveData(){
        return mListRestaurantMutableLiveData;
    }
}
