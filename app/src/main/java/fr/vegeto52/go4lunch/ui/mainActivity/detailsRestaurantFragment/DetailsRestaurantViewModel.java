package fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.data.repository.NearbySearchRepository;
import fr.vegeto52.go4lunch.data.repository.PlaceDetailsRepository;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.RestaurantDetails;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 04/08/2023.
 */
public class DetailsRestaurantViewModel extends ViewModel {

    private final MediatorLiveData<DetailsRestaurantViewState> mMediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<RestaurantDetails.Result> mRestaurantDetailsMutableLiveData = new MutableLiveData<>();
    FirestoreRepository mFirestoreRepository;
    PlaceDetailsRepository mPlaceDetailsRepository;

    public DetailsRestaurantViewModel(FirestoreRepository firestoreRepository, NearbySearchRepository nearbySearchRepository, PlaceDetailsRepository placeDetailsRepository) {

        mFirestoreRepository = firestoreRepository;
        mPlaceDetailsRepository = placeDetailsRepository;

        LiveData<List<User>> listUsers = firestoreRepository.getListUsersMutableLiveData();
        LiveData<User> currentUser = firestoreRepository.getCurrentUserMutableLiveData();
        LiveData<List<Restaurant.Results>> listRestaurants = nearbySearchRepository.getListRestaurantLiveData();

        mMediatorLiveData.addSource(listUsers, listUsers1 -> combine(listUsers1, currentUser.getValue(), listRestaurants.getValue()));
        mMediatorLiveData.addSource(currentUser, currentUser1 -> combine(listUsers.getValue(), currentUser1, listRestaurants.getValue()));
        mMediatorLiveData.addSource(listRestaurants, listRestaurants1 -> combine(listUsers.getValue(), currentUser.getValue(), listRestaurants1));
    }

    private void combine(List<User> userList, User currentUser, List<Restaurant.Results> restaurantList) {
        if (userList != null && currentUser != null && restaurantList != null) {
            mMediatorLiveData.setValue(new DetailsRestaurantViewState(userList, currentUser, restaurantList));
        }
    }

    public void getPlaceDetails(String placeId) {
        mPlaceDetailsRepository.getRestaurantDetails(placeId);
        mPlaceDetailsRepository.getRestaurantDetailsLiveData().observeForever(mRestaurantDetailsMutableLiveData::setValue);
    }

    public void setSelectedRestaurant(String placeId) {
        mFirestoreRepository.setSelectedRestaurant(placeId);
    }

    public void setFavoritesRestaurant(List<String> favoritesResto) {
        mFirestoreRepository.setFavoritesRestaurants(favoritesResto);
    }

    public LiveData<DetailsRestaurantViewState> getViewModelDetailsRestaurantLiveData() {
        return mMediatorLiveData;
    }

    public LiveData<RestaurantDetails.Result> getDetailsRestaurantLiveData() {
        return mRestaurantDetailsMutableLiveData;
    }
}
