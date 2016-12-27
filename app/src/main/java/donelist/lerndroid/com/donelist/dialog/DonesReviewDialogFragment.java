package donelist.lerndroid.com.donelist.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import donelist.lerndroid.com.donelist.CauseLab;
import donelist.lerndroid.com.donelist.R;
import donelist.lerndroid.com.donelist.model.Cause;
import donelist.lerndroid.com.donelist.model.CausesDone;

/**
 * Created by ivan on 28.11.16.
 */

public class DonesReviewDialogFragment extends DialogFragment {
    public static final String ARG_CAUSE_ID = "cause_id";

    private Cause mCause;

    @BindView(R.id.review_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review, container, false);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this, v);
        initUi();
        return v;
    }


    public static DonesReviewDialogFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARG_CAUSE_ID, id);

        DonesReviewDialogFragment dialog = new DonesReviewDialogFragment();
        dialog.setArguments(args);
        return dialog;
    }

    private void initUi() {
        int causeId = getArguments().getInt(ARG_CAUSE_ID);
        mCause = CauseLab.get(getActivity()).getCause(causeId);
       // DoneAdapter adapter = new DoneAdapter(mCause.getDones());

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
      //  mRecyclerView.setAdapter(adapter);
    }

    public class DonesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_item_title)
        TextView mTitle;
        @BindView(R.id.review_item_description)
        TextView mDescription;
        @BindView(R.id.review_item_date)
        TextView mDate;

        CausesDone mDone;

        public DonesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindDone(CausesDone done) {
            mDone = done;

            mTitle.setText(mDone.getmTitle());
            mDescription.setText(mDone.getmDescription());
            mDate.setText(mDone.getmDoneDate());
        }
    }

    private class DoneAdapter extends RecyclerView.Adapter<DonesHolder> {

        private List<CausesDone> mDones;

        DoneAdapter(List<CausesDone> dones) {
            if (dones != null){
                mDones = dones;
            }else{
                mDones = new ArrayList<>();
            }
        }

        @Override
        public DonesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.review_done_list_item, parent, false);
            return new DonesHolder(v);
        }

        @Override
        public void onBindViewHolder(DonesHolder holder, int position) {
            CausesDone done = mDones.get(position);
            holder.bindDone(done);
        }

        @Override
        public int getItemCount() {
            return mDones.size();
        }

        public void setDones(List<CausesDone> dones) {
            mDones = dones;
        }
    }
}
