package fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.data.viewModelFactory.ViewModelFactory;
import fr.vegeto52.go4lunch.databinding.FragmentDetailsRestaurantBinding;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.RestaurantDetails;
import fr.vegeto52.go4lunch.model.User;


public class DetailsRestaurantFragment extends Fragment {

    private DetailsRestaurantViewModel mDetailsRestaurantViewModel;
    private String mPlaceId;
    private List<User> mListUsers;
    private User mCurrentUser;
    private List<Restaurant.Results> mListRestaurant;
    private RestaurantDetails.Result mRestaurantDetails;
    private BottomNavigationView mBottomNavigationView;
    private ImageView mPhotoRestoReference;
    private TextView mNameResto;
    private TextView mAdressResto;
    private ImageView mStarRating1;
    private ImageView mStarRating2;
    private ImageView mStarRating3;
    private ImageButton mRestoSelected;
    private Button mCallResto;
    private Button mLikeResto;
    private Button mWebsiteResto;
    private RecyclerView mRecyclerView;
    private boolean iconCheckedRestoSelected;
    private boolean iconLikeDislikeResto;
    private List<String> mUserFavoriteResto = new ArrayList<>();
    private final List<User> mListUserSelectedRestaurant = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        fr.vegeto52.go4lunch.databinding.FragmentDetailsRestaurantBinding binding = FragmentDetailsRestaurantBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mBottomNavigationView = requireActivity().findViewById(R.id.MA_bottom_navigation_view);
        mPhotoRestoReference = view.findViewById(R.id.DRF_photo_restaurant_details);
        mNameResto = view.findViewById(R.id.DRF_name_restaurant_details);
        mAdressResto = view.findViewById(R.id.DRF_adress_restaurant_details);
        mStarRating1 = view.findViewById(R.id.DRF_icon_star_rating_details_1);
        mStarRating2 = view.findViewById(R.id.DRF_icon_star_rating_details_2);
        mStarRating3 = view.findViewById(R.id.DRF_icon_star_rating_details_3);
        mRestoSelected = view.findViewById(R.id.DRF_check_resto_selected);
        mCallResto = view.findViewById(R.id.DRF_call_resto_details);
        mLikeResto = view.findViewById(R.id.DRF_like_resto_details);
        mWebsiteResto = view.findViewById(R.id.DRF_website_resto_details);
        mRecyclerView = view.findViewById(R.id.DRF_recyclerview_list_people_resto_details);

        Bundle args = getArguments();
        if (args != null) {
            mPlaceId = args.getString("placeId");
        }
        initViewModel();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Initialize ViewModel
    private void initViewModel() {
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        mDetailsRestaurantViewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsRestaurantViewModel.class);
        mDetailsRestaurantViewModel.getViewModelDetailsRestaurantLiveData().observe(getViewLifecycleOwner(), detailsRestaurantViewState -> {
            mListUsers = detailsRestaurantViewState.getUserList();
            mCurrentUser = detailsRestaurantViewState.getCurrentUser();
            mListRestaurant = detailsRestaurantViewState.getRestaurantList();

            mDetailsRestaurantViewModel.getPlaceDetails(mPlaceId);
            mDetailsRestaurantViewModel.getDetailsRestaurantLiveData().observe(getViewLifecycleOwner(), result -> {
                mRestaurantDetails = result;
                initUI();
            });
        });
    }

    // Initialize UI
    private void initUI() {
        mBottomNavigationView.setVisibility(View.GONE);
        for (Restaurant.Results restaurant : mListRestaurant) {
            if (restaurant.getPlace_id().equals(mPlaceId)) {
                mNameResto.setText(restaurant.getName());
                String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + restaurant.getPhotos().get(0).getPhoto_reference() + "&key=AIzaSyCuiuuSU0l2xgi-bWz_gYHM1EkZJeuWmx4";
                Glide.with(this)
                        .load(urlPhoto)
                        .into(mPhotoRestoReference);
                mAdressResto.setText(restaurant.getVicinity());
                starsRatingUI(restaurant.getRating());
                break;
            }
        }
        restoSelectedUI();
        callButton();
        likeDislikeButton();
        websiteButton();
        checkUserSelectedRestaurant();
    }

    // Initialize RecyclerView
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        DetailsRestaurantAdapter detailsRestaurantAdapter = new DetailsRestaurantAdapter(mListUserSelectedRestaurant);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(detailsRestaurantAdapter);
    }

    // Check if user selected restaurant
    private void checkUserSelectedRestaurant() {
        mListUserSelectedRestaurant.clear();
        for (User user : mListUsers) {
            if (user.getSelectedResto() != null && user.getSelectedResto().equals(mPlaceId)) {
                mListUserSelectedRestaurant.add(user);
            }
        }
        initRecyclerView();
    }

    // Initialize stars rating
    private void starsRatingUI(double rating) {
        if (rating <= 1.25) {
            mStarRating1.setVisibility(View.GONE);
            mStarRating2.setVisibility(View.GONE);
            mStarRating3.setVisibility(View.GONE);
        } else if (rating > 1.25 && rating <= 2.5) {
            mStarRating2.setVisibility(View.GONE);
            mStarRating3.setVisibility(View.GONE);
        } else if (rating > 2.5 && rating <= 3.75) {
            mStarRating3.setVisibility(View.GONE);
        }
    }

    // Button resto selected
    private void restoSelectedUI() {
        if (mCurrentUser.getSelectedResto() != null && mCurrentUser.getSelectedResto().equals(mPlaceId)) {
            mRestoSelected.setImageResource(R.drawable.baseline_check_circle_24);
            iconCheckedRestoSelected = true;
        } else {
            mRestoSelected.setImageResource(R.drawable.baseline_cancel_24);
            iconCheckedRestoSelected = false;
        }
        mRestoSelected.setOnClickListener(view -> {
            if (iconCheckedRestoSelected) {
                mDetailsRestaurantViewModel.setSelectedRestaurant("");
                mListUsers.remove(mCurrentUser);
                mCurrentUser.setSelectedResto("");
                mRestoSelected.setImageResource(R.drawable.baseline_cancel_24);
                iconCheckedRestoSelected = false;
                checkUserSelectedRestaurant();
            } else {
                mDetailsRestaurantViewModel.setSelectedRestaurant(mPlaceId);
                mCurrentUser.setSelectedResto(mPlaceId);
                boolean isUserAlreadyInList = false;
                for (User user : mListUsers) {
                    if (user.getUid().equals(mCurrentUser.getUid())) {
                        isUserAlreadyInList = true;
                        break;
                    }
                }
                if (!isUserAlreadyInList) {
                    mListUsers.add(mCurrentUser);
                }
                mRestoSelected.setImageResource(R.drawable.baseline_check_circle_24);
                iconCheckedRestoSelected = true;
                checkUserSelectedRestaurant();
            }
        });
    }

    // Button call
    private void callButton() {
        mCallResto.setOnClickListener(view -> {
            if (mRestaurantDetails.getFormatted_phone_number() != null) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mRestaurantDetails.getFormatted_phone_number()));
                startActivity(dialIntent);
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.DRF_no_phone_number_provided), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Button like-dislike
    private void likeDislikeButton() {
        mUserFavoriteResto = mCurrentUser.getFavoritesResto();
        if (mUserFavoriteResto.contains(mPlaceId)) {
            mLikeResto.setText(getResources().getString(R.string.DRF_like));
            mLikeResto.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.baseline_star_like_24, 0, 0);
            iconLikeDislikeResto = false;
        } else {
            mLikeResto.setText(getResources().getString(R.string.DRF_dislike));
            mLikeResto.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.baseline_star_outline_24, 0, 0);
            iconLikeDislikeResto = true;
        }

        mLikeResto.setOnClickListener(view -> {
            if (iconLikeDislikeResto) {
                mUserFavoriteResto.add(mPlaceId);
                mDetailsRestaurantViewModel.setFavoritesRestaurant(mUserFavoriteResto);
                mLikeResto.setText(getResources().getString(R.string.DRF_like));
                mLikeResto.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.baseline_star_like_24, 0, 0);
                Toast.makeText(getContext(), getResources().getString(R.string.DRF_restaurant_added_to_favorites), Toast.LENGTH_SHORT).show();
                iconLikeDislikeResto = false;
            } else {
                mUserFavoriteResto.remove(mPlaceId);
                mDetailsRestaurantViewModel.setFavoritesRestaurant(mUserFavoriteResto);
                mLikeResto.setText(getResources().getString(R.string.DRF_dislike));
                mLikeResto.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.baseline_star_outline_24, 0, 0);
                Toast.makeText(getContext(), getResources().getString(R.string.DRF_restaurant_removed_from_favorites), Toast.LENGTH_SHORT).show();
                iconLikeDislikeResto = true;
            }
        });
    }

    // Button website
    private void websiteButton() {
        mWebsiteResto.setOnClickListener(view -> {
            if (mRestaurantDetails.getWebsite() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mRestaurantDetails.getWebsite()));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.DRF_no_website_provided), Toast.LENGTH_SHORT).show();
            }
        });
    }
}