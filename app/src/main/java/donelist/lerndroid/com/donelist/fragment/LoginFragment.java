package donelist.lerndroid.com.donelist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import donelist.lerndroid.com.donelist.CausesActivity;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.custom_views.AvenirBookEditText;

/**
 * Created by ivan on 20.12.16.
 */

public class LoginFragment extends Fragment {
    private final String TAG = "LoginFragment";

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

    private boolean isLoginViewsVisible = true;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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


        mAuth.createUserWithEmailAndPassword("vaniakerer9@gmail.com", "qweewqqwe")
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "Complated");
                        if (task.isSuccessful()){
                            Log.d(TAG, "Success");


                        } else {
                            Log.d(TAG, String.valueOf(task.getException()));

                        }
                    }
                });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        initListeners();
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
                //TODO create sign up fragment
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "We Are Here");

                //Пробуєм авторизуватись через email/password
                if (!validateForm()) {
                    return;
                }

                String email = mUserLoginEd.getText().toString();
                String password = mUserPasswordEd.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "We Are Here");
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getActivity(), CausesActivity.class));
                                } else {
                                    Log.d(TAG, String.valueOf(task.getException()));
                                }
                            }
                        });
            }
        });

        mSignUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.fragment_container, SignInFragment.newInstance(), SignInFragment.TAG)
                        .commit();
            }
        });
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
            final Animation showSocialAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in_social_view);

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
            final Animation hideLoginWithSocialViews = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out_social_view);
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
