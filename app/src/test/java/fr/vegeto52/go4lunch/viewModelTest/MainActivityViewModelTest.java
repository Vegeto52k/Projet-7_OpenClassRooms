package fr.vegeto52.go4lunch.viewModelTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fr.vegeto52.go4lunch.LiveDataTestUtils;
import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.model.User;
import fr.vegeto52.go4lunch.ui.mainActivity.MainActivityViewModel;
import fr.vegeto52.go4lunch.ui.mainActivity.MainActivityViewState;

/**
 * Created by Vegeto52-PC on 21/08/2023.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityViewModelTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    public static final String USER_USER_NAME = "userUserName";
    public static final String USER_PHOTO_URL = "UserPhotoUrl";
    public static final String USER_SELECTED_RESTO = "UserSelectedResto";
    public static final String USER_EMAIL = "UserEmail";

    @Mock
    FirestoreRepository mMockFirestoreRepository;

    private final MutableLiveData<User> mCurrentUserMutableLiveData = new MutableLiveData<>();

    MainActivityViewModel SUT;


    @Before
    public void setup() {
        doReturn(mCurrentUserMutableLiveData)
                .when(mMockFirestoreRepository)
                .getCurrentUserMutableLiveData();

        mCurrentUserMutableLiveData.setValue(getDefaultCurrentUserIn());

        SUT = new MainActivityViewModel(mMockFirestoreRepository);
    }

    @Test
    public void nominalCase(){
        boolean areEqua1 = getDefaultMainActivityViewState().getCurrentUser().equals(LiveDataTestUtils.getValueForTesting(SUT.getCurrentUserLiveData()).getCurrentUser());
        assertTrue(areEqua1);
    }

    private User getDefaultCurrentUserIn() {
        return new User("uid", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true);
    }

    private User getDefaultCurrentUserOut(){
        return new User("uid", USER_USER_NAME, USER_PHOTO_URL, USER_SELECTED_RESTO, USER_EMAIL, null, true);
    }

    public MainActivityViewState getDefaultMainActivityViewState(){
        return  new MainActivityViewState(getDefaultCurrentUserOut());
    }
}
