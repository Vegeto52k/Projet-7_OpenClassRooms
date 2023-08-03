package fr.vegeto52.go4lunch.ui.workmatesViewFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.data.repository.NearbySearchRepository;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 03/08/2023.
 */
public class WorkmatesViewViewModel extends ViewModel {

    private final MediatorLiveData<WorkmatesViewViewState> mMediatorLiveData = new MediatorLiveData<>();

    public WorkmatesViewViewModel(FirestoreRepository firestoreRepository, NearbySearchRepository nearbySearchRepository){
        LiveData<List<User>> listUsers = firestoreRepository.getListUsersMutableLiveData();
        LiveData<List<Restaurant.Results>> listRestaurants = nearbySearchRepository.getListRestaurantLiveData();

        mMediatorLiveData.addSource(listUsers, listUsers1 -> combine(listUsers1, listRestaurants.getValue()));
        mMediatorLiveData.addSource(listRestaurants, listRestaurants1 -> combine(listUsers.getValue(), listRestaurants1));
    }

    private void combine(List<User> userList, List<Restaurant.Results> restaurantList){
        if (userList != null && restaurantList != null){
            mMediatorLiveData.setValue(new WorkmatesViewViewState(userList, restaurantList));
        }
    }

    public LiveData<WorkmatesViewViewState> getWorkmatesViewLiveData(){
        return mMediatorLiveData;
    }
}
