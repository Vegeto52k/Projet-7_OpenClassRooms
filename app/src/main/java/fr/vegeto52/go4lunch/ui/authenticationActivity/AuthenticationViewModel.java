package fr.vegeto52.go4lunch.ui.authenticationActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;

/**
 * Created by Vegeto52-PC on 01/09/2023.
 */
public class AuthenticationViewModel extends ViewModel {

    FirestoreRepository mFirestoreRepository;
    private final MutableLiveData<String> mCheckUserMutableLiveData = new MutableLiveData<>();

    public AuthenticationViewModel() {
        mFirestoreRepository = new FirestoreRepository("dummyParameter");
    }

    public void createOrUpdateUser(AuthCredential credential){
        mFirestoreRepository.createOrUpdateUser(credential);
        mFirestoreRepository.getCheckUserAuthenticateMutableLiveData().observeForever(mCheckUserMutableLiveData::setValue);
    }

    public LiveData<String> getCheckUserAuthenticateLiveData(){
        return mCheckUserMutableLiveData;
    }
}
