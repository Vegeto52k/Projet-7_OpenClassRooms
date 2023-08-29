package fr.vegeto52.go4lunch.viewModelTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.vegeto52.go4lunch.LiveDataTestUtils;
import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.data.repository.NearbySearchRepository;
import fr.vegeto52.go4lunch.data.repository.PlaceDetailsRepository;
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.RestaurantDetails;
import fr.vegeto52.go4lunch.model.User;
import fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment.DetailsRestaurantViewModel;
import fr.vegeto52.go4lunch.ui.mainActivity.detailsRestaurantFragment.DetailsRestaurantViewState;

/**
 * Created by Vegeto52-PC on 24/08/2023.
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailsRestaurantViewModelTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    public static final String USER_USER_NAME = "userUserName";
    public static final String USER_PHOTO_URL = "UserPhotoUrl";
    public static final String USER_SELECTED_RESTO = "UserSelectedResto";
    public static final String USER_EMAIL = "UserEmail";
    public static final String PLACE_ID = "azerty";

    @Mock
    NearbySearchRepository mockNearbySearchRepository;
    @Mock
    FirestoreRepository mockFirestoreRepository;
    @Mock
    PlaceDetailsRepository mockPlaceDetailsRepository;

    private final MutableLiveData<List<Restaurant.Results>> listRestaurantsMutableLiveData =new MutableLiveData<>(); //LiveData<List<Restaurant.Results>> getListRestaurantLiveData(NearbySearchRepository)
    private final MutableLiveData<List<User>> usersMutableLiveData=new MutableLiveData<>(); // LiveData<List<User>> getListUsersMutableLiveData()(FirestoreRepository)
    private final MutableLiveData<User> userMutableLiveData=new MutableLiveData<>();
    private final MutableLiveData<RestaurantDetails.Result> restaurantDetailsLiveData=new MutableLiveData<>();

    DetailsRestaurantViewModel SUT;

    @Before
    public void setup() throws Exception {
        doReturn(listRestaurantsMutableLiveData)
                .when(mockNearbySearchRepository)
                .getListRestaurantLiveData();
        doReturn(usersMutableLiveData)
                .when(mockFirestoreRepository)
                .getListUsersMutableLiveData();
        doReturn(userMutableLiveData)
                .when(mockFirestoreRepository)
                .getCurrentUserMutableLiveData();
        doReturn(restaurantDetailsLiveData)
                .when(mockPlaceDetailsRepository)
                .getRestaurantDetailsLiveData();

        listRestaurantsMutableLiveData.setValue(getDefaultRestaurantListIn());
        usersMutableLiveData.setValue(getDefaultUsersIn());
        userMutableLiveData.setValue(getDefaultCurrentUserIn());
        restaurantDetailsLiveData.setValue(getDefaultRestaurantDetailsIn());

        SUT = new DetailsRestaurantViewModel(mockFirestoreRepository, mockNearbySearchRepository, mockPlaceDetailsRepository);
        SUT.getPlaceDetails(PLACE_ID);
    }

    @Test
    public void nominalCase() throws  Exception {
        assertEquals(getDefaultDetailsRestaurantViewState().getRestaurantList().size(), LiveDataTestUtils.getValueForTesting(SUT.getViewModelDetailsRestaurantLiveData()).getRestaurantList().size());
        assertEquals(getDefaultDetailsRestaurantViewState().getUserList().size(), LiveDataTestUtils.getValueForTesting(SUT.getViewModelDetailsRestaurantLiveData()).getUserList().size());

        boolean areEqua1 = getDefaultDetailsRestaurantViewState().getCurrentUser().equals(LiveDataTestUtils.getValueForTesting(SUT.getViewModelDetailsRestaurantLiveData()).getCurrentUser());
        assertTrue(areEqua1);

        RestaurantDetails.Result result = LiveDataTestUtils.getValueForTesting(SUT.getDetailsRestaurantLiveData());
        boolean areEqua2 = getDefaultRestaurantDetailsOut().equals(result);
        assertTrue(areEqua2);

        String placeId = PLACE_ID;
        SUT.setSelectedRestaurant(placeId);
        verify(mockFirestoreRepository).setSelectedRestaurant(placeId);

        List<String> favoriteResto = new ArrayList<>(Arrays.asList("resto1", "resto2", "resto3"));
        SUT.setFavoritesRestaurant(favoriteResto);
        verify(mockFirestoreRepository).setFavoritesRestaurants(favoriteResto);
    }

    private List<Restaurant.Results> getDefaultRestaurantListIn() {
        ArrayList<Restaurant.Results> resultsArrayList = new ArrayList<>();
        Restaurant.Results r1 = new Restaurant.Results();
        r1.setName("RESTO_O1");
        r1.setPlace_id("RESTO1_ID");
        r1.setVicinity("RESTO1_ADDRESS");
        resultsArrayList.add(r1);
        Restaurant.Results r2 = new Restaurant.Results();
        r2.setName("RESTO_O2");
        r2.setPlace_id("RESTO2_ID");
        r2.setVicinity("RESTO2_ADDRESS");
        resultsArrayList.add(r2);
        Restaurant.Results r3 = new Restaurant.Results();
        r3.setName("RESTO3");
        r3.setPlace_id("RESTO3_ID");
        r3.setVicinity("RESTO3_ADDRESS");
        resultsArrayList.add(r3);
        return resultsArrayList;
    }

    private List<User> getDefaultUsersIn() {
        return Arrays.asList(
                new User("uid", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true),
                new User("uid1", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true),
                new User("uid2", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true)
        );
    }

    private User getDefaultCurrentUserIn() {
        return new User("uid", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true);
    }

    private RestaurantDetails.Result getDefaultRestaurantDetailsIn(){
        RestaurantDetails.Result result = new RestaurantDetails.Result();
        result.setWebsite("RESTO.COM");
        result.setFormatted_phone_number("0600000000");
        return result;
    }


    private List<Restaurant.Results> getDefaultRestaurantListOut() {
        ArrayList<Restaurant.Results> resultsArrayList = new ArrayList<>();
        Restaurant.Results r1 = new Restaurant.Results();
        r1.setName("RESTO_O1");
        r1.setPlace_id("RESTO1_ID");
        r1.setVicinity("RESTO1_ADDRESS");
        resultsArrayList.add(r1);
        Restaurant.Results r2 = new Restaurant.Results();
        r2.setName("RESTO_O2");
        r2.setPlace_id("RESTO2_ID");
        r2.setVicinity("RESTO2_ADDRESS");
        resultsArrayList.add(r2);
        Restaurant.Results r3 = new Restaurant.Results();
        r3.setName("RESTO3");
        r3.setPlace_id("RESTO3_ID");
        r3.setVicinity("RESTO3_ADDRESS");
        resultsArrayList.add(r3);
        return resultsArrayList;
    }

    private List<User> getDefaultUsersOut() {
        return Arrays.asList(
                new User("uid", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true),
                new User("uid1", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true),
                new User("uid2", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true)
        );
    }

    private User getDefaultCurrentUserOut(){
        return new User("uid", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true);
    }

    private RestaurantDetails.Result getDefaultRestaurantDetailsOut(){
        RestaurantDetails.Result result = new RestaurantDetails.Result();
        result.setWebsite("RESTO.COM");
        result.setFormatted_phone_number("0600000000");
        return result;
    }

    public DetailsRestaurantViewState getDefaultDetailsRestaurantViewState(){
        return new DetailsRestaurantViewState(getDefaultUsersOut(), getDefaultCurrentUserOut(), getDefaultRestaurantListOut());
    }
}
