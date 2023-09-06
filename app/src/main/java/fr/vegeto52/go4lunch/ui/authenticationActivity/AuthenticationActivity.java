package fr.vegeto52.go4lunch.ui.authenticationActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import fr.vegeto52.go4lunch.R;
import fr.vegeto52.go4lunch.databinding.ActivityAuthenticationBinding;
import fr.vegeto52.go4lunch.ui.mainActivity.MainActivity;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class AuthenticationActivity extends AppCompatActivity {

    AuthenticationViewModel mAuthenticationActivityViewModel;
    ActivityAuthenticationBinding mBinding;
    Button mButtonFacebook;
    Button mButtonGoogle;
    ImageView mBackgroundAuthView;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> mGoogleSignInLauncher;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        initUi();
    }

    //Initialize UI
    private void initUi() {
        mBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        mButtonFacebook = findViewById(R.id.AA_sign_in_button_facebook);
        mButtonGoogle = findViewById(R.id.AA_sign_in_button_google);
        mBackgroundAuthView = findViewById(R.id.AA_background_auth_view);

        initViewModel();

        // Configure background
        Glide.with(this)
                .load("https://restaurant-lasiesta.fr/wp-content/uploads/2022/12/la-siesta-restaurant-canet-en-roussillon-2-570x855.jpeg")
                .transform(new ColorFilterTransformation(Color.argb(100, 0, 0, 0)), new BlurTransformation(25))
                .into(mBackgroundAuthView);

        // Configure Google authentication with Firebase authentication
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize ActivityResultLauncher
        mGoogleSignInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleGoogleSignInResult(task);
                }
            }
        });

        // Click on Google Button
        mButtonGoogle.setOnClickListener(view12 -> signInWithGoogle());

        // Click on Facebook Button
        mButtonFacebook.setOnClickListener(view1 -> {
            //    signInWithFacebook();
            Toast.makeText(AuthenticationActivity.this, getResources().getString(R.string.AA_facebook_login_disabled), Toast.LENGTH_SHORT).show();
        });
    }

    private void initViewModel(){
        mAuthenticationActivityViewModel = new AuthenticationViewModel();
    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mGoogleSignInLauncher.launch(signInIntent);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            firebaseAuthWithGoogle(idToken);
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
        }
    }

    // Firebase Authentication with Google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuthenticationActivityViewModel.createOrUpdateUser(credential);
        mAuthenticationActivityViewModel.getCheckUserAuthenticateLiveData().observe(this, s -> {
            if (Objects.equals(s, "Success")) {
                signInSuccesNewActivity();
            } else {
                Toast.makeText(AuthenticationActivity.this, getResources().getString(R.string.AA_authentication_failed),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Sign In Success, Go to MainActivity
    private void signInSuccesNewActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}