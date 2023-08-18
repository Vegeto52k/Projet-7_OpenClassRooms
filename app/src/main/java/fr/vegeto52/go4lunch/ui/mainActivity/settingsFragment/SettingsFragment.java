package fr.vegeto52.go4lunch.ui.mainActivity.settingsFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.data.viewModelFactory.ViewModelFactory;
import fr.vegeto52.go4lunch.databinding.FragmentSettingsBinding;
import fr.vegeto52.go4lunch.model.User;

public class SettingsFragment extends Fragment {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mNotificationSwitch;
    private SettingsFragmentViewModel mSettingsFragmentViewModel;
    private User mCurrentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mNotificationSwitch = view.findViewById(R.id.SF_notifications_switch);
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.MA_bottom_navigation_view);
        bottomNavigationView.setVisibility(View.GONE);
        initViewModel();
        return view;
    }

    private void initViewModel(){
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        mSettingsFragmentViewModel = new ViewModelProvider(this, viewModelFactory).get(SettingsFragmentViewModel.class);
        mSettingsFragmentViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), settingsFragmentViewState -> {
            mCurrentUser = settingsFragmentViewState.getCurrentUser();
            managementNotifications();
        });
    }

    private void managementNotifications(){
        boolean notificationsActivation = mCurrentUser.isNotifications();
        mNotificationSwitch.setChecked(notificationsActivation);
        mNotificationSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                enableNotifications();
            } else {
                disableNotifications();
            }
        });
    }

    private void enableNotifications(){
        mSettingsFragmentViewModel.setNotifications(true);
        mCurrentUser.setNotifications(true);
    }

    private void disableNotifications(){
        mSettingsFragmentViewModel.setNotifications(false);
        mCurrentUser.setNotifications(false);
    }
}