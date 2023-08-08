package fr.vegeto52.go4lunch.data.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.data.repository.LocationRepository;
import fr.vegeto52.go4lunch.data.repository.NearbySearchRepository;
import fr.vegeto52.go4lunch.data.repository.PlaceDetailsRepository;
import fr.vegeto52.go4lunch.ui.mainActivity.MainActivityViewModel;
import fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment.DetailsRestaurantViewModel;
import fr.vegeto52.go4lunch.ui.mainActivity.listViewFragment.ListViewViewModel;
import fr.vegeto52.go4lunch.ui.mainActivity.mapViewFragment.MapViewViewModel;
import fr.vegeto52.go4lunch.ui.mainActivity.workmatesViewFragment.WorkmatesViewViewModel;

/**
 * Created by Vegeto52-PC on 23/07/2023.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final FirestoreRepository mFirestoreRepository;
    private final LocationRepository mLocationRepository;
    private final NearbySearchRepository mNearbySearchRepository;
    private final PlaceDetailsRepository mPlaceDetailsRepository;

    private static volatile ViewModelFactory sFactory;


    public static ViewModelFactory getInstance(){
        if (sFactory == null){
            synchronized (ViewModelFactory.class){
                if (sFactory == null){
                    sFactory = new ViewModelFactory();
                }
            }
        }
        return sFactory;
    }

    private ViewModelFactory(){
        this.mFirestoreRepository = new FirestoreRepository();
        this.mLocationRepository = new LocationRepository();
        this.mNearbySearchRepository = new NearbySearchRepository();
        this.mPlaceDetailsRepository = new PlaceDetailsRepository();
        mLocationRepository.getLocationLiveData().observeForever(mNearbySearchRepository::getNearbySearch);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)){
            return (T) new MainActivityViewModel(mFirestoreRepository);
        }
        if (modelClass.isAssignableFrom(MapViewViewModel.class)){
            return (T) new MapViewViewModel(mLocationRepository, mNearbySearchRepository, mFirestoreRepository);
        }
        if (modelClass.isAssignableFrom(ListViewViewModel.class)){
            return (T) new ListViewViewModel(mLocationRepository, mNearbySearchRepository, mFirestoreRepository);
        }
        if (modelClass.isAssignableFrom(WorkmatesViewViewModel.class)){
            return (T) new WorkmatesViewViewModel(mFirestoreRepository, mNearbySearchRepository);
        }
        if (modelClass.isAssignableFrom(DetailsRestaurantViewModel.class)){
            return (T) new DetailsRestaurantViewModel(mFirestoreRepository, mNearbySearchRepository, mPlaceDetailsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
