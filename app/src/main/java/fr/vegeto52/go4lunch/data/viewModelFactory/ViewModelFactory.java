package fr.vegeto52.go4lunch.data.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.ui.mainActivity.MainActivityViewModel;

/**
 * Created by Vegeto52-PC on 23/07/2023.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final FirestoreRepository mFirestoreRepository;

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
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)){
            return (T) new MainActivityViewModel(mFirestoreRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
