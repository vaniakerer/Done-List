package donelist.lerndroid.com.donelist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;
import donelist.lerndroid.com.donelist.R;

/**
 * Created by ivan on 21.12.16.
 */

public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";

    @BindView(R.id.fragment_sign_in_sign_in_tv)
    TextView mSignInTv;
    @BindView(R.id.fragment_sign_up_exit_img)
    ImageView mExitImg;
    @BindView(R.id.fragment_sign_in_user_full_name)
    EditText mUserNameTv;
    @BindView(R.id.fragment_sign_in_user_email)
    EditText mUserEmailTv;
    @BindView(R.id.fragment_sign_in_password)
    EditText mUserPasswordTv;
    @BindView(R.id.fragment_sign_in_create_button)
    Button mCreateUserBtn;
    @BindView(R.id.fragment_sign_in_progress)
    ProgressWheel mProgress;
    @BindView(R.id.fragment_sign_in_logo_img)
    ImageView mLogoImg;


    public static Fragment newInstance() {
        return new SignUpFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sing_up, container, false);
        ButterKnife.bind(this, v);



        initListeners();

        return v;
    }

    private void initListeners() {
        View.OnClickListener exitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        };

        mSignInTv.setOnClickListener(exitListener);
        mExitImg.setOnClickListener(exitListener);
        mCreateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser() {
        if (!validateForm()) {
            return;
        }

        hideLogo();

        String email = mUserEmailTv.getText().toString();
        String password = mUserPasswordTv.getText().toString();
        final String fullName = mUserNameTv.getText().toString();

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();

                            FirebaseUser user = auth.getCurrentUser();

                            Log.d(TAG, String.valueOf(user != null));
                            if (user != null) {
                                user.updateProfile(updateProfile)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d(TAG, String.valueOf(task.isSuccessful()));
                                                getActivity().finish();
                                            }
                                        });
                            }
                        } else {
                            showLogo();
                            Toast.makeText(getActivity(), R.string.something_goes_wrong, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    private boolean validateForm() {
        boolean valid = true;

        String name = mUserNameTv.getText().toString();
        if (name.length() == 0) {
            mUserNameTv.setError(getString(R.string.fielt_cannot_be_empty));
            valid = false;
        }

        String email = mUserEmailTv.getText().toString();
        if (email.length() == 0) {
            mUserEmailTv.setError(getString(R.string.fielt_cannot_be_empty));
            valid = false;
        }

        String password = mUserPasswordTv.getText().toString();

        if (password.length() < 6) {
            mUserPasswordTv.setError(getString(R.string.password_length_6_symbols));
            valid = false;
        }

        return valid;
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


}
