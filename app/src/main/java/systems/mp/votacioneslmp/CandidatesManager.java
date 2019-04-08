package systems.mp.votacioneslmp;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CandidatesManager {

    public final static String CANDIDATES_FILENAME = "CANDIDATES_FILE_LMP";
    private static final String TAG = "MANAGER";

    public static void initializeDummyData(Context context){
        String[] names = {"Ana María Suárez", "José Delgado", "Agustín Intriago"};
        int[] votes = {3053, 2444, 4512};
        List<Candidate> theCandidates = new ArrayList<>();
        for (int i = 0; i<names.length; i++){
            theCandidates.add(new Candidate(names[i], votes[i]));
        }
        if(saveAllCandidates(context, theCandidates) == 0){
            Log.d(TAG, "Candidates Saved");
        }else{
            Log.e(TAG, "Error saving");
        }
    }

    public static List<Candidate> getAllCandidates(Context context){
        ArrayList<Candidate> candidates = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(CANDIDATES_FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            candidates = (ArrayList<Candidate> ) is.readObject();
            is.close();
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return candidates;
    }

    public static int saveAllCandidates(Context context, List<Candidate> candidates){
        try {
            FileOutputStream fos = context.openFileOutput(CANDIDATES_FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(candidates);
            of.flush();
            of.close();
            fos.close();
            return 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Candidate findCandidateInList(List<Candidate> candidates, Candidate candidate){
        return candidates.stream().filter(candidate::equals).findAny().orElse(null);
    }



}
