package donelist.lerndroid.com.donelist.model;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by ivan on 28.11.16.
 */

public class CausesDone {
    private int mId;
    private String mTitle;
    private String mDescription;
    private Date mDoneDate;
    private int status;

    public CausesDone(int mId, String mTitle, String mDescription, Date mDoneDate) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mDoneDate = mDoneDate;
        this.status = 0;
    }

    public CausesDone() {
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getmId() {
        return mId;
    }

    public String getDoneDate() {
        String dateFormat = "EEE, MMM dd";
        return DateFormat.format(dateFormat, this.mDoneDate).toString();
    }

    public void setDoneDate(Date mDoneDate) {
        this.mDoneDate = mDoneDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
