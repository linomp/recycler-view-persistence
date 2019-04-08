package systems.mp.votacioneslmp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class VotingActivity extends AppCompatActivity {

    private static final String TAG = "VOTING";
    private static final int MODE = 0;
    private List<Candidate> mCandidates;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCandidates = CandidatesManager.getAllCandidates(this);
        Log.d(TAG, mCandidates.toString());

        mRecyclerView = (RecyclerView) findViewById(R.id.candidates_rv_voting);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter
        CandidateAdapter adapter = new CandidateAdapter(mCandidates, MODE);
        adapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nameTxt = v.findViewById(R.id.candidate_name_textview);
                String selectedCandidateName = nameTxt.getText().toString();
                int pos = mCandidates.indexOf(new Candidate(selectedCandidateName, 0));
                showConfirmationDialog(pos);
            }
        });
        mRecyclerView.setAdapter(adapter);

    }

    private void showConfirmationDialog(int pos){
        String msg1 = String.format("%s %s?", getString(R.string.mensaje_confirmacion) , mCandidates.get(pos).getName());

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmacion))
                .setMessage(msg1)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        increaseVote(pos);
                        String msg2 = String.format("Total de votos por %s: %d", mCandidates.get(pos).getName(), mCandidates.get(pos).getVoteCount());
                        Toast.makeText(VotingActivity.this, msg2, Toast.LENGTH_LONG).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void increaseVote(int pos){
        mCandidates.get(pos).increaseVoteCount();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if(CandidatesManager.saveAllCandidates(VotingActivity.this, mCandidates) == 0){
            Log.d(TAG, "Candidates Saved");
        }else{
            Log.e(TAG, "Error saving");
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(CandidatesManager.saveAllCandidates(VotingActivity.this, mCandidates) == 0){
            Log.d(TAG, "Candidates Saved");
        }else{
            Log.e(TAG, "Error saving");
        }
    }
}
