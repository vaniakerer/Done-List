package donelist.lerndroid.com.donelist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import donelist.lerndroid.com.donelist.CauseActivity;
import donelist.lerndroid.com.donelist.CauseLab;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.model.Cause;

/**
 * Created by ivan on 28.11.16.
 */

public class NewCauseFragment extends Fragment {

    private static final String TAG = "NewCauseFragment";

    private static final int REQUEST_MAKE_PHOTO = 0;

    private static final String EXTRA_CAUSE_ID = "donelist.lerndriod.com.donelist.cause_id";

    private File mPhotoFile;

    @BindView(R.id.new_cause_title_ed)
    EditText mNewCauseTitleEd;
    @BindView(R.id.new_cause_description_ed)
    EditText mNewCauseDescripptionEd;
    @BindView(R.id.new_cause_photo_img)
    ImageView mNewCausePhotoImg;
    @BindView(R.id.new_cause_edit_image_fab)
    FloatingActionButton mSubmitCauseFab;

    public static NewCauseFragment newInstance() {
        return new NewCauseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_cause, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @OnClick(R.id.new_cause_edit_image_fab)
    public void onFabClick(View view) {
        if (mNewCauseTitleEd.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), R.string.must_be_filled, Toast.LENGTH_SHORT)
                    .show();
        } else {
            CauseLab causeLab = CauseLab.get(getActivity());
            causeLab.addCause(bindCause());

            Intent intent = new Intent(getActivity(), CauseActivity.class);
            intent.putExtra(EXTRA_CAUSE_ID, causeLab.getCauses().size() - 1);
            startActivity(intent);

            getActivity().finish();
        }
    }

    private Cause bindCause() {
        Cause cause = new Cause();
        Calendar c = Calendar.getInstance();

        cause.setmId(CauseLab.get(getActivity()).getCauses().size());
        cause.setTitle(mNewCauseTitleEd.getText().toString());
        cause.setDescription(mNewCauseDescripptionEd.getText().toString());
        cause.setPhotoPath(null);//TODO make photo
        cause.setDate(c.getTime());

        return cause;
    }

    @OnClick(R.id.new_cause_photo_img)
    public void makePhotoListener() {
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = captureImage.resolveActivity(getActivity().getPackageManager()) != null;

        if (canTakePhoto){
            startActivityForResult(captureImage, REQUEST_MAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        switch (requestCode){
            case REQUEST_MAKE_PHOTO:

                Uri photoUri = data.getData();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    mNewCausePhotoImg.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


}
