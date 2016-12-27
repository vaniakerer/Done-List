package donelist.lerndroid.com.donelist;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import donelist.lerndroid.com.donelist.model.Cause;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 * Created by ivan on 28.11.16.
 */

public class CauseLab {

    private static CauseLab sCauseLab;
    private List<Cause> mCauses;

    private Context mContext;

    public static CauseLab get(Context context) {
        if (sCauseLab == null) {
            sCauseLab = new CauseLab(context.getApplicationContext());
        }
        return sCauseLab;
    }



    private CauseLab(Context context) {
        mContext = context;
        mCauses = new ArrayList<>();
    }

    public List<Cause> getCauses() {

        return mCauses;
    }

    public void addCause(Cause cause) {
        List<CausesDone> dones = new ArrayList<>();
       // cause.setDones(dones);
        mCauses.add(cause);
    }

    public Cause getCause(String id) {
        for (Cause item : mCauses) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public void setCauses(List<Cause> causes){
        mCauses = causes;
    }

    public void removeCause(int id) {
        mCauses.remove(id);
    }
}
