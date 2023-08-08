package fr.vegeto52.go4lunch.data.api;

import fr.vegeto52.go4lunch.model.RestaurantDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vegeto52-PC on 03/08/2023.
 */
public interface PlaceDetailsApi {

    @GET("details/json?")
    Call<RestaurantDetails> getDetails(
            @Query("place_id") String placeId,
            @Query("fields") String fields,
            @Query("key") String key
    );
}
