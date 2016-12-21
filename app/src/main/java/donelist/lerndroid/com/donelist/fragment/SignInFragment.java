package donelist.lerndroid.com.donelist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import donelist.lerndroid.com.donelist.R;

/**
 * Created by ivan on 21.12.16.
 */

public class SignInFragment extends Fragment {
    public static final String TAG = "SignInFragment";

    @BindView(R.id.fragment_sign_in_sign_in_tv)
    TextView mSignInTv;

    public static Fragment newInstance(){
        return new SignInFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sing_up, container, false);
        ButterKnife.bind(this, v);

        initListeners();

        return v;
    }

    private void initListeners(){
        mSignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .remove(fm.findFragmentByTag(TAG))
                        .commit();
            }
        });
    }



}
