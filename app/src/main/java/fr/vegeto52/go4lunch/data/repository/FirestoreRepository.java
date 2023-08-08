package fr.vegeto52.go4lunch.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 23/07/2023.
 */
public class FirestoreRepository {

    private static final String COLLECTION_NAME = "users";
    private final MutableLiveData<List<User>> mListUsersMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> mCurrentUserMutableLiveData = new MutableLiveData<>();
    List<User> mUserList = new ArrayList<>();
    String mUidCurrentUser;

    public FirestoreRepository() {
        getListUsersAndCurrentUser();
    }

    // For get Current User UID
    private String getUidCurrentUser(){
        return mUidCurrentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    // For get List Users in Firestore
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Get List Users and Current User
    public void getListUsersAndCurrentUser(){
        getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots){
                    User user = documentSnapshot.toObject(User.class);
                    String uid = documentSnapshot.getId();
                    assert user != null;
                    user.setUid(uid);
                    mUserList.add(user);
                    if (getUidCurrentUser().equals(user.getUid())){
                        mCurrentUserMutableLiveData.setValue(user);
                    }
                }
                mListUsersMutableLiveData.setValue(mUserList);
            }
        });
    }

    // Update User Selected Restaurant
    public void setSelectedRestaurant(String placeId){
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        getUsersCollection().document(currentUserId).update("selectedResto", placeId);
    }

    // Update User Favorites Restaurants
    public void setFavoritesRestaurants(List<String> favoritesRestaurants){
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        getUsersCollection().document(currentUserId).update("FAVORITE_RESTO_LIST", favoritesRestaurants);
    }

    // LiveData for List Users
    public LiveData<List<User>> getListUsersMutableLiveData(){
        return mListUsersMutableLiveData;
    }

    //LiveData for CurrentUser
    public LiveData<User> getCurrentUserMutableLiveData(){
        return mCurrentUserMutableLiveData;
    }
}
