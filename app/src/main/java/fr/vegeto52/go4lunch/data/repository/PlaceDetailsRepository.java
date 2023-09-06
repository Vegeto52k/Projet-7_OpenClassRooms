package fr.vegeto52.go4lunch.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fr.vegeto52.go4lunch.BuildConfig;
import fr.vegeto52.go4lunch.data.api.PlaceDetailsApi;
import fr.vegeto52.go4lunch.data.api.RetrofitService;
import fr.vegeto52.go4lunch.model.RestaurantDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vegeto52-PC on 03/08/2023.
 */
public class PlaceDetailsRepository {

    String mKey = BuildConfig.MAPS_API_KEY;
    String mFields = "website,formatted_phone_number";

    private final MutableLiveData<RestaurantDetails.Result> mRestaurantDetailsMutableLiveData = new MutableLiveData<>();

    public PlaceDetailsRepository() {
    }

    // Get Restaurant Details
    public void getRestaurantDetails(String placeId) {
        PlaceDetailsApi placeDetailsApi = RetrofitService.getRetrofitInstance().create(PlaceDetailsApi.class);
        placeDetailsApi.getDetails(placeId, mFields, mKey).enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantDetails> call, @NonNull Response<RestaurantDetails> response) {
                assert response.body() != null;
                mRestaurantDetailsMutableLiveData.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantDetails> call, @NonNull Throwable t) {
                mRestaurantDetailsMutableLiveData.setValue(null);
            }
        });
    }

    public LiveData<RestaurantDetails.Result> getRestaurantDetailsLiveData() {
        return mRestaurantDetailsMutableLiveData;
    }
}
