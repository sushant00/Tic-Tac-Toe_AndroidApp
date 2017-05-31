package google_cs_with_android.tic_tac_toe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private int firstTurn = 1;
    private boolean playAgainstCom = true;
    private String playerMark = "X";
    private BoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardView = new BoardView(MainActivity.this);
        Button restart = (Button)findViewById(R.id.restart);
        Log.i("here",findViewById(R.id.button2)+"5"+restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardView = new BoardView(MainActivity.this);
                boardView.init(playAgainstCom, playerMark, firstTurn);
            }
        });
        boardView.init(playAgainstCom, playerMark, firstTurn);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tic_tac_toe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent();
            settingsIntent.setClass(MainActivity.this, settingsActivity.class);
            startActivityForResult(settingsIntent, 0);
            return true;
        }
        else if(id == R.id.exit){
            finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            playAgainstCom = mySharedPreferences.getBoolean("vsCom",true);
            if(playAgainstCom){
                playerMark = mySharedPreferences.getString("playerMark", "X");
                //if firstTurn==1 : player makes the move else if firstTurn == 0: com plays
                firstTurn = Integer.parseInt(mySharedPreferences.getString("firstTurn1", "1"));

            }else{
                playerMark = mySharedPreferences.getString("player1Mark", "X");
                //if firstTurn==1 : player1 makes the move else: player2
                 firstTurn = Integer.parseInt(mySharedPreferences.getString("firstTurn2", "1"));

            }
            boardView = new BoardView(MainActivity.this);
            boardView.init(playAgainstCom, playerMark, firstTurn);
        }
    }
}
