package donelist.lerndroid.com.donelist;

import android.support.v4.app.Fragment;

import donelist.lerndroid.com.donelist.fragment.LoginFragment;

/**
 * Created by ivan on 20.12.16.
 */

public class LoginActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}
