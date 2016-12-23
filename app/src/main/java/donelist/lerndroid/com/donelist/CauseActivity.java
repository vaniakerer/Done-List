package donelist.lerndroid.com.donelist;

import android.support.v4.app.Fragment;

import donelist.lerndroid.com.donelist.fragment.CauseFragment;

/**
 * Created by ivan on 01.12.16.
 */

public class CauseActivity extends SingleFragmentActivity {

    private static final String EXTRA_CAUSE_ID = "donelist.lerndriod.com.donelist.cause_id";

    @Override
    public Fragment createFragment() {
        return CauseFragment.newInstance(getIntent().getIntExtra(EXTRA_CAUSE_ID, 1));
    }
}
