package fr.vegeto52.go4lunch.ui.mainActivity.mapViewFragment;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.data.repository.LocationRepository;
import fr.vegeto52.go4lunch.data.repository.NearbySearchRepository;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 25/07/2023.
 */
public class MapViewViewModel extends ViewModel {

    private final MediatorLiveData<MapViewViewState> mMediatorLiveData = new MediatorLiveData<>();


    public MapViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository, FirestoreRepository firestoreRepository) {

        LiveData<Location> location = locationRepository.getLocationLiveData();
        LiveData<List<Restaurant.Results>> listRestaurants = nearbySearchRepository.getListRestaurantLiveData();
        LiveData<List<User>> listUsers = firestoreRepository.getListUsersMutableLiveData();

        mMediatorLiveData.addSource(location, location1 -> combine(location1, listRestaurants.getValue(), listUsers.getValue()));
        mMediatorLiveData.addSource(listRestaurants, listRestaurants1 -> combine(location.getValue(), listRestaurants1, listUsers.getValue()));
        mMediatorLiveData.addSource(listUsers, listUsers1 -> combine(location.getValue(), listRestaurants.getValue(), listUsers1));


    }

    private void combine(Location location, List<Restaurant.Results> listRestaurants, List<User> listUsers) {
        if (location != null && listRestaurants != null && listUsers != null) {
            mMediatorLiveData.setValue(new MapViewViewState(location, listRestaurants, listUsers));
        }
    }

    public LiveData<MapViewViewState> getMapViewLiveData() {
        return mMediatorLiveData;
    }
}
