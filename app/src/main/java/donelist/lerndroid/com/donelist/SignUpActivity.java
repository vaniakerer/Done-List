package donelist.lerndroid.com.donelist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import donelist.lerndroid.com.donelist.fragment.SignUpFragment;

/**
 * Created by ivan on 21.12.16.
 */

public class SignUpActivity extends SingleFragmentActivity {

    public static Intent getIntent(Context context){
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    public Fragment createFragment() {
        return SignUpFragment.newInstance();
    }
}
