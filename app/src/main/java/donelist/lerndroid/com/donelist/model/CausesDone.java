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
        return mDoneDate;
    }

    public void setDoneDate(String mDoneDate) {
        this.mDoneDate = mDoneDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getmDoneDate() {
        return mDoneDate;
    }

    public void setmDoneDate(String mDoneDate) {
        this.mDoneDate = mDoneDate;
    }

    @Override
    public String toString() {
        return this.getTitle() + " " + this.getmDescription() + " " + this.getDoneDate() + " " +  this.getmId();
    }

    
}
