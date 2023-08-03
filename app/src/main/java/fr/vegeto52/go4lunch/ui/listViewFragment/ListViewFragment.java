package fr.vegeto52.go4lunch.ui.listViewFragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.data.viewModelFactory.ViewModelFactory;
import fr.vegeto52.go4lunch.databinding.FragmentListViewBinding;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;


public class ListViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Location mLocation;
    private List<Restaurant.Results> mListRestaurants;
    private List<User> mListUsers;
    private FragmentListViewBinding mBinding;
    private List<Restaurant.Results> mFilteredListRestaurants;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mRecyclerView = view.findViewById(R.id.LVF_recyclerview_list_resto);
        return view;
    }

    // Initialize ViewModel
    private void initViewModel(){
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        ListViewViewModel listViewViewModel = new ViewModelProvider(this, viewModelFactory).get(ListViewViewModel.class);
        listViewViewModel.getListViewLiveData().observe(getViewLifecycleOwner(), listViewViewState -> {
            mLocation = listViewViewState.getLocation();
            mListRestaurants = listViewViewState.getRestaurantList();
            mListUsers = listViewViewState.getUserList();
            initRecyclerView();
            mBinding.LVFListRestoViewEmpty.setVisibility(mListRestaurants.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    // Initialize RecyclerView
    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mListRestaurants, mListUsers);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(listViewAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
        inflater.inflate(R.menu.menu_search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.MS_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Perform Search
    @SuppressLint("NotifyDataSetChanged")
    private void performSearch(String text){
        ListViewAdapter listViewAdapter;
        if (text.length() >= 3){
            mFilteredListRestaurants = filterRestaurants(text);
            listViewAdapter = new ListViewAdapter(mLocation, mFilteredListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        } else {
            listViewAdapter = new ListViewAdapter(mLocation, mListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        }
        listViewAdapter.notifyDataSetChanged();
    }

    private List<Restaurant.Results> filterRestaurants(String text){
        List<Restaurant.Results> filteredList = new ArrayList<>();
        for (Restaurant.Results restaurant : mListRestaurants){
            if (restaurant.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(restaurant);
            }
        }
        return filteredList;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.MS_sort_name){
            sortByName();
            return true;
        } else if (id == R.id.MS_sort_distance) {
            sorByDistance();
            return true;
        } else if (id == R.id.MS_sort_rating){
            sortByRating();
            return true;
        } else if (id == R.id.MS_sort_workmates) {
            sortByWorkmates();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Sort by Name
    private void sortByName(){
        if (mFilteredListRestaurants.isEmpty()){
            Collections.sort(mListRestaurants, (results, t1) -> results.getName().compareToIgnoreCase(t1.getName()));
            ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        } else {
            Collections.sort(mFilteredListRestaurants, (results, t1) -> results.getName().compareToIgnoreCase(t1.getName()));
            ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mFilteredListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        }
    }

    // Sort by Distance
    private void sorByDistance(){
        if (mFilteredListRestaurants.isEmpty()){
            Collections.sort(mListRestaurants, (results, t1) -> {
                float distance = results.getDistance();
                float distance1 = t1.getDistance();
                return Float.compare(distance, distance1);
            });
            ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        } else {
            Collections.sort(mFilteredListRestaurants, (results, t1) -> {
                float distance = results.getDistance();
                float distance1 = t1.getDistance();
                return Float.compare(distance, distance1);
            });
            ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mFilteredListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        }

    }

    // Sort by Rating
    private void sortByRating(){
        if (mFilteredListRestaurants.isEmpty()){
            Collections.sort(mListRestaurants, (results, t1) -> {
                double rating = results.getRating();
                double rating1 = t1.getRating();
                return Double.compare(rating1, rating);
            });
            ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        } else {
            Collections.sort(mFilteredListRestaurants, (results, t1) -> {
                double rating = results.getRating();
                double rating1 = t1.getRating();
                return Double.compare(rating1, rating);
            });
            ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mFilteredListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        }
    }

    // Sort by Workmates
    private void sortByWorkmates(){
        if (mFilteredListRestaurants.isEmpty()){
            Collections.sort(mListRestaurants, (results, t1) -> {
                int workmatesSelected = results.getWorkmates_selected();
                int workmatesSelected1 = t1.getWorkmates_selected();
                return Integer.compare(workmatesSelected1, workmatesSelected);
            });
            ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        } else {
            Collections.sort(mFilteredListRestaurants, (results, t1) -> {
                int workmatesSelected = results.getWorkmates_selected();
                int workmatesSelected1 = t1.getWorkmates_selected();
                return Integer.compare(workmatesSelected1, workmatesSelected);
            });
            ListViewAdapter listViewAdapter = new ListViewAdapter(mLocation, mFilteredListRestaurants, mListUsers);
            mRecyclerView.setAdapter(listViewAdapter);
        }

    }
}