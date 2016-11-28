package donelist.lerndroid.com.donelist;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import donelist.lerndroid.com.donelist.model.Cause;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 * Created by ivan on 28.11.16.
 */

public class CauseLab {
    private static CauseLab sCauseLab;

    private Context mContext;

    public static CauseLab get(Context context){
        if (sCauseLab == null){
            sCauseLab = new CauseLab(context);
        }
        return sCauseLab;
    }

    private CauseLab(Context context){
        mContext = context;
    }

    public List<Cause> getCauses(){
        ArrayList<Cause> causes = new ArrayList<>();
        Date date = new Date();

        Calendar c = Calendar.getInstance();

        causes.add(new Cause(1,"Caused title", "Thanks, this has been done but interestingly the...", c.getTime(), new ArrayList<CausesDone>()));
        return causes;
    }
}
