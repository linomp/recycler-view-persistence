package systems.mp.votacioneslmp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

public class CandidatesActivity extends AppCompatActivity {

    private static final String TAG = "CANDIDATES";
    private static final int MODE = 1;
    public static final int CREATE_CANDIDATE = 10;
    public static final int EDIT_CANDIDATE = 11;
    private List<Candidate> mCandidates;

    private RecyclerView mRecyclerView; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startNewCandidateActivity());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCandidates = CandidatesManager.getAllCandidates(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.candidates_rv_edit);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter
        CandidateAdapter adapter = new CandidateAdapter(mCandidates, MODE);
        adapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nameTxt = v.findViewById(R.id.candidate_name_textview);
                String selectedCandidateName = nameTxt.getText().toString();
                beginEdition(selectedCandidateName);
            }
        });
        mRecyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void startNewCandidateActivity() {
        Intent createCandidateIntent = new Intent(CandidatesActivity.this, NewCandidateActivity.class);
        startActivityForResult(createCandidateIntent, CREATE_CANDIDATE);
    }

    private void beginEdition(String oldName) {
        Intent createCandidateIntent = new Intent(CandidatesActivity.this, NewCandidateActivity.class);
        createCandidateIntent.putExtra(NewCandidateActivity.OLD_NAME_KEY, oldName);
        startActivityForResult(createCandidateIntent, EDIT_CANDIDATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_candidates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset) {
            Toast.makeText(CandidatesActivity.this, getString(R.string.votos_reset), Toast.LENGTH_LONG).show();

            // reset all candidates votes to 0
            Iterator<Candidate> candidateIterator = mCandidates.iterator();
            while(candidateIterator.hasNext()) {
                candidateIterator.next().setVoteCount(0);
            }
            if(mRecyclerView.getAdapter() != null) {
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if(CandidatesManager.saveAllCandidates(CandidatesActivity.this, mCandidates) == 0){
            Log.d(TAG, "Candidates Saved");
        }else{
            Log.e(TAG, "Error saving");
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(CandidatesManager.saveAllCandidates(CandidatesActivity.this, mCandidates) == 0){
            Log.d(TAG, "Candidates Saved");
        }else{
            Log.e(TAG, "Error saving");
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("requestCode", requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_OK || data == null){
            return;
        }
        switch(requestCode){
            case CREATE_CANDIDATE:
                String name = data.getStringExtra(NewCandidateActivity.NEW_NAME_KEY);
                if(mCandidates.contains(new Candidate(name, 0))) {
                    Toast.makeText(CandidatesActivity.this, getString(R.string.duplicado), Toast.LENGTH_LONG).show();
                    startNewCandidateActivity();
                }else{
                    mCandidates.add(new Candidate(name, 0));
                }
                break;
            case EDIT_CANDIDATE:
                String oldName = data.getStringExtra(NewCandidateActivity.OLD_NAME_KEY);
                String newName = data.getStringExtra(NewCandidateActivity.NEW_NAME_KEY);
                int oldIndex = mCandidates.indexOf(new Candidate(oldName, 0));
                int newIndex = mCandidates.indexOf(new Candidate(newName, 0));
                if( ( oldIndex != newIndex) && mCandidates.contains(new Candidate(newName, 0))) {
                    Toast.makeText(CandidatesActivity.this, getString(R.string.duplicado), Toast.LENGTH_LONG).show();
                    beginEdition(oldName);
                }else {
                    int pos = mCandidates.indexOf(new Candidate(oldName, 0));
                    mCandidates.get(pos).setName(newName);
                }
                break;
        }
        if(mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
