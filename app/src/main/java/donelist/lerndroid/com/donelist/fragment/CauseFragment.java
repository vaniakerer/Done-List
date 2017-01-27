package donelist.lerndroid.com.donelist.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import donelist.lerndroid.com.donelist.CauseLab;
import donelist.lerndroid.com.donelist.FirebaseDatabaseReferences;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.dialog.EditCauseDialog;
import donelist.lerndroid.com.donelist.dialog.NewDoneDialog;
import donelist.lerndroid.com.donelist.model.Cause;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 * Created by ivan on 01.12.16.
 */

public class CauseFragment extends Fragment {
    private static final String TAG = "CauseFragment";

    private static final String ARG_CAUSE_ID = "cause_id";

    private static final int REQUEST_NEW_DONE = 1;
    private static final int REQUEST_EDIT_CAUSE = 1101;

    private static final String EXTRA_TITLE = "cause_title";
    private static final String EXTRA_DESCRIPTION = "cause_description";

    private DatabaseReference mDonesReference;
    private DatabaseReference mCausesReference;

    private Cause mCause;
    private String mCauseKey;

    private CauseDoneAdapter mAdapter;

    @BindView(R.id.cause_fragment_cause_title_tv)
    TextView mTitle;
    @BindView(R.id.cause_fragment_cause_description_tv)
    TextView mDescription;
    @BindView(R.id.cause_fragment_cause_date_tv)
    TextView mDate;
    @BindView(R.id.cause_fragment_no_dones_tv)
    TextView mNoDones;
    @BindView(R.id.cause_fragment_new_done_fab)
    FloatingActionButton mAddDoneFab;
    @BindView(R.id.cause_fragment_causes_image_letter)
    TextView mCauseLatter;
    @BindView(R.id.cause_fragment_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_cause_edit_cause)
    ImageView mEditCauseImage;

    public static CauseFragment newInstance(String causeId) {
        Bundle args = new Bundle();
        args.putString(ARG_CAUSE_ID, causeId);
        CauseFragment fragment = new CauseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String causeKey = getArguments().getString(ARG_CAUSE_ID);
        mCauseKey = causeKey;
        mCause = CauseLab.get(getActivity()).getCause(mCauseKey);

        //init firebase database
        mCausesReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(FirebaseDatabaseReferences.USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseDatabaseReferences.CAUSES)
                .child(mCauseKey);

        mDonesReference = mCausesReference
                .child(FirebaseDatabaseReferences.DONES);

        mDonesReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<CausesDone> dones = new ArrayList<>();
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            dones.add(item.getValue(CausesDone.class));
                        }
                        if (mAdapter != null) {
                            mAdapter.setDones(dones);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cause, container, false);
        ButterKnife.bind(this, v);
        initUi();

        return v;
    }

    private void initUi() {
        if (mCause == null) {
            onDestroy();
            return;
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCauseLatter.setText(String.valueOf(mCause.getmTitle().charAt(0)));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    mAddDoneFab.hide();
                } else if (dy < 0) {
                    mAddDoneFab.show();
                }
            }
        });

        mAdapter = new CauseDoneAdapter(mCause.getmDones());
        mRecyclerView.setAdapter(mAdapter);

        mTitle.setText(mCause.getTitle());
        mDescription.setText(mCause.getDescription());
        mDate.setText(mCause.getDate());

            /*Picasso.with(getActivity())
                    .load("https://www.simplifiedcoding.net/wp-content/uploads/2015/10/advertise.png")
                    .error(R.drawable.circular_image_view_background)
                    .into(mImage);*/
        if (mCause.getmDones() == null) {
            mRecyclerView.setVisibility(View.GONE);
            mNoDones.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.cause_fragment_new_done_fab)
    public void onFabClick() {
        NewDoneDialog dialog = NewDoneDialog.newInstance(mCauseKey);
        dialog.setTargetFragment(this, REQUEST_NEW_DONE);

        dialog.show(getActivity().getSupportFragmentManager(), NewDoneDialog.TAG);
    }

    @OnClick(R.id.fragment_cause_edit_cause)
    public void onEditCauseClick() {
        Log.d("ASFASFAFsssssa", "ASccc");
        EditCauseDialog dialog = EditCauseDialog.newInstance(mCauseKey, mCause.getmTitle(), mCause.getDescription());
        dialog.setTargetFragment(this, REQUEST_EDIT_CAUSE);

        dialog.show(getActivity().getSupportFragmentManager(), EditCauseDialog.TAG);
    }

    public class CauseDonesViewHolder extends RecyclerView.ViewHolder {

        private CausesDone mCausesDone;

        @BindView(R.id.cause_done_list_item_title_tv)
        TextView mTitle;
        @BindView(R.id.cause_done_list_item_date_tv)
        TextView mDate;
        @BindView(R.id.cause_done_list_item_status_img)
        ImageView mStatusImage;


        public CauseDonesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(CausesDone causesDone) {
            mCausesDone = causesDone;
            mTitle.setText(mCausesDone.getmTitle());
            mDate.setText(mCausesDone.getmDoneDate());
            Log.d(TAG, causesDone.toString());
        }

        @OnLongClick(R.id.list_item_causes_done)
        public boolean onDoneItemLongClick() {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.delete_cause)
                    .setMessage(R.string.are_you_sure_done)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteDone(mCausesDone.getmId());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return true;
        }

        @OnClick(R.id.list_item_causes_done)
        public void onClik() {
            new AlertDialog.Builder(getActivity())
                    .setMessage("\n" + mCausesDone.getmTitle())
                    .create()
                    .show();
        }
    }

    /**
     * removing causes done by key
     *
     * @param key - dones identity key in database
     */
    private void deleteDone(String key) {
        mDonesReference
                .child(key)
                .removeValue();
    }

    /**
     * removing current cause
     */
    private void deleteCause() {
        mCausesReference.removeValue();
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_cause_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail_cause_menu_delete_cause:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.delete_cause)
                        .setMessage(R.string.are_you_sure_cause)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCause();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class CauseDoneAdapter extends RecyclerView.Adapter<CauseDonesViewHolder> {

        private List<CausesDone> mCausesDones;

        public CauseDoneAdapter(List<CausesDone> causesDones) {
            if (causesDones != null) {
                mCausesDones = causesDones;
            } else {
                mCausesDones = new ArrayList<>();
            }
        }

        @Override
        public CauseDonesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.causes_done_list_item, parent, false);
            return new CauseDonesViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CauseDonesViewHolder holder, int position) {
            CausesDone causesDone = mCausesDones.get(position);
            holder.bindView(causesDone);
        }

        @Override
        public int getItemCount() {
            return mCausesDones.size();
        }

        public void addDone(CausesDone done) {
            mCausesDones.add(done);
            notifyItemInserted(mCausesDones.size() - 1);
        }

        public void setDones(List<CausesDone> dones) {
            if (!dones.isEmpty()) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mNoDones.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mNoDones.setVisibility(View.VISIBLE);
            }

            mCausesDones = dones;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_EDIT_CAUSE:
                if (resultCode == Activity.RESULT_OK){
                    String title = data.getStringExtra(EXTRA_TITLE);
                    String description = data.getStringExtra(EXTRA_DESCRIPTION);
                    mTitle.setText(title);
                    mDescription.setText(description);
                }
                break;
        }
    }
}
