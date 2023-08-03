package fr.vegeto52.go4lunch.ui.listViewFragment;

import android.location.Location;

import java.util.List;

import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 01/08/2023.
 */
public class ListViewViewState {

    private final Location mLocation;
    private final List<Restaurant.Results> mRestaurantList;
    private final List<User> mUserList;

    public ListViewViewState(Location location, List<Restaurant.Results> restaurantList, List<User> userList) {
        mLocation = location;
        mRestaurantList = restaurantList;
        mUserList = userList;
    }

    public Location getLocation() {
        return mLocation;
    }

    public List<Restaurant.Results> getRestaurantList() {
        return mRestaurantList;
    }

    public List<User> getUserList() {
        return mUserList;
    }
}
