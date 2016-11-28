package donelist.lerndroid.com.donelist;

import android.support.v4.app.Fragment;

import donelist.lerndroid.com.donelist.fragment.NewCauseFragment;

/**
 * Created by ivan on 28.11.16.
 */

public class NewCauseActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return NewCauseFragment.newInstance();
    }
}
