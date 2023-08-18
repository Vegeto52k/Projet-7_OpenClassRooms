package fr.vegeto52.go4lunch.ui.mainActivity.settingsFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 17/08/2023.
 */
public class SettingsFragmentViewModel extends ViewModel{
    private final MediatorLiveData<SettingsFragmentViewState> mMediatorLiveData = new MediatorLiveData<>();
    FirestoreRepository mFirestoreRepository;


    public SettingsFragmentViewModel(FirestoreRepository firestoreRepository) {

        mFirestoreRepository = firestoreRepository;

        LiveData<User> currentUser = firestoreRepository.getCurrentUserMutableLiveData();

        mMediatorLiveData.addSource(currentUser, this::combine);
    }

    private void combine(User currentUser) {
        if (currentUser != null) {
            mMediatorLiveData.setValue(new SettingsFragmentViewState(currentUser));
        }
    }

    public void setNotifications(boolean notifications){
        mFirestoreRepository.setNotifications(notifications);
    }

    public LiveData<SettingsFragmentViewState> getCurrentUserLiveData (){
        return mMediatorLiveData;
    }
}
