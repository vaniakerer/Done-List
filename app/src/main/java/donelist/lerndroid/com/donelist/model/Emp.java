package donelist.lerndroid.com.donelist.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 10.12.16.
 */

public class Emp {
    private String firstName;
    private String lastName;

    public Emp() {

    }

    public Emp(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName", firstName);
        result.put("lastName", lastName);

        return result;
    }

}
