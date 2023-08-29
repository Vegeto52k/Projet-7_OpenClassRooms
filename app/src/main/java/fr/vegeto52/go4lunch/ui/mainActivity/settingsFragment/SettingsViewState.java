package fr.vegeto52.go4lunch.ui.mainActivity.settingsFragment;

import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 17/08/2023.
 */
public class SettingsViewState {

    private final User currentUser;


    public SettingsViewState(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
