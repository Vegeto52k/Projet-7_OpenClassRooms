package fr.vegeto52.go4lunch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * Created by Vegeto52-PC on 21/07/2023.
 */
@SuppressWarnings("unused")
public class User {

    private String mUid;
    private String mUserName;
    @Nullable
    private String mUrlPhoto;
    @Nullable
    private String mSelectedResto;
    private String mAdressMail;
    private List<String> mfavoritesResto = new ArrayList<>();
    private boolean mNotifications;


    public User() {
    }

    //Constructor
    public User(String uid, String userName, @Nullable String urlPhoto, @Nullable String selectedResto, String adressMail, List<String> favoritesResto, boolean notifications) {
        mUid = uid;
        mUserName = userName;
        mUrlPhoto = urlPhoto;
        mSelectedResto = selectedResto;
        mAdressMail = adressMail;
        mfavoritesResto = favoritesResto;
        mNotifications = notifications;
    }

    @Override
    public boolean equals(@androidx.annotation.Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User that = (User) obj;
        return Objects.equals(mUid, that.mUid) &&
                Objects.equals(mUserName, that.mUserName) &&
                Objects.equals(mUrlPhoto, that.mUrlPhoto) &&
                Objects.equals(mSelectedResto, that.mSelectedResto) &&
                Objects.equals(mAdressMail, that.mAdressMail) &&
                Objects.equals(mfavoritesResto, that.mfavoritesResto) &&
                Objects.equals(mNotifications, that.mNotifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUid, mUserName, mUrlPhoto, mSelectedResto, mAdressMail, mfavoritesResto, mNotifications);
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
        return mfavoritesResto;
    }

    public boolean isNotifications() {
        return mNotifications;
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
        mfavoritesResto = favoritesResto;
    }

    public void setNotifications(boolean notifications) {
        mNotifications = notifications;
    }
}
