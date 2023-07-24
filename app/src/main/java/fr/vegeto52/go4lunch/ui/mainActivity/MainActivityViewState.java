package fr.vegeto52.go4lunch.ui.mainActivity;

import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 23/07/2023.
 */
public class MainActivityViewState {

    private final User currentUser;


    public MainActivityViewState(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
