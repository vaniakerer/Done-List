package donelist.lerndroid.com.donelist;

import android.support.v4.app.Fragment;

import donelist.lerndroid.com.donelist.fragment.CausesFragment;

public class CausesActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return CausesFragment.newInstance();
    }
}
