package google_cs_with_android.tic_tac_toe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class helper extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tic_tac_toe, menu);
        return true;
    }

    public void dismiss(View v) {
       finish();
    }

}

