package donelist.lerndroid.com.donelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import donelist.lerndroid.com.donelist.fragment.CausesFragment;
import donelist.lerndroid.com.donelist.model.Emp;

public class CausesActivity extends SingleFragmentActivity {
    private DatabaseReference database;
    @Override
    public Fragment createFragment() {
        return CausesFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance().getReference();

        database.child("employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Emp> emps = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Emp emp = dataSnapshot1.getValue(Emp.class);
                    emps.add(emp);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*Cause cause = new Cause();
        cause.setmId(1);
        cause.setPhotoPath("photoPath");
        cause.setDate(new Date());
        cause.setTitle("title");
        cause.setDescription("desc");
        List<CausesDone> dones = new ArrayList<>();
        dones.add(new CausesDone(1, "asf", "asf", new Date()));
        cause.setDones(dones);*/
    }

    private void weriteNewData(){
        String key = database.child("employees").push().getKey();
        Emp emp = new Emp("testName", "testLastName");
        Map<String, Object> empValues = emp.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/employees/" + key, empValues);
        database.updateChildren(childUpdates);
    }
}


