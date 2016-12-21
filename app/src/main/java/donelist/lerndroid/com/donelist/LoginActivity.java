package donelist.lerndroid.com.donelist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import donelist.lerndroid.com.donelist.fragment.LoginFragment;

/**
 * Created by ivan on 20.12.16.
 */

public class LoginActivity extends SingleFragmentActivity {
    public static final Intent getIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();


    }
}
