package donelist.lerndroid.com.donelist.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 * Created by ivan on 03.12.16.
 */

public class NewDoneDialog extends DialogFragment {

    private static final String DIALOG_DATE = "DialogDate";

    private static final String EXTRA_DONE_TITLE = "donelist.lerndriod.com.donelist.done_title";
    private static final String EXTRA_DONE_DATE = "donelist.lerndriod.com.donelist.done_date";

    private static final int REQUEST_DATE = 0;

    @BindView(R.id.new_done_title_tv)
    TextView mTitle;
    @BindView(R.id.new_done_date_btn)
    Button mDate;

    private Date date;

    public static NewDoneDialog newInstance(){
        return new NewDoneDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_new_done, null);
        ButterKnife.bind(this, view);
        updateDateButton(Calendar.getInstance().getTime());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_done)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTitle.getText().toString().length() == 0){
                            Toast.makeText(getActivity(), R.string.must_be_filled, Toast.LENGTH_SHORT)
                                    .show();
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

        String dateFormat = "EEE, MMM dd yyyy";
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
        if (getTargetFragment() == null){
            return;
        }

        CausesDone done = new CausesDone();


        Intent intent = new Intent();
        intent.putExtra(EXTRA_DONE_TITLE, mTitle.getText().toString());
        intent.putExtra(EXTRA_DONE_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
