package donelist.lerndroid.com.donelist.model;

/**
 * Created by ivan on 28.11.16.
 */

public class CausesDone {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mDoneDate;
    private int status;

    public CausesDone(String mId, String mTitle, String mDescription, String mDoneDate, int status) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mDoneDate = mDoneDate;
        this.status = status;
    }

    public CausesDone(String id, String mTitle, String mDoneDate, int status) {
        this.mDoneDate = mDoneDate;
        this.status = status;
        this.mTitle = mTitle;
        this.mId = id;
    }

    public CausesDone() {
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
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
