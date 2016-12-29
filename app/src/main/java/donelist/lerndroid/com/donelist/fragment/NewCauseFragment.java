package donelist.lerndroid.com.donelist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import donelist.lerndroid.com.donelist.CauseActivity;
import donelist.lerndroid.com.donelist.CauseLab;
import donelist.lerndroid.com.donelist.FirebaseDatabaseReferences;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.model.Cause;

/**
 * Created by ivan on 28.11.16.
 */

public class NewCauseFragment extends Fragment {

    private static final String TAG = "NewCauseFragment";

    private static final int REQUEST_MAKE_PHOTO = 0;

    private static final String EXTRA_CAUSE_ID = "donelist.lerndriod.com.donelist.cause_id";

    private static final String PHOTO_URI = "photoUri";

    private Uri mPhotoUri;

    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    @BindView(R.id.new_cause_title_ed)
    EditText mNewCauseTitleEd;
    @BindView(R.id.new_cause_description_ed)
    EditText mNewCauseDescripptionEd;
    @BindView(R.id.new_cause_photo_img)
    ImageView mNewCausePhotoImg;
    @BindView(R.id.new_cause_edit_image_fab)
    FloatingActionButton mSubmitCauseFab;
    @BindView(R.id.fragment_new_cause_progress)
    ProgressWheel mProgress;

    public static NewCauseFragment newInstance() {
        return new NewCauseFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            Toast.makeText(getActivity(), getString(R.string.you_are_not_authorized), Toast.LENGTH_SHORT)
                    .show();
            onDestroy();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_cause, container, false);
        ButterKnife.bind(this, v);

        if (savedInstanceState != null) {
            mPhotoUri = savedInstanceState.getParcelable(PHOTO_URI);
            Bitmap photo = null;

            if (mPhotoUri != null) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mPhotoUri);
                    mNewCausePhotoImg.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return v;
    }

    @OnClick(R.id.new_cause_edit_image_fab)
    public void onFabClick(View view) {
        if (mNewCauseTitleEd.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), R.string.must_be_filled, Toast.LENGTH_SHORT)
                    .show();
        } else {

            mProgress.setVisibility(View.VISIBLE);

            final String key = mDatabase
                    .child(FirebaseDatabaseReferences.USERS)
                    .child(mUser.getUid())
                    .child(FirebaseDatabaseReferences.CAUSES)
                    .push()
                    .getKey();

            final Cause cause = bindCause(key);

            mDatabase
                    .child(FirebaseDatabaseReferences.USERS)
                    .child(mUser.getUid())
                    .child(FirebaseDatabaseReferences.CAUSES)
                    .child(key)
                    .setValue(cause)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                CauseLab causeLab = CauseLab.get(getActivity());
                                causeLab.addCause(cause);

                                getActivity().finish();

                                Intent intent = new Intent(getActivity(), CauseActivity.class);
                                intent.putExtra(EXTRA_CAUSE_ID, key);


                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                        new Pair<View, String>(mSubmitCauseFab, getString(R.string.transition_name_fab))
                                );
                                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                            } else {
                                Toast.makeText(getActivity(), R.string.something_goes_wrong, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }
    }

    private Cause bindCause(String key) {
        Cause cause = new Cause();
        Calendar c = Calendar.getInstance();
        String dateFormat = "MMM, dd yyyy";

        cause.setmId(key);
        cause.setTitle(mNewCauseTitleEd.getText().toString());
        cause.setDescription(mNewCauseDescripptionEd.getText().toString());
        cause.setPhotoPath(null);//TODO make photo
        cause.setDate(DateFormat.format(dateFormat, c.getTime()).toString());

        if (mPhotoUri != null) {
            cause.setPhotoPath(mPhotoUri.getPath());
        }
        return cause;
    }

    @OnClick(R.id.new_cause_photo_img)
    public void makePhotoListener() {
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = captureImage.resolveActivity(getActivity().getPackageManager()) != null;

        if (canTakePhoto) {
            startActivityForResult(captureImage, REQUEST_MAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_MAKE_PHOTO:
                mPhotoUri = data.getData();
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mPhotoUri);

                    mNewCausePhotoImg.setImageBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PHOTO_URI, mPhotoUri);
        super.onSaveInstanceState(outState);
    }
}
