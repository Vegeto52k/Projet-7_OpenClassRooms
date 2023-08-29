package fr.vegeto52.go4lunch;

import androidx.lifecycle.LiveData;

/**
 * Created by Vegeto52-PC on 20/08/2023.
 */
//public class LiveDataTestUtils {

//    public static void observeForTesting(LiveData liveData, OnObservedListener block){
//        liveData.observeForever(ignored -> {});
//        block.onObserved(liveData.getValue());
//    }
//
//    public interface OnObservedListener{
//        void onObserved(T liveData);
//    }
//}
public class LiveDataTestUtils {
    public static <T> T getValueForTesting(final LiveData<T> liveData) {
        liveData.observeForever(ignored -> {
        });

        return liveData.getValue();
    }
}
