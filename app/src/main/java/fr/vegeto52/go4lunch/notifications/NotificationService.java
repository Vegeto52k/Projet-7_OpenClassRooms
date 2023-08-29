package fr.vegeto52.go4lunch.notifications;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.data.MainApplication;
import fr.vegeto52.go4lunch.data.api.NearbySearchApi;
import fr.vegeto52.go4lunch.data.api.RetrofitService;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;
import fr.vegeto52.go4lunch.ui.authenticationActivity.AuthenticationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vegeto52-PC on 08/08/2023.
 */
public class NotificationService extends FirebaseMessagingService {

    private final List<User> mUserList = new ArrayList<>();
    private User mUser;
    private List<Restaurant.Results> mRestaurantList;
    private String mNameRestaurant;
    private String mAddressRestaurant;
    private final List<User> mListUserWithSameRestaurant = new ArrayList<>();
    private static final String COLLECTION_NAME = "users";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification() != null) {
            Log.d("Notification Reçu", "J'ai bien reçu " + message.getNotification().getBody());
            getListUserAndCurrentUser();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        Context context = MainApplication.getApplication();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this::getListRestaurants);
    }

    private void getListRestaurants(Location location) {
        NearbySearchApi nearbySearchApi = RetrofitService.getRetrofitInstance().create(NearbySearchApi.class);
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        String latLng = "" + currentLatitude + "," + currentLongitude;
        int radius = 1500;
        String type = "restaurant";
        String map_key = "AIzaSyCuiuuSU0l2xgi-bWz_gYHM1EkZJeuWmx4";

        nearbySearchApi.getObjectRestaurant(latLng, radius, type, map_key).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(@NonNull Call<Restaurant> call, @NonNull Response<Restaurant> response) {
                assert response.body() != null;
                mRestaurantList = response.body().getResults();
                getInformationsRestaurant();
                sendVisualNotification();
            }

            @Override
            public void onFailure(@NonNull Call<Restaurant> call, @NonNull Throwable t) {
                mRestaurantList = null;
            }
        });
    }

    private void getListUserAndCurrentUser() {
        String uidCurrentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    String uid = documentSnapshot.getId();
                    assert user != null;
                    user.setUid(uid);
                    mUserList.add(user);
                    if (uidCurrentUser.equals(user.getUid())) {
                        mUser = user;
                    }
                }
                if (mUser.isNotifications())
                    getLocation();
            }
        });
    }

    private void getInformationsRestaurant() {
        String placeId = mUser.getSelectedResto();
        for (Restaurant.Results restaurant : mRestaurantList) {
            if (restaurant.getPlace_id().equals(placeId)) {
                mNameRestaurant = restaurant.getName();
                mAddressRestaurant = restaurant.getVicinity();
                break;
            }
        }
        mListUserWithSameRestaurant.clear();
        for (User user : mUserList) {
            assert user.getSelectedResto() != null;
            if (user.getSelectedResto().equals(placeId)) {
                mListUserWithSameRestaurant.add(user);
            }
        }
    }

    private void sendVisualNotification() {
        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, AuthenticationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build a Notification object
        String CHANNEL_ID = "Go4Lunch";
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(NotificationService.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon_app)
                        .setContentTitle(getResources().getString(R.string.NS_recap_for_lunch))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        StringBuilder userNamesBuilder = new StringBuilder();
        for (User user : mListUserWithSameRestaurant) {
            userNamesBuilder.append(user.getUserName()).append("\n");
        }
        String userNamesString = userNamesBuilder.toString();
        String infoLunch = getResources().getString(R.string.NS_info_lunch);
        String formattedInfoLunch = String.format(infoLunch, mNameRestaurant, mAddressRestaurant, userNamesString);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(formattedInfoLunch);
        notificationBuilder.setStyle(bigTextStyle);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Firebase Messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        int NOTIFICATION_ID = 7;
        String NOTIFICATION_TAG = "FIREBASEOC";
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }


}
