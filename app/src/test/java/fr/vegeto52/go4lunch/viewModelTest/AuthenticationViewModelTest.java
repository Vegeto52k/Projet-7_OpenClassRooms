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

import fr.vegeto52.go4lunch.LiveDataTestUtils;
import fr.vegeto52.go4lunch.data.repository.FirestoreRepository;
import fr.vegeto52.go4lunch.ui.authenticationActivity.AuthenticationViewModel;

/**
 * Created by Vegeto52-PC on 04/09/2023.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationViewModelTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    FirestoreRepository mockFirestoreRepository;

    private final MutableLiveData<String> checkUserMutableLiveData = new MutableLiveData<>();

    AuthenticationViewModel SUT;

    @Before
    public void setup() {
        doReturn(checkUserMutableLiveData)
                .when(mockFirestoreRepository)
                .getCheckUserAuthenticateMutableLiveData();

        checkUserMutableLiveData.setValue(getDefaultCheckUserIn());

        mockFirestoreRepository.getCheckUserAuthenticateMutableLiveData();
        SUT = new AuthenticationViewModel();
    }

    @Test
    public void nominalCase(){
        assertEquals(getDefaultCheckUserOut(), LiveDataTestUtils.getValueForTesting(checkUserMutableLiveData));
    }

    private String getDefaultCheckUserIn(){
        return "Success";
    }

    private String getDefaultCheckUserOut(){
        return "Success";
    }
}
