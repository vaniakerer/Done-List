package donelist.lerndroid.com.donelist.model;

import java.util.List;

/**
 * Created by ivan on 11.12.16.
 */

public class User {
    private List<Cause> causes;

    public User(List<Cause> causes) {
        this.causes = causes;
    }

    public User() {
    }

    public List<Cause> getCauses() {
        return causes;
    }

    public void setCauses(List<Cause> causes) {
        this.causes = causes;
    }
}
