package com.apockestafe.team19;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.internal.FacebookDialogFragment.TAG;

/*
*** LoginFragment is used to verify the Facebook login was a success, and
*** if so launches the MainActivity
 */
public class LoginFragment extends Fragment {

    private CallbackManager mCallbackManager;
    public AccessTokenTracker mAccessTokenTracker;
    public ProfileTracker mProfileTracker;
    private SharedPreferencesEditor editor;
    public static Context contextOfApplication;
    LoginButton loginButton;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            AccessToken accesstoken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Context applicationContext = MainActivity.getContextOfApplication();
            editor = new SharedPreferencesEditor(applicationContext.getSharedPreferences("login", MODE_PRIVATE));
            final String name = profile.getName();
            System.out.println("NAME: " + name);
            editor.addName(name);
            Log.d("get me profile", name);
            //handleFacebookAccessToken(accesstoken);
            openNextActivity();

        }

        @Override
        public void onCancel() {
//            FacebookSdk.sdkInitialize(getApplicationContext());
//            LoginManager.getInstance().logOut();
//            AccessToken.setCurrentAccessToken(null);
        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOfApplication = getApplicationContext();

        AccessToken at = AccessToken.getCurrentAccessToken();
        try {
            final String data = getActivity().getIntent().getStringExtra("accessToken");
            if (data.equals("null"))
                at = null;
        } catch (Exception e){ e.printStackTrace();}


        if (at != null) {
            Profile profile = Profile.getCurrentProfile();
            final String name = profile.getName();
            Log.d("get me profile", name);
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.putExtra("name", name);
            startActivity(i);
        }

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();

        AccessTokenTracker mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldtracker, AccessToken newtracker) {
            if (newtracker != null)
                openNextActivity();
            }
        };

        mAccessTokenTracker.startTracking();

        ProfileTracker mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldprofile, Profile newprofile) {

            }
        };

        mProfileTracker.startTracking();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_signin, container, false);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_status"));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                openNextActivity();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void openNextActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}