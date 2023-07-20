package fr.vegeto52.go4lunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.vegeto52.go4lunch.databinding.ActivityAuthenticationBinding;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class AuthenticationActivity extends AppCompatActivity {

    ActivityAuthenticationBinding mBinding;
    Button mButtonFacebook;
    Button mButtonGoogle;
    ImageView mBackgroundAuthView;
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

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

        mAuth = FirebaseAuth.getInstance();

        // Click on Google Button
        mButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        // Click on Facebook Button
        mButtonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    signInWithFacebook();
                Toast.makeText(AuthenticationActivity.this, getResources().getString(R.string.AA_facebook_login_disabled), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    // Firebase Authentication with Google
    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference usersRef = db.collection("users");
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String uid = user.getUid();
                            String name = user.getDisplayName();
                            Uri photoUri = user.getPhotoUrl();
                            String adressMail = user.getEmail();

                            usersRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        Map<String, Object> userDoc = new HashMap<>();
                                        if (documentSnapshot.contains("selectedResto")){
                                            userDoc.put("selectedResto", documentSnapshot.get("selectedResto"));
                                        } else {
                                            userDoc.put("selectedResto", "");
                                        }
                                        if (documentSnapshot.contains("FAVORITE_RESTO_LIST")) {
                                            userDoc.put("FAVORITE_RESTO_LIST", documentSnapshot.get("FAVORITE_RESTO_LIST"));
                                        } else {
                                            userDoc.put("FAVORITE_RESTO_LIST", new ArrayList<String>());
                                        }
                                        usersRef.document(uid).update(userDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                signInSuccesNewActivity();
                                            }
                                        });
                                    } else {
                                        Map<String, Object> userDoc = new HashMap<>();
                                        userDoc.put("userName", name);
                                        userDoc.put("urlPhoto", photoUri);
                                        userDoc.put("adressMail", adressMail);
                                        userDoc.put("selectedResto", "");
                                        userDoc.put("FAVORITE_RESTO_LIST", new ArrayList<String>());
                                        usersRef.document(uid).set(userDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                signInSuccesNewActivity();
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AuthenticationActivity.this, getResources().getString(R.string.AA_authentication_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInSuccesNewActivity(){
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}