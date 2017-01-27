package donelist.lerndroid.com.donelist.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import donelist.lerndroid.com.donelist.FirebaseDatabaseReferences;
import donelist.lerndroid.com.donelist.R;

/**
 * Created by ivan on 27.01.17.
 */

public class EditCauseDialog extends DialogFragment {
    public static final String TAG = "EditCauseDialog";

    private static final String ARG_CAUSE_KEY = "cause_key";
    private static final String ARG_CAUSE_TITLE = "cause_title";
    private static final String ARG_CAUSE_DESCRIPTION = "cause_description";

    private static final String EXTRA_TITLE = "cause_title";
    private static final String EXTRA_DESCRIPTION = "cause_description";


    private String mCauseKey;
    private String mCauseTitle;
    private String mCauseDescription;

    private DatabaseReference mDatabase;

    @BindView(R.id.dialog_edit_cause_title_ed)
    EditText mCauseTitleEd;
    @BindView(R.id.dialog_edit_cause_description_ed)
    EditText mCauseDescriptionEd;
    @BindView(R.id.dialog_edit_cause_submit_button)
    Button mSubmitEditingButton;

    public static final EditCauseDialog newInstance(String causeKey, String causeTitle, String causeDescription){
        Bundle args = new Bundle();
        args.putString(ARG_CAUSE_KEY, causeKey);
        args.putString(ARG_CAUSE_TITLE, causeTitle);
        args.putString(ARG_CAUSE_DESCRIPTION, causeDescription);
        EditCauseDialog dialog = new EditCauseDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCauseKey = getArguments().getString(ARG_CAUSE_KEY);
        mCauseTitle = getArguments().getString(ARG_CAUSE_TITLE);
        mCauseDescription = getArguments().getString(ARG_CAUSE_DESCRIPTION);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_edit_cause, null);

        ButterKnife.bind(this, v);

        initUi();

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

    private void initUi(){
        mCauseTitleEd.setText(mCauseTitle);
        mCauseDescriptionEd.setText(mCauseDescription);

        mSubmitEditingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitChanges();
            }
        });
    }

    private void submitChanges(){
        final String title = mCauseTitleEd.getText().toString();
        final String  description = mCauseDescriptionEd.getText().toString();

        if (title.length() == 0 || description.length() == 0){
            Toast.makeText(getActivity(), getString(R.string.fielt_cannot_be_empty), Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!isNetworkAvailable()){
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //TODO start progress

        Map<String, Object> clildUpdates = new HashMap<>();

        clildUpdates.put(FirebaseDatabaseReferences.TITLE, title);
        clildUpdates.put(FirebaseDatabaseReferences.DESCRIPTION, description);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(FirebaseDatabaseReferences.USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseDatabaseReferences.CAUSES)
                .child(mCauseKey)
                .updateChildren(clildUpdates)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getActivity(), getString(R.string.something_goes_wrong), Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        sendResult(Activity.RESULT_OK, title, description);
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), getString(R.string.something_goes_wrong), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void sendResult(int resultCode, String title, String description){
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        dismiss();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }
}
