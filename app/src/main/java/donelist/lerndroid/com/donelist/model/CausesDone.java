package donelist.lerndroid.com.donelist.model;

/**
 * Created by ivan on 28.11.16.
 */

public class CausesDone {
    private int mId;
    private String mTitle;
    private String mDescription;
    private String mDoneDate;
    private int status;

    public CausesDone(int mId, String mTitle, String mDescription, String mDoneDate) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mDoneDate = mDoneDate;
        this.status = 0;
    }

    public CausesDone(String mTitle, String mDoneDate, int status) {
        this.mDoneDate = mDoneDate;
        this.status = status;
        this.mTitle = mTitle;
    }

    public CausesDone() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmDoneDate() {
        return mDoneDate;
    }

    public void setmDoneDate(String mDoneDate) {
        this.mDoneDate = mDoneDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getmTitle() + " " + this.getmDescription() + " " + this.getmDoneDate() + " " +  this.getmId();
    }



}
