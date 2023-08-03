package fr.vegeto52.go4lunch.ui.listViewFragment;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;
import fr.vegeto52.go4lunch.ui.DetailsRestaurantFragment;

/**
 * Created by Vegeto52-PC on 01/08/2023.
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private static Location mUserLocation;
    private static List<Restaurant.Results> mListRestaurants;
    private static List<User> mListUser;


    public ListViewAdapter(Location location, List<Restaurant.Results> restaurants, List<User> users) {
        mUserLocation = location;
        mListRestaurants = restaurants;
        mListUser = users;
    }

    @NonNull
    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewAdapter.ViewHolder holder, int position) {
        holder.displayRestaurant(mListRestaurants.get(position));

        // Show photo
        String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + mListRestaurants.get(position).getPhotos().get(0).getPhoto_reference() + "&key=AIzaSyCuiuuSU0l2xgi-bWz_gYHM1EkZJeuWmx4";
        Glide.with(holder.mPhotoRestaurant.getContext())
                .load(urlPhoto)
                .centerCrop()
                .into(holder.mPhotoRestaurant);

        // Click on item
        holder.itemView.setOnClickListener(view -> {
            Fragment fragment = new DetailsRestaurantFragment();
            Bundle args = new Bundle();
            args.putString("placeId", holder.mPlaceId);
            fragment.setArguments(args);
            if (view.getContext() instanceof AppCompatActivity){
                ((AppCompatActivity) view.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.MA_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListRestaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mNameRestaurant;
        public TextView mAddressRestaurant;
        public ImageView mPhotoRestaurant;
        public TextView mOpenHour;
        public TextView mDistance;
        public TextView mNumberPerson;
        public ImageView mIconPerson;
        public ImageView mIconStarRating1;
        public ImageView mIconStarRating2;
        public ImageView mIconStarRating3;
        public Context mContext;
        public Location mLocation;
        public double mRating;
        String mPlaceId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameRestaurant = itemView.findViewById(R.id.LVF_name_restaurant);
            mAddressRestaurant = itemView.findViewById(R.id.LVF_adress_restaurant);
            mPhotoRestaurant = itemView.findViewById(R.id.LVF_photo_restaurant);
            mOpenHour = itemView.findViewById(R.id.LVF_open_hour);
            mDistance = itemView.findViewById(R.id.LVF_distance);
            mNumberPerson = itemView.findViewById(R.id.LVF_number_person);
            mIconPerson = itemView.findViewById(R.id.LVF_icon_person);
            mIconStarRating1 = itemView.findViewById(R.id.LVF_icon_star_rating_1);
            mIconStarRating2 = itemView.findViewById(R.id.LVF_icon_star_rating_2);
            mIconStarRating3 = itemView.findViewById(R.id.LVF_icon_star_rating_3);
            mContext = itemView.getContext();
        }

        public void displayRestaurant(Restaurant.Results results){
            // Show name
            mNameRestaurant.setText(results.getName());
            // Show address
            mAddressRestaurant.setText(results.getVicinity());
            // Show open/close
            if (results.getOpening_hours() == null){
                mOpenHour.setText(mContext.getString(R.string.LVF_no_information));
                mOpenHour.setTextColor(Color.BLUE);
            } else {
                if (results.getOpening_hours().getOpen_now()){
                    mOpenHour.setText(mContext.getString(R.string.LVF_open));
                } else {
                    mOpenHour.setText(mContext.getString(R.string.LVF_close));
                    mOpenHour.setTextColor(Color.RED);
                }
            }
            // Show distance
            mLocation = new Location("");
            mLocation.setLatitude(results.getGeometry().getLocation().getLat());
            mLocation.setLongitude(results.getGeometry().getLocation().getLng());
            if (mUserLocation != null){
                float distanceUserRestaurant = mUserLocation.distanceTo(mLocation);
                mDistance.setText(String.format(Locale.US, "%.0f m", distanceUserRestaurant));
                results.setDistance(distanceUserRestaurant);
            }
            // Show user's number
            if (mListUser != null){
                int count = 0;
                for (User user : mListUser){
                    if (user.getSelectedResto() != null){
                        if (user.getSelectedResto().equals(results.getPlace_id())){
                            count++;
                        }
                    }
                }
                String numberOfPersonsFormat = mContext.getString(R.string.LVF_count_user);
                String numberOfPersonsString = String.format(numberOfPersonsFormat, count);
                mNumberPerson.setText(numberOfPersonsString);
                results.setWorkmates_selected(count);
            }
            // Show rating
            mRating = results.getRating();
            if (mRating <= 1.25){
                mIconStarRating1.setVisibility(View.GONE);
                mIconStarRating2.setVisibility(View.GONE);
                mIconStarRating3.setVisibility(View.GONE);
            } else if (mRating > 1.25 && mRating <= 2.5) {
                mIconStarRating2.setVisibility(View.GONE);
                mIconStarRating3.setVisibility(View.GONE);
            } else if (mRating > 2.5 && mRating <= 3.75) {
                mIconStarRating3.setVisibility(View.GONE);
            }
        }
    }
}
