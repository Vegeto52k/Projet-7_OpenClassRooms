package fr.vegeto52.go4lunch.ui.workmatesViewFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.data.viewModelFactory.ViewModelFactory;
import fr.vegeto52.go4lunch.databinding.FragmentWorkmatesViewBinding;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;

public class WorkmatesViewFragment extends Fragment {

    FragmentWorkmatesViewBinding mBinding;
    WorkmatesViewViewModel mWorkmatesViewViewModel;
    RecyclerView mRecyclerView;
    List<User> mListUsers = new ArrayList<>();
    List<Restaurant.Results> mListRestaurants = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentWorkmatesViewBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mRecyclerView = view.findViewById(R.id.WVF_recyclerview_list_workmates);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
    }

    private void initViewModel(){
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        mWorkmatesViewViewModel = new ViewModelProvider(this, viewModelFactory).get(WorkmatesViewViewModel.class);
        mWorkmatesViewViewModel.getWorkmatesViewLiveData().observe(getViewLifecycleOwner(), workmatesViewViewState -> {
            mListUsers = workmatesViewViewState.getUserList();
            mListRestaurants = workmatesViewViewState.getRestaurantsList();
            initRecyclerView();
            mBinding.WVFListWorkmateViewEmpty.setVisibility(mListUsers.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        WorkmatesViewAdapter workmatesViewAdapter = new WorkmatesViewAdapter(mListUsers, mListRestaurants);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(workmatesViewAdapter);
    }
}