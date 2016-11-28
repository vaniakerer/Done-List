package donelist.lerndroid.com.donelist.model;

import java.util.UUID;

/**
 * Created by ivan on 28.11.16.
 */

public class CausesDone {
    private UUID mId;
    private String mTitle;
    private String mDescription;

    public CausesDone(UUID mId, String mTitle, String mDescription) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
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

    public UUID getmId() {
        return mId;
    }
}
