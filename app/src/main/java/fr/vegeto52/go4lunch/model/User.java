package fr.vegeto52.go4lunch.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Vegeto52-PC on 21/07/2023.
 */
public class User {

    private String mUid;
    private String mUserName;
    @Nullable
    private String mUrlPhoto;
    @Nullable
    private String mSelectedResto;
    private String mAdressMail;
    private List<String> mFAVORITE_RESTO_LIST = new ArrayList<>();
    private boolean mNotifications;


    public User() {
    }

    //Constructor
    public User(String uid, String userName, @Nullable String urlPhoto, @Nullable String selectedResto, String adressMail, List<String> FAVORITE_RESTO_LIST, boolean notifications) {
        mUid = uid;
        mUserName = userName;
        mUrlPhoto = urlPhoto;
        mSelectedResto = selectedResto;
        mAdressMail = adressMail;
        mFAVORITE_RESTO_LIST = FAVORITE_RESTO_LIST;
        mNotifications = notifications;
    }

    //Getters
    public String getUid() {
        return mUid;
    }

    public String getUserName() {
        return mUserName;
    }

    @Nullable
    public String getUrlPhoto() {
        return mUrlPhoto;
    }

    @Nullable
    public String getSelectedResto() {
        return mSelectedResto;
    }

    public String getAdressMail() {
        return mAdressMail;
    }

    public List<String> getFavoritesResto() {
        return mFAVORITE_RESTO_LIST;
    }

    //Setters
    public void setUid(String uid) {
        mUid = uid;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setUrlPhoto(@Nullable String urlPhoto) {
        mUrlPhoto = urlPhoto;
    }

    public void setSelectedResto(@Nullable String selectedResto) {
        mSelectedResto = selectedResto;
    }

    public void setAdressMail(String adressMail) {
        mAdressMail = adressMail;
    }

    public void setFavoritesResto(List<String> favoritesResto) {
        mFAVORITE_RESTO_LIST = favoritesResto;
    }

    public boolean isNotifications() {
        return mNotifications;
    }

    public void setNotifications(boolean notifications) {
        mNotifications = notifications;
    }
}
