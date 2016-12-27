package donelist.lerndroid.com.donelist.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ivan on 28.11.16.
 */

public class Cause {
    private String mId;//Key
    private String mTitle;
    private String mDescription;
    private String date;
    private String photoPath;
    private HashMap<String, CausesDone> mDones;

    public Cause(String mId, String mTitle, String mDescription, String date, List<CausesDone> mDones) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.date = date;
       // this.mDones = mDones;
    }

    public Cause() {
    }

    public String getId() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public List<CausesDone> getmDones() {
        if (mDones == null){
            return null;
        }
        List<CausesDone> dones = new ArrayList<>(mDones.values());
        return dones;
    }

    public void setmDones(HashMap<String, CausesDone> mDones) {
        this.mDones = mDones;
    }
}
