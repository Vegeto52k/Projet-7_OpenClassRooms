package fr.vegeto52.go4lunch.ui.mapViewFragment;

import android.location.Location;

import java.util.List;

import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 25/07/2023.
 */
public class MapViewViewState {

    private final Location mLocation;
    private final List<Restaurant.Results> mListRestaurants;
    private final List<User> mListUsers;

    public MapViewViewState(Location location, List<Restaurant.Results> listRestaurants, List<User> listUsers) {
        mLocation = location;
        mListRestaurants = listRestaurants;
        mListUsers = listUsers;
    }

    public Location getLocation() {
        return mLocation;
    }

    public List<Restaurant.Results> getListRestaurants() {
        return mListRestaurants;
    }

    public List<User> getListUsers() {
        return mListUsers;
    }
}
