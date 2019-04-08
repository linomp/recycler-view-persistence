package systems.mp.votacioneslmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

public class NewCandidateActivity extends AppCompatActivity {

    private String mOldName;
    private String mNewName;
    public static final String NEW_NAME_KEY = "new_name";
    public static final String OLD_NAME_KEY = "old_name";
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_candidate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditText = findViewById(R.id.name_edit_text);

        Intent intent = getIntent();
        if(intent.hasExtra("requestCode")){
            if(intent.getIntExtra("requestCode", 0) == CandidatesActivity.EDIT_CANDIDATE){
                mOldName = intent.getStringExtra(OLD_NAME_KEY);
                mEditText.setText(mOldName);
                setTitle(getString(R.string.edit_candidate));
            }
        }

        // Sanitizes input removing all numbers.
        // White spaces are removed only for validation.
        findViewById(R.id.button_ok).setOnClickListener(v->{
            mNewName = mEditText.getText().toString().replaceAll("\\d","").trim();
            if(mNewName.replaceAll("\\s","").isEmpty()){
                mEditText.setError(getString(R.string.campo_vacio));
            }else {
                Intent returnResultIntent = new Intent(NewCandidateActivity.this, CandidatesActivity.class);
                returnResultIntent.putExtra(NEW_NAME_KEY, mNewName);
                returnResultIntent.putExtra(OLD_NAME_KEY, mOldName);
                setResult(RESULT_OK, returnResultIntent);
                finish();
            }
        });

    }

    // To survive configuration changes
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(NEW_NAME_KEY, mNewName);
        savedInstanceState.putString(OLD_NAME_KEY, mOldName);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNewName = savedInstanceState.getString(NEW_NAME_KEY);
        mOldName = savedInstanceState.getString(OLD_NAME_KEY);
    }

}
