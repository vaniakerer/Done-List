package donelist.lerndroid.com.donelist.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import donelist.lerndroid.com.donelist.FirebaseDatabaseReferences;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 *
 */

public class NewDoneDialog extends DialogFragment {

    private static final String DIALOG_DATE = "DialogDate";

    private static final String EXTRA_DONE_TITLE = "donelist.lerndriod.com.donelist.done_title";
    private static final String EXTRA_DONE_DATE = "donelist.lerndriod.com.donelist.done_date";

    private static final int REQUEST_DATE = 0;

    private static final  String ARG_CAUSE_ID = "causeId";

    @BindView(R.id.new_done_title_tv)
    TextView mTitle;
    @BindView(R.id.new_done_date_btn)
    Button mDate;

    private Date date;
    private String mCauseId;

    private FirebaseUser mUser;

    public static NewDoneDialog newInstance(String causeKey){
        Bundle args = new Bundle();
        args.putString(ARG_CAUSE_ID, causeKey);

        NewDoneDialog dialog = new NewDoneDialog();
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null){
            Toast.makeText(getActivity(), R.string.you_are_not_authorized, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_new_done, null);
        ButterKnife.bind(this, view);

        mCauseId = getArguments().getString(ARG_CAUSE_ID);

        if (mCauseId == null){
            dismiss();
        }

        updateDateButton(Calendar.getInstance().getTime());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTitle.getText().toString().length() == 0){
                            Toast.makeText(getActivity(), R.string.must_be_filled, Toast.LENGTH_SHORT)
                                    .show();
                            mTitle.setError(getString(R.string.fielt_cannot_be_empty));
                        }else {
                            sendResult(Activity.RESULT_OK);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }

    private void updateDateButton(Date newDate){
        date = newDate;

        String dateFormat = "MMM, dd yyyy";
        mDate.setText(DateFormat.format(dateFormat, date));
    }

    @OnClick(R.id.new_done_date_btn)
    public void onDateButtonClick(){
        FragmentManager fm = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(date);
        dialog.setTargetFragment(this, REQUEST_DATE);
        dialog.show(fm, DIALOG_DATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((resultCode != Activity.RESULT_OK) || (data == null)){
            return;
        }

        switch (requestCode){
            case REQUEST_DATE:
                date = (Date) data.getExtras().getSerializable(DatePickerFragment.EXTRA_DATE);
                updateDateButton(date);
                break;
        }
    }

    private void sendResult(int resultCode){
        if (getTargetFragment() == null || !isNetworkAvailable()){
            return;
        }
        //TODO Progressbar
        String title = mTitle.getText().toString();
        String date = mDate.getText().toString();
        int status = 0; //by default

        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDatabaseReferences.USERS)
                .child(mUser.getUid())
                .child(FirebaseDatabaseReferences.CAUSES)
                .child(mCauseId)
                .child(FirebaseDatabaseReferences.DONES);
        database
                .push()
                .setValue(new CausesDone(title, date, status))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });


        Intent intent = new Intent();
        intent.putExtra(EXTRA_DONE_TITLE, mTitle.getText().toString());
        intent.putExtra(EXTRA_DONE_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }
}
