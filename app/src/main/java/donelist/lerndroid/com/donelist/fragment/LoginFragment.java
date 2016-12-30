package donelist.lerndroid.com.donelist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import donelist.lerndroid.com.donelist.CausesActivity;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.SignUpActivity;
import donelist.lerndroid.com.donelist.custom_views.AvenirBookEditText;

/**
 * Created by ivan on 20.12.16.
 */

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = "LoginFragment";
    private static final int REQUEST_GOOGLE_SIGN_IN = 101;

    @BindView(R.id.login_linear)
    LinearLayout mLoginViewsLin;
    @BindView(R.id.fragment_login_login_with_social_tv)
    TextView mLoginWithSocialTv;
    @BindView(R.id.login_with_social_views)
    LinearLayout mLoginWithSocialLin;
    @BindView(R.id.fragment_login_sign_up_tv)
    TextView mSignUpTv;
    @BindView(R.id.fragment_login_user_login)
    AvenirBookEditText mUserLoginEd;
    @BindView(R.id.fragment_login_password)
    AvenirBookEditText mUserPasswordEd;
    @BindView(R.id.fragment_login_sign_in_button)
    Button mSignInButton;
    @BindView(R.id.fragment_login_progress)
    ProgressWheel mProgress;
    @BindView(R.id.fragment_login_logo_img)
    ImageView mLogoImg;
    //Facebook login button
    @BindView(R.id.login_with_facebook)
    LoginButton loginButton;
    //Google plus login button
    @BindView(R.id.login_with_google_plus)
    SignInButton mGoogleSignInButton;

    private boolean isLoginViewsVisible = true;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //facebook
    private CallbackManager mCallbackManager;

    //google+
    private GoogleApiClient mGoogleApiClient;

    public static final Fragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(getActivity(), CausesActivity.class));
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //configure Google Sign-In to request the user data required by app
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        initListeners();
        initFacebookAuth();

        return v;
    }

    private void initListeners() {
        //login views hide animation

        mLoginWithSocialTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateViews();
            }
        });

        mSignUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),

                    /*new Pair<View, String>(v.findViewById(R.id.card_item_cause_title_tv),
                            getString(R.string.transition_name_title)),
                    new Pair<View, String>(v.findViewById(R.id.card_item_cause_description_tv),
                            getString(R.string.transition_name_description)),
                    new Pair<View, String>(v.findViewById(R.id.card_item_cause_date_tv),
                            getString(R.string.transition_name_date)),*/
                        new Pair<View, String>(mLogoImg, getString(R.string.transition_name_image))
                );

                ActivityCompat.startActivity(getActivity(), SignUpActivity.getIntent(getActivity()), options.toBundle());


            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Пробуєм авторизуватись через email/password
                if (!validateForm()) {
                    return;
                }
                hideLogo();

                String email = mUserLoginEd.getText().toString();
                String password = mUserPasswordEd.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getActivity(), CausesActivity.class));
                                } else {
                                    Log.d(TAG, String.valueOf(task.getException()));
                                    showLogo();
                                    Toast.makeText(getActivity(), R.string.something_goes_wrong_check_info, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            }
        });
    }

    private void initFacebookAuth() {

        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        mCallbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: " + exception.toString());

            }
        });
    }

    /**
     * FACEBOOK
     *
     * @param token - токен від facebook для авторизації
     */
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getActivity(), R.string.auth_success,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * GOOGLE PLUS
     */
    private void handleGoogleSignINResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount googleAccount = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), R.string.auth_success, Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(getActivity(), R.string.auth_failed, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });

        }
    }

    @OnClick(R.id.login_with_google_plus)
    public void signInWithGooglePlus() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent, REQUEST_GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignINResult(result);
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mUserLoginEd.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mUserLoginEd.setError(getString(R.string.fielt_cannot_be_empty));
            valid = false;
        }

        String password = mUserPasswordEd.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mUserPasswordEd.setError("Required.");
            valid = false;
        }
        return valid;
    }

    private void animateViews() {

        isLoginViewsVisible = !isLoginViewsVisible;

        if (!isLoginViewsVisible) {
            mLoginWithSocialTv.setText(R.string.log_in_with_login_password_tv_text);
            enableDisableViews(false);
            final Animation hideLoginAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out_login_view);
            final Animation showSocialAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in_login_view);

            hideLoginAnimation.setFillAfter(true);
            showSocialAnimation.setFillAfter(true);

            hideLoginAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Log.d(TAG, "onAnimationStart hideLoginAnimation");

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    Log.d(TAG, "onAnimationEnd hideLoginAnimation");
                    mLoginViewsLin.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            mLoginViewsLin.startAnimation(hideLoginAnimation);
            mLoginWithSocialLin.startAnimation(showSocialAnimation);
            mLoginWithSocialLin.setVisibility(View.VISIBLE);

        } else {
            mLoginWithSocialTv.setText(R.string.log_in_with_social_tv_text);
            enableDisableViews(true);
            final Animation showLoginViews = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in_login_view);
            final Animation hideLoginWithSocialViews = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out_login_view);
            showLoginViews.setFillAfter(true);
            hideLoginWithSocialViews.setFillAfter(true);

            showLoginViews.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mLoginViewsLin.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            hideLoginWithSocialViews.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLoginWithSocialLin.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            mLoginViewsLin.startAnimation(showLoginViews);
            mLoginWithSocialLin.startAnimation(hideLoginWithSocialViews);
        }
    }

    private void enableDisableViews(boolean enabled) {
        mUserLoginEd.setEnabled(enabled);
        mUserPasswordEd.setEnabled(enabled);
        mSignInButton.setEnabled(enabled);
    }

    private void showLogo() {
        Animation showLogo = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in_login_view);
        showLogo.setFillAfter(true);
        showLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mProgress.setVisibility(View.GONE);
                mLogoImg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mProgress.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLogoImg.startAnimation(showLogo);
    }

    private void hideLogo() {
        Animation showLogo = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out_login_view);
        showLogo.setFillAfter(true);
        showLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLogoImg.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mLogoImg.startAnimation(showLogo);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mAuth.removeAuthStateListener(mAuthListener);
        super.onDestroy();
    }


}
