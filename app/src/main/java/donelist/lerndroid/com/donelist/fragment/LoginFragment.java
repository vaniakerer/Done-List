package donelist.lerndroid.com.donelist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import donelist.lerndroid.com.donelist.R;

/**
 * Created by ivan on 20.12.16.
 */

public class LoginFragment extends Fragment {

    public static final Fragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        return v;
    }

}
