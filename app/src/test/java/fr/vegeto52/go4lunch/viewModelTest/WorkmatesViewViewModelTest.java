package fr.vegeto52.go4lunch.viewModelTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

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
import fr.vegeto52.go4lunch.model.Restaurant;
import fr.vegeto52.go4lunch.model.User;
import fr.vegeto52.go4lunch.ui.mainActivity.workmatesViewFragment.WorkmatesViewViewModel;
import fr.vegeto52.go4lunch.ui.mainActivity.workmatesViewFragment.WorkmatesViewViewState;

/**
 * Created by Vegeto52-PC on 23/08/2023.
 */
@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewViewModelTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    public static final String USER_USER_NAME = "userUserName";
    public static final String USER_PHOTO_URL = "UserPhotoUrl";
    public static final String USER_SELECTED_RESTO = "UserSelectedResto";
    public static final String USER_EMAIL = "UserEmail";

    @Mock
    NearbySearchRepository mockNearbySearchRepository;
    @Mock
    FirestoreRepository mockFirestoreRepository;

    private final MutableLiveData<List<Restaurant.Results>> listRestaurantsMutableLiveData =new MutableLiveData<>(); //LiveData<List<Restaurant.Results>> getListRestaurantLiveData(NearbySearchRepository)
    private final MutableLiveData<List<User>> usersMutableLiveData=new MutableLiveData<>(); // LiveData<List<User>> getListUsersMutableLiveData()(FirestoreRepository)

    WorkmatesViewViewModel SUT;

    @Before
    public void setup() {
        doReturn(listRestaurantsMutableLiveData)
                .when(mockNearbySearchRepository)
                .getListRestaurantLiveData();
        doReturn(usersMutableLiveData)
                .when(mockFirestoreRepository)
                .getListUsersMutableLiveData();

        listRestaurantsMutableLiveData.setValue(getDefaultRestaurantListIn());
        usersMutableLiveData.setValue(getDefaultUsersIn());

        SUT = new WorkmatesViewViewModel(mockFirestoreRepository, mockNearbySearchRepository);
    }

    @Test
    public void nominalCase() {
        assertEquals(getDefaultWorkmatesViewViewState().getRestaurantsList().size(), LiveDataTestUtils.getValueForTesting(SUT.getWorkmatesViewLiveData()).getRestaurantsList().size());
        assertEquals(getDefaultWorkmatesViewViewState().getUserList().size(), LiveDataTestUtils.getValueForTesting(SUT.getWorkmatesViewLiveData()).getUserList().size());
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

    public WorkmatesViewViewState getDefaultWorkmatesViewViewState(){
        return new WorkmatesViewViewState(getDefaultUsersOut(), getDefaultRestaurantListOut());
    }
}
