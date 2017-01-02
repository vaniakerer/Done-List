package donelist.lerndroid.com.donelist;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import donelist.lerndroid.com.donelist.model.Cause;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 * Created by ivan on 28.11.16.
 */

public class CauseLab {

    private static CauseLab sCauseLab;
    private Map<String, Cause> mCauses;

    private Context mContext;

    public static CauseLab get(Context context) {
        if (sCauseLab == null) {
            sCauseLab = new CauseLab(context.getApplicationContext());
        }
        return sCauseLab;
    }

    private CauseLab(Context context) {
        mContext = context;
        mCauses = new HashMap<>();
    }

    public List<Cause> getCauses() {
        List<Cause> causes = new ArrayList<>(mCauses.values());
        return causes;
    }

    public void addCause(Cause cause) {
        List<CausesDone> dones = new ArrayList<>();
       // cause.setDones(dones);
        //mCauses.add(cause);
    }

    public void clear(){
        mCauses.clear();
    }

    public Cause getCause(String key) {
        return mCauses.get(key);
    }

    public void setCauses(Map<String, Cause> causes){
        mCauses = causes;
    }

    public void removeCause(int id) {
        mCauses.remove(id);
    }
}
