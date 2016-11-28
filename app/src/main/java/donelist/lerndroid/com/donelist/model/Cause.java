package donelist.lerndroid.com.donelist.model;

import java.util.Date;
import java.util.List;

/**
 * Created by ivan on 28.11.16.
 */

public class Cause {
    private int mId;
    private String mTitle;
    private String mDescription;
    private Date date;
    private List<CausesDone> mDones;

    public Cause(int mId, String mTitle, String mDescription, Date date, List<CausesDone> mDones) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.date = date;
        this.mDones = mDones;
    }

    public int getmId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<CausesDone> getmDones() {
        return mDones;
    }

    public void setmDones(List<CausesDone> mDones) {
        this.mDones = mDones;
    }
}
