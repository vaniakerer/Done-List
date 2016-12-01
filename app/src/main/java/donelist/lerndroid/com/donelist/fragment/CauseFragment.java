package donelist.lerndroid.com.donelist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import donelist.lerndroid.com.donelist.CauseLab;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.model.Cause;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 * Created by ivan on 01.12.16.
 */

public class CauseFragment extends Fragment {
    private static final String TAG = "CauseFragment";

    private static final String ARG_CAUSE_ID = "cause_id";

    private Cause mCause;
    private CauseDoneAdapter mAdapter;

    @BindView(R.id.cause_fragment_cause_title_tv)
    TextView mTitle;
    @BindView(R.id.cause_fragment_cause_description_tv)
    TextView mDescription;
    @BindView(R.id.cause_fragment_cause_date_tv)
    TextView mDate;
    @BindView(R.id.cause_fragment_cause_image)
    ImageView mImage;
    @BindView(R.id.cause_fragment_make_photo_button)
    ImageButton mMakePhotoButton;
    @BindView(R.id.cause_fragment_recycler_view)
     RecyclerView mRecyclerView;

    public static CauseFragment newInstance(int causeId){
        Bundle args = new Bundle();
        args.putInt(ARG_CAUSE_ID, causeId);
        CauseFragment fragment = new CauseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cause, container, false);
        ButterKnife.bind(this, v);
        initUi();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCause = CauseLab.get(getActivity()).getCause(getArguments().getInt(ARG_CAUSE_ID));
    }

    private void initUi(){
        if (mCause == null){
            this.onDestroy();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CauseDoneAdapter(mCause.getDones());
        mRecyclerView.setAdapter(mAdapter);

        String dateFormat = "EEE, MMM dd";
        mTitle.setText(mCause.getTitle());
        mDescription.setText(mCause.getDescription());
        mDate.setText(DateFormat.format(dateFormat, mCause.getDate()).toString());
    }


    public class CauseDonesViewHolder extends RecyclerView.ViewHolder{

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

        public void bindView(CausesDone causesDone){
            mCausesDone = causesDone;
            mTitle.setText(mCausesDone.getTitle());
            mDate.setText(mCausesDone.getDoneDate());
            mStatusImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.circular_image_view_background));
        }
    }

    public class CauseDoneAdapter extends RecyclerView.Adapter<CauseDonesViewHolder>{

        private List<CausesDone> mCausesDones;

        public CauseDoneAdapter(List<CausesDone> causesDones){
            mCausesDones = causesDones;
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
    }

}
