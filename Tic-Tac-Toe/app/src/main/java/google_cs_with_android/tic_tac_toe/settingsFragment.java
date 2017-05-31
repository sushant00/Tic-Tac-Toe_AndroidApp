package google_cs_with_android.tic_tac_toe;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class settingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}