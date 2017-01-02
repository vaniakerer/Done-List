package donelist.lerndroid.com.donelist.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import donelist.lerndroid.com.donelist.CauseActivity;
import donelist.lerndroid.com.donelist.CauseLab;
import donelist.lerndroid.com.donelist.FirebaseDatabaseReferences;
import donelist.lerndroid.com.donelist.LoginActivity;
import donelist.lerndroid.com.donelist.NewCauseActivity;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.dialog.DonesReviewDialogFragment;
import donelist.lerndroid.com.donelist.model.Cause;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 * Created by ivan on 28.11.16.
 */

public class CausesFragment extends Fragment {
    private static final String TAG = "CausesFragment";
    private static final String ARG_CAUSE_ID = "cause_id";
    private static final String DIALOG_REVIEW = "DialogReview";
    private static final String EXTRA_CAUSE_ID = "donelist.lerndriod.com.donelist.cause_id";

    private CauseAdapter mCauseAdapter;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @BindView(R.id.cause_fragment_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.causes_fragment_new_cause_fab)
    FloatingActionButton mCreateCauseFab;
    @BindView(R.id.fragment_causes_progress)
    ProgressWheel mProgress;


    public static CausesFragment newInstance() {
        return new CausesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            Toast.makeText(getActivity(), "Hello, " + mUser.getDisplayName(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_causes, container, false);
        ButterKnife.bind(this, v);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    mCreateCauseFab.hide();
                } else if (dy < 0) {
                    mCreateCauseFab.show();
                }
            }
        });

        updateUi();
        init();

        return v;
    }

    @OnClick(R.id.causes_fragment_new_cause_fab)
    public void onFabClick() {
        startActivity(new Intent(getActivity(), NewCauseActivity.class));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cause_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.caused_menu_add_cause:
                startActivity(new Intent(getActivity(), NewCauseActivity.class));
                return true;
            case R.id.caused_menu_logout:
                mAuth.signOut();
                startActivity(LoginActivity.getIntent(getActivity()));
                CauseLab.get(getActivity()).clear();
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //ініціалізація бд
    private void init() {

        if (mUser == null) {
            startActivity(LoginActivity.getIntent(getActivity()));
            getActivity().finish();
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase
                .child(FirebaseDatabaseReferences.USERS)
                .child(mUser.getUid())
                .child(FirebaseDatabaseReferences.CAUSES)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Cause> causes = new HashMap<String, Cause>();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Cause cause = dataSnapshot1.getValue(Cause.class);
                            cause.setmId(dataSnapshot1.getKey());
                            String causeKey = dataSnapshot1.getKey();
                            causes.put(causeKey, cause);
                        }

                        CauseLab.get(getActivity()).setCauses(causes);
                        updateUi();
                        mProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public class CausesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_item_cause_title_tv)
        TextView mCauseTitle;
        @BindView(R.id.card_item_cause_description_tv)
        TextView mCauseDescription;
        @BindView(R.id.card_item_cause_date_tv)
        TextView mCauseDate;
        @BindView(R.id.card_item_cause_share_tv)
        TextView mCauseShareButton;
        @BindView(R.id.card_item_cause_preview_tv)
        TextView mCausepreviewButton;
        @BindView(R.id.card_item_watch_img)
        ImageView mWatchImg;

        private Cause mCause;

        public CausesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindCause(Cause cause) {
            //TODO seting images
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

            mCause = cause;
            mCauseTitle.setText(cause.getTitle());
            mCauseDescription.setText(cause.getDescription());
            mCauseDate.setText(cause.getDate());
        }

        @OnClick(R.id.card_item_cause_cardview)
        public void onCardClick(View v) {
            Intent intent = new Intent(getActivity(), CauseActivity.class);
            intent.putExtra(EXTRA_CAUSE_ID, mCause.getId());

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),

                    /*new Pair<View, String>(v.findViewById(R.id.card_item_cause_title_tv),
                            getString(R.string.transition_name_title)),
                    new Pair<View, String>(v.findViewById(R.id.card_item_cause_description_tv),
                            getString(R.string.transition_name_description)),
                    new Pair<View, String>(v.findViewById(R.id.card_item_cause_date_tv),
                            getString(R.string.transition_name_date)),*/
                    new Pair<View, String>(mCreateCauseFab, getString(R.string.transition_name_fab))
            );

            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
            /*startActivity(intent);*/
        }

        @OnLongClick(R.id.card_item_cause_cardview)
        public boolean onCardLongClick() {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.delete_cause)
                    .setMessage(R.string.are_you_sure_cause)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCause(mCause.getmId());
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

        @OnClick(R.id.card_item_cause_preview_tv)
        public void onPreviewClick() {
            DonesReviewDialogFragment reviewDialog = DonesReviewDialogFragment.newInstance(mCause.getId());
            reviewDialog.show(getFragmentManager(), DIALOG_REVIEW);
        }

        @OnClick(R.id.card_item_cause_share_tv)
        public void share() {
            Intent i = ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText(getCauseForShare(mCause.getmDones()))
                    .setSubject(getString(R.string.send_cause))
                    .setChooserTitle(getString(R.string.send_report))
                    .createChooserIntent();

            startActivity(i);
        }

        private String getCauseForShare(List<CausesDone> dones) {

            String result = "Title: " + mCause.getTitle() + ". \nDescription: " + mCause.getDescription() + ".\n Date: " + mCause.getDate() + "\n" + "My dones";

            for (CausesDone done : dones) {
                result += "- " + done.getmTitle() + "; \n";
            }

            return result;
        }
    }

    private void deleteCause(String key) {
        mDatabase.child(FirebaseDatabaseReferences.USERS)
                .child(mUser.getUid())
                .child(FirebaseDatabaseReferences.CAUSES)
                .child(key)
                .removeValue();
    }

    public class CauseAdapter extends RecyclerView.Adapter<CausesHolder> {
        private List<Cause> mCauses;

        public CauseAdapter(List<Cause> causes) {
            mCauses = causes;
        }

        @Override
        public CausesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.card_list_item_cause, parent, false);
            return new CausesHolder(v);
        }

        @Override
        public void onBindViewHolder(CausesHolder holder, int position) {
            Cause cause = mCauses.get(position);
            holder.bindCause(cause);
        }

        @Override
        public int getItemCount() {
            return mCauses.size();
        }

        private void setCauses(List<Cause> causes) {
            mCauses = causes;
        }

    }

    private void updateUi() {
        List<Cause> causes = CauseLab.get(getActivity()).getCauses();

        if (mCauseAdapter == null) {
            mCauseAdapter = new CauseAdapter(causes);
            mRecyclerView.setAdapter(mCauseAdapter);
        } else {
            mCauseAdapter.setCauses(causes);
            mCauseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }


}
