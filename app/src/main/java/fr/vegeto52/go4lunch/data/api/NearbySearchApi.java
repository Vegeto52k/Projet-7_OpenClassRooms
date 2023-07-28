package fr.vegeto52.go4lunch.data.api;

import fr.vegeto52.go4lunch.model.Restaurant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vegeto52-PC on 25/07/2023.
 */
public interface NearbySearchApi {

    @GET("nearbysearch/json?")
    Call<Restaurant> getObjectRestaurant(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String map_key
    );
}
