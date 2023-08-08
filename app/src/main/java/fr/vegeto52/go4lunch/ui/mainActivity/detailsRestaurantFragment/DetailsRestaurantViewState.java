package fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment;

import java.util.List;

import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 04/08/2023.
 */
public class DetailsRestaurantViewState {

    private final List<User> mUserList;
    private final User mCurrentUser;
    private final List<Restaurant.Results> mRestaurantList;

    public DetailsRestaurantViewState(List<User> userList, User currentUser, List<Restaurant.Results> restaurantList) {
        mUserList = userList;
        mCurrentUser = currentUser;
        mRestaurantList = restaurantList;
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public List<Restaurant.Results> getRestaurantList() {
        return mRestaurantList;
    }

}
