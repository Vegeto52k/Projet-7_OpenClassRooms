package fr.vegeto52.go4lunch.ui.mainActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 23/07/2023.
 */
public class MainActivityViewModel extends ViewModel {

    private final MediatorLiveData<MainActivityViewState> mMediatorLiveData = new MediatorLiveData<>();


    public MainActivityViewModel(FirestoreRepository firestoreRepository) {

        LiveData<User> currentUser = firestoreRepository.getCurrentUserMutableLiveData();

        mMediatorLiveData.addSource(currentUser, this::combine);
    }

    private void combine(User currentUser) {
        if (currentUser != null) {
            mMediatorLiveData.setValue(new MainActivityViewState(currentUser));
        }
    }

    public LiveData<MainActivityViewState> getCurrentUserLiveData (){
        return mMediatorLiveData;
    }
}
