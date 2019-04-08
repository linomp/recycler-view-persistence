package systems.mp.votacioneslmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.cne_button).setOnClickListener(e -> {
            startActivity(new Intent(MainActivity.this, CandidatesActivity.class));
        });
        findViewById(R.id.votantes_button).setOnClickListener(e -> {
            startActivity(new Intent(MainActivity.this, VotingActivity.class));
        });


        // Write file with starter data only upon fresh install
        File file = new File(getApplicationContext().getFilesDir(),CandidatesManager.CANDIDATES_FILENAME);
        if(!file.exists()){
            Toast.makeText(getApplicationContext(),getString(R.string.file_created_msg), Toast.LENGTH_SHORT).show();
            CandidatesManager.initializeDummyData(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset_data:
                Toast.makeText(getApplicationContext(),getString(R.string.file_created_msg), Toast.LENGTH_SHORT).show();
                CandidatesManager.initializeDummyData(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


}