package fr.vegeto52.go4lunch.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import fr.vegeto52.go4lunch.data.MainApplication;

/**
 * Created by Vegeto52-PC on 25/07/2023.
 */
public class LocationRepository {

    private final MutableLiveData<Location> mLocationMutableLiveData = new MutableLiveData<>();
    FusedLocationProviderClient mFusedLocationProviderClient;

    public LocationRepository() {
        getLocation();
    }

    // Get Location
    @SuppressLint("MissingPermission")
    private void getLocation(){
        Context context = MainApplication.getApplication();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(mLocationMutableLiveData::setValue);
    }

    // LiveData for Location
    public LiveData<Location> getLocationLiveData(){
        return mLocationMutableLiveData;
    }

}
