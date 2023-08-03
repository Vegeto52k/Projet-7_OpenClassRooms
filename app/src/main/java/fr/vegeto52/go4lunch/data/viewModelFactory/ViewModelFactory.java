package fr.vegeto52.go4lunch.data.viewModelFactory;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.data.repository.LocationRepository;
import fr.vegeto52.go4lunch.data.repository.NearbySearchRepository;
import fr.vegeto52.go4lunch.ui.listViewFragment.ListViewViewModel;
import fr.vegeto52.go4lunch.ui.mainActivity.MainActivityViewModel;
import fr.vegeto52.go4lunch.ui.mapViewFragment.MapViewViewModel;

/**
 * Created by Vegeto52-PC on 23/07/2023.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final FirestoreRepository mFirestoreRepository;
    private final LocationRepository mLocationRepository;
    private final NearbySearchRepository mNearbySearchRepository;

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
        mLocationRepository.getLocationLiveData().observeForever(new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mNearbySearchRepository.getNearbySearch(location);
            }
        });
    }

    @NonNull
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
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
