package fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.model.User;

/**
 * Created by Vegeto52-PC on 05/08/2023.
 */
public class DetailsRestaurantAdapter extends RecyclerView.Adapter<DetailsRestaurantAdapter.ViewHolder> {

    private final List<User> mUserList;

    public DetailsRestaurantAdapter(List<User> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public DetailsRestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_people_resto_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsRestaurantAdapter.ViewHolder holder, int position) {
        holder.displayPeopleRestaurantDetails(mUserList.get(position));
        String urlPhotoPeople = mUserList.get(position).getUrlPhoto();
        Glide.with(holder.mPhotoPeople.getContext())
                .load(urlPhotoPeople)
                .centerCrop()
                .into(holder.mPhotoPeople);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView mPhotoPeople;
        public TextView mNamePeople;
        Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoPeople = itemView.findViewById(R.id.DRF_photo_workmates);
            mNamePeople = itemView.findViewById(R.id.DRF_name_workmates);
            mContext = itemView.getContext();
        }

        public void displayPeopleRestaurantDetails(User user) {
            String fullName = user.getUserName();
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts[0];
            String isJoining = mContext.getResources().getString(R.string.DRF_is_joining);
            String formattedString = String.format(isJoining, firstName);
            mNamePeople.setText(formattedString);
        }
    }
}
