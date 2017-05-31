package google_cs_with_android.tic_tac_toe;

import android.app.Activity;
import android.os.Bundle;

public class settingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new settingsFragment()).commit();
    }

}