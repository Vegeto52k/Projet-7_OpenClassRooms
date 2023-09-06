package fr.vegeto52.go4lunch.data.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 23/07/2023.
 */
public class FirestoreRepository {

    private static final String COLLECTION_NAME = "users";
    private final MutableLiveData<List<User>> mListUsersMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> mCurrentUserMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mCheckUser = new MutableLiveData<>();
    List<User> mUserList = new ArrayList<>();
    String mUidCurrentUser;

    public FirestoreRepository() {
        getListUsersAndCurrentUser();
    }

    @SuppressWarnings("unused")
    public FirestoreRepository(String dummyParameter){
    }

    // For get Current User UID
    private String getUidCurrentUser() {
        return mUidCurrentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    // For get List Users in Firestore
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Get List Users and Current User
    public void getListUsersAndCurrentUser() {
        getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    String uid = documentSnapshot.getId();
                    assert user != null;
                    user.setUid(uid);
                    mUserList.add(user);
                    if (getUidCurrentUser().equals(user.getUid())) {
                        mCurrentUserMutableLiveData.setValue(user);
                    }
                }
                mListUsersMutableLiveData.setValue(mUserList);
            }
        });
    }

    // Update User Selected Restaurant
    public void setSelectedRestaurant(String placeId) {
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        getUsersCollection().document(currentUserId).update("selectedResto", placeId);
    }

    // Update User Favorites Restaurants
    public void setFavoritesRestaurants(List<String> favoritesRestaurants) {
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        getUsersCollection().document(currentUserId).update("favoritesResto", favoritesRestaurants);
    }

    // Update Notification activation
    public void setNotifications(boolean notifications) {
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        getUsersCollection().document(currentUserId).update("notifications", notifications);
    }

    // Authentication User
    public void createOrUpdateUser(AuthCredential credential){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();
                assert user != null;
                String uid = user.getUid();
                String name = user.getDisplayName();
                Uri photoUri = user.getPhotoUrl();
                String adressMail = user.getEmail();

                getUsersCollection().document(uid).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> userDoc = new HashMap<>();
                        userDoc.put("userName", name);
                        userDoc.put("urlPhoto", photoUri);
                        userDoc.put("adressMail", adressMail);
                        userDoc.put("selectedResto", documentSnapshot.get("selectedResto"));
                        userDoc.put("favoritesResto", documentSnapshot.get("favoritesResto"));
                        userDoc.put("notifications", documentSnapshot.get("notifications"));
                        getUsersCollection().document(uid).set(userDoc).addOnSuccessListener(unused -> {
                            String checkUser;
                            checkUser = "Success";
                            mCheckUser.setValue(checkUser);
                        }).addOnFailureListener(e -> {
                            String checkUser;
                            checkUser = "Failure";
                            mCheckUser.setValue(checkUser);
                        });
                    } else {
                        Map<String, Object> userDoc = new HashMap<>();
                        userDoc.put("userName", name);
                        userDoc.put("urlPhoto", photoUri);
                        userDoc.put("adressMail", adressMail);
                        userDoc.put("selectedResto", "");
                        userDoc.put("favoritesResto", new ArrayList<String>());
                        userDoc.put("notifications", true);
                        getUsersCollection().document(uid).set(userDoc).addOnSuccessListener(unused -> {
                            String checkUser;
                            checkUser = "Success";
                            mCheckUser.setValue(checkUser);
                        }).addOnFailureListener(e -> {
                            String checkUser;
                            checkUser = "Failure";
                            mCheckUser.setValue(checkUser);
                        });
                    }
                });
            }
        });
    }

    // LiveData for List Users
    public LiveData<List<User>> getListUsersMutableLiveData() {
        return mListUsersMutableLiveData;
    }

    //LiveData for CurrentUser
    public LiveData<User> getCurrentUserMutableLiveData() {
        return mCurrentUserMutableLiveData;
    }

    // LiveData for Check User
    public LiveData<String> getCheckUserAuthenticateMutableLiveData(){
        return mCheckUser;
    }

}
