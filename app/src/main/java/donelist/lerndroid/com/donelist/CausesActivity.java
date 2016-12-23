package donelist.lerndroid.com.donelist;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import donelist.lerndroid.com.donelist.fragment.CausesFragment;

public class CausesActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorLightPrimary)));

        return CausesFragment.newInstance();
    }

}

