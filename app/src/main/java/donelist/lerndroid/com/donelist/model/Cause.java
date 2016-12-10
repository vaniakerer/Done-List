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
    private String photoPath;
    private List<CausesDone> mDones;

    public Cause(int mId, String mTitle, String mDescription, Date date, List<CausesDone> mDones) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.date = date;
        this.mDones = mDones;
    }

    public Cause() {
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<CausesDone> getDones() {
        return mDones;
    }

    public void setDones(List<CausesDone> mDones) {
        this.mDones = mDones;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
