package donelist.lerndroid.com.donelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DatabaseReference;

import donelist.lerndroid.com.donelist.fragment.CausesFragment;

public class CausesActivity extends SingleFragmentActivity {
    private DatabaseReference database;

    @Override
    public Fragment createFragment() {
        return CausesFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      /*  Cause cause = new Cause();
        cause.setmId(1);
        cause.setPhotoPath("photoPath");
        cause.setDate(new Date());
        cause.setTitle("title");
        cause.setDescription("desc");
        List<CausesDone> dones = new ArrayList<>();
        dones.add(new CausesDone(1, "asf", "asf", new Date()));
        cause.setDones(dones);

        List<Cause> causes = new ArrayList<>();
        causes.add(cause);
        causes.add(cause);
        causes.add(cause);
        causes.add(cause);

        database.child("causes").setValue(causes);*/
    }

}


