package donelist.lerndroid.com.donelist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import donelist.lerndroid.com.donelist.R;

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

    public static final Fragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        initListeners();
        return v;
    }

    private void initListeners(){
        //login views hide animation
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

        mLoginWithSocialTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginViewsLin.startAnimation(hideLoginAnimation);
                mLoginWithSocialLin.startAnimation(showSocialAnimation);
                mLoginWithSocialLin.setVisibility(View.VISIBLE);
            }
        });

        mSignUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

}
