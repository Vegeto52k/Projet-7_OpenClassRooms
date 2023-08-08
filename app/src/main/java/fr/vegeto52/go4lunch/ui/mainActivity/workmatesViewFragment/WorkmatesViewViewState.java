package fr.vegeto52.go4lunch.ui.mainActivity.workmatesViewFragment;

import java.util.List;

import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 03/08/2023.
 */
public class WorkmatesViewViewState {

    private final List<User> mUserList;
    private final List<Restaurant.Results> mRestaurantsList;

    public WorkmatesViewViewState(List<User> userList, List<Restaurant.Results> restaurantsList) {
        mUserList = userList;
        mRestaurantsList = restaurantsList;
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public List<Restaurant.Results> getRestaurantsList() {
        return mRestaurantsList;
    }
}
