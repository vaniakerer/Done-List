package donelist.lerndroid.com.donelist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import donelist.lerndroid.com.donelist.CauseLab;
import donelist.lerndroid.com.donelist.NewCauseActivity;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.model.Cause;

/**
 * Created by ivan on 28.11.16.
 */

public class CausesFragment extends Fragment {
    private static final String TAG = "CausesFragment";

    private CauseAdapter mCauseAdapter;

    @BindView(R.id.cause_fragment_recycler_view) RecyclerView mRecyclerView;


    public static CausesFragment newInstance(){
        return new CausesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_causes, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUi();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cause_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.caused_menu_add_cause:
                startActivity(new Intent(getActivity(), NewCauseActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public class CausesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.card_item_cause_cardview) CardView mCauseCard;
        @BindView(R.id.card_item_cause_image_imv) ImageView mCauseImage;
        @BindView(R.id.card_item_cause_title_tv) TextView mCauseTitle;
        @BindView(R.id.card_item_cause_description_tv) TextView mCauseDescription;
        @BindView(R.id.card_item_cause_date_tv) TextView mCauseDate;
        @BindView(R.id.card_item_cause_share_tv) TextView mCauseShareButton;
        @BindView(R.id.card_item_cause_preview_tv) TextView mCausepreviewButton;
        private Cause mCause;

        public CausesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindCause(Cause cause){
            //TODO seting images
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

            mCause = cause;
            mCauseTitle.setText(cause.getTitle());
            mCauseDescription.setText(cause.getDescription());
            mCauseDate.setText(dateFormat.format(cause.getDate()));
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Card Clicked", Toast.LENGTH_SHORT)
                    .show();
        }

        @OnClick(R.id.card_item_cause_share_tv)
        public void share(){
            Intent i = ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText(getCauseForShare())
                    .setSubject(getString(R.string.send_cause))
                    .setChooserTitle(getString(R.string.send_report))
                    .createChooserIntent();

            startActivity(i);
        }

        private String getCauseForShare(){
            String dateFormat = "EEE, MMM dd";
            String dateString = DateFormat.format(dateFormat, mCause.getDate()).toString();

            return mCause.getTitle() + ":" + mCause.getDescription() + "\n" + dateString;
        }
    }

    public class CauseAdapter extends RecyclerView.Adapter<CausesHolder>{
        private List<Cause> mCauses;

        public CauseAdapter(List<Cause> causes){
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

        private void setCauses(List<Cause> causes){
            mCauses = causes;
        }

    }

    private void updateUi(){
        List<Cause> causes = CauseLab.get(getActivity()).getCauses();

        if (mCauseAdapter == null){
            mCauseAdapter = new CauseAdapter(causes);
            mRecyclerView.setAdapter(mCauseAdapter);
        }else {
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
