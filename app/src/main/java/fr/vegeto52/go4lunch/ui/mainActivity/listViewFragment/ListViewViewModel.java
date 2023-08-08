package fr.vegeto52.go4lunch.ui.mainActivity.listViewFragment;

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
 * Created by Vegeto52-PC on 01/08/2023.
 */
public class ListViewViewModel extends ViewModel {

    private final MediatorLiveData<ListViewViewState> mMediatorLiveData = new MediatorLiveData<>();

    public ListViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository, FirestoreRepository firestoreRepository){
        LiveData<Location> location =locationRepository.getLocationLiveData();
        LiveData<List<Restaurant.Results>> restaurants = nearbySearchRepository.getListRestaurantLiveData();
        LiveData<List<User>> listUsers = firestoreRepository.getListUsersMutableLiveData();

        mMediatorLiveData.addSource(location, location1 -> combine(location1, restaurants.getValue(), listUsers.getValue()));
        mMediatorLiveData.addSource(restaurants, restaurants1 -> combine(location.getValue(), restaurants1, listUsers.getValue()));
        mMediatorLiveData.addSource(listUsers, listUsers1 -> combine(location.getValue(), restaurants.getValue(), listUsers1));
    }

    private void combine(Location location, List<Restaurant.Results> listRestaurants, List<User> listUsers){
        if (location != null && listRestaurants != null && listUsers != null){
            mMediatorLiveData.setValue(new ListViewViewState(location, listRestaurants, listUsers));
        }
    }

    public LiveData<ListViewViewState> getListViewLiveData(){
        return mMediatorLiveData;
    }
}
