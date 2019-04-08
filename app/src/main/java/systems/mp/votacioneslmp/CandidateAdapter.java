package systems.mp.votacioneslmp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder> {

    private List<Candidate> mCandidates;
    private int mMode;
    private View.OnClickListener mClickListener;
    private Context mContext;

    public CandidateAdapter(List<Candidate> candidates, int mode) {
        mCandidates = candidates;
        mMode = mode;
    }

    public void deleteItem(int position) {
        Toast.makeText(mContext, mContext.getString(R.string.candidato_eliminado) + mCandidates.get(position).getName(), Toast.LENGTH_SHORT).show();
        mCandidates.remove(position);
        notifyItemRemoved(position);
    }

    class CandidateViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView votes;
        TextView lbl;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.candidate_name_textview);
            votes = itemView.findViewById(R.id.candidate_votes_textview);
            lbl = itemView.findViewById(R.id.candidates_votes_lbl);
        }
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.candidate_item,
                viewGroup, false);
        CandidateViewHolder holder = new CandidateViewHolder(v);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(view);
            }
        });
        if(mContext == null) {
            mContext = v.getContext();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder candidateViewHolder, int i) {
        candidateViewHolder.name.setText(mCandidates.get(i).getName());
        // Hide vote count from list if on Voting Activity
        if(mMode == 0) {
            candidateViewHolder.votes.setVisibility(View.GONE);
            candidateViewHolder.lbl.setVisibility(View.GONE);
        }else{
            candidateViewHolder.votes.setText(String.valueOf(mCandidates.get(i).getVoteCount()));
        }
    }

    @Override
    public int getItemCount() {
        return mCandidates.size();
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

    public Context getContext(){
        return mContext;
    }

}
