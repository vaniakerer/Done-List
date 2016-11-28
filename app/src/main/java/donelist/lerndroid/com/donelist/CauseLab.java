package donelist.lerndroid.com.donelist;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import donelist.lerndroid.com.donelist.model.Cause;

/**
 * Created by ivan on 28.11.16.
 */

public class CauseLab {
    private static CauseLab sCauseLab;
    private List<Cause> mCauses;

    private Context mContext;

    public static CauseLab get(Context context){
        if (sCauseLab == null){
            sCauseLab = new CauseLab(context.getApplicationContext());
        }
        return sCauseLab;
    }

    private CauseLab(Context context){
        mContext = context;
        mCauses = new ArrayList<>();
    }

    public List<Cause> getCauses(){
      /*  ArrayList<Cause> causes = new ArrayList<>();
        Date date = new Date();

        Calendar c = Calendar.getInstance();

        causes.add(new Cause(1,"Caused title", "Thanks, this has been done but interestingly the...", c.getTime(), new ArrayList<CausesDone>()));*/
        return mCauses;
    }

    public void addCause(Cause cause){
        mCauses.add(cause);
    }

    public void removeCause(int id){
        mCauses.remove(id);
    }
}
