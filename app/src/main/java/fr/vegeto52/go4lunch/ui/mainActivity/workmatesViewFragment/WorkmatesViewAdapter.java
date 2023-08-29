package fr.vegeto52.go4lunch.ui.mainActivity.workmatesViewFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;
import fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment.DetailsRestaurantFragment;

/**
 * Created by Vegeto52-PC on 03/08/2023.
 */
public class WorkmatesViewAdapter extends RecyclerView.Adapter<WorkmatesViewAdapter.ViewHolder> {

    private static List<User> mUserList;
    private static List<Restaurant.Results> mRestaurantList;

    public WorkmatesViewAdapter(List<User> usersList, List<Restaurant.Results> restaurantsList) {
        mUserList = usersList;
        mRestaurantList = restaurantsList;
    }

    @NonNull
    @Override
    public WorkmatesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewAdapter.ViewHolder holder, int position) {
        holder.displayWorkmatesView(mUserList.get(position));
        String urlPhotoPeople = mUserList.get(position).getUrlPhoto();
        Glide.with(holder.mPhotoWorkmate.getContext())
                .load(urlPhotoPeople)
                .centerCrop()
                .into(holder.mPhotoWorkmate);

        List<String> placeIdList = new ArrayList<>();
        for (Restaurant.Results restaurant : mRestaurantList) {
            placeIdList.add(restaurant.getPlace_id());
        }
        if (!holder.mPlaceId.isEmpty() && placeIdList.contains(holder.mPlaceId)) {
            holder.itemView.setOnClickListener(view -> {
                Fragment fragment = new DetailsRestaurantFragment();
                Bundle args = new Bundle();
                args.putString("placeId", holder.mPlaceId);
                fragment.setArguments(args);
                if (view.getContext() instanceof AppCompatActivity) {
                    ((AppCompatActivity) view.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.MA_fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView mPhotoWorkmate;
        public TextView mNameWorkmate;
        Context mContext;
        String mPlaceId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoWorkmate = itemView.findViewById(R.id.WVF_photo_workmates);
            mNameWorkmate = itemView.findViewById(R.id.WVF_name_workmates);
            mContext = itemView.getContext();
        }

        public void displayWorkmatesView(User user) {
            String fullName = user.getUserName();
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts[0];
            if (user.getSelectedResto() != null) {

                mPlaceId = user.getSelectedResto();
                if (mPlaceId.equals("")) {
                    String hasNotDecidedYet = mContext.getString(R.string.WVF_has_not_decided_yet);
                    String formattedString = String.format(hasNotDecidedYet, firstName);
                    mNameWorkmate.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
                    mNameWorkmate.setText(formattedString);
                } else {
                    for (Restaurant.Results restaurant : mRestaurantList) {
                        if (restaurant.getPlace_id().equals(mPlaceId)) {
                            String nameRestaurant = restaurant.getName();
                            String eatingAtRestaurant = mContext.getString(R.string.WVF_is_eating_at);
                            String formattedString = String.format(eatingAtRestaurant, firstName, nameRestaurant);
                            mNameWorkmate.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                            mNameWorkmate.setText(formattedString);
                            break;
                        } else {
                            String eatingAtRestaurant = mContext.getString(R.string.WVF_is_eating_at_a_distant_restaurant);
                            String formattedString = String.format(eatingAtRestaurant, firstName);
                            mNameWorkmate.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
                            mNameWorkmate.setText(formattedString);
                        }
                    }
                }
            }
        }
    }
}
