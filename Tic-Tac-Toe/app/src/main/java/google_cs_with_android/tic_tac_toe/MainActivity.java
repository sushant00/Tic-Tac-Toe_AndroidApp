package google_cs_with_android.tic_tac_toe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private int firstTurn ;
    private String[] names = {"Player1","Player2"};
    private boolean playAgainstCom;
    private String playerMark;
    private BoardView boardView;
    private boolean voiceMode;
    private Button restart;
    private boolean speech; //true means moves are speaked out

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardView = new BoardView(MainActivity.this);
        restart = (Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardView = new BoardView(MainActivity.this);
                boardView.init(playAgainstCom, playerMark, firstTurn, names, speech);
            }
        });

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        playAgainstCom = mySharedPreferences.getBoolean("vsCom",true);
        speech = mySharedPreferences.getBoolean("speech",false);
        voiceMode = mySharedPreferences.getBoolean("voiceMode",false);
        if(playAgainstCom){
            playerMark = mySharedPreferences.getString("playerMark", "X");
            //if firstTurn==1 : player makes the move else if firstTurn == 0: com plays
            firstTurn = Integer.parseInt(mySharedPreferences.getString("firstTurn1", "1"));
            names[0] = mySharedPreferences.getString("playerName", "Player1");
        }else{
            playerMark = mySharedPreferences.getString("player1Mark", "X");
            //if firstTurn==1 : player1 makes the move else: player2
            firstTurn = Integer.parseInt(mySharedPreferences.getString("firstTurn2", "1"));
            names[0] = mySharedPreferences.getString("player1Name", "Player1");
            names[1] = mySharedPreferences.getString("player2Name", "Player2");
        }
        if(voiceMode){
            Toast.makeText(this, "Press volume down key."+
                    "Say \"Restart\" to restart the game" +
                            "Say \"Board\" to read out the board. " +
                            " Speak out positions from 1 to 9. "+"     Good Luck!!", Toast.LENGTH_LONG).show();
        }
        boardView.init(playAgainstCom, playerMark, firstTurn, names, speech);
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


    public void speechInput(){
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please speak your move");
        try {
            startActivityForResult(speechIntent, 222);
        }
        catch(ActivityNotFoundException a){
            Toast.makeText(this,"Voice Control not supported for your device!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 222 && resultCode == RESULT_OK) {
            ArrayList<String> command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for (String s : command) {
                switch (s) {
                    case "1":
                        boardView.buttons[0][0].performClick();
                        return;
                    case "2":
                        boardView.buttons[0][1].performClick();
                        return;
                    case "3":
                        boardView.buttons[0][2].performClick();
                        return;
                    case "4":
                        boardView.buttons[1][0].performClick();
                        return;
                    case "5":
                        boardView.buttons[1][1].performClick();
                        return;
                    case "6":
                        boardView.buttons[1][2].performClick();
                        return;
                    case "7":
                        boardView.buttons[2][0].performClick();
                        return;
                    case "8":
                        boardView.buttons[2][1].performClick();
                        return;
                    case "9":
                        boardView.buttons[2][2].performClick();
                        return;
                    case "restart":
                        restart.performClick();
                        return;
                    case "board":
                        boardView.speakBoard();
                        return;
                    }
                }
            }
        else if (requestCode == 0) {
            SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            playAgainstCom = mySharedPreferences.getBoolean("vsCom",true);
            speech = mySharedPreferences.getBoolean("speech",false);
            voiceMode = mySharedPreferences.getBoolean("voiceMode",false);
            if(playAgainstCom){
                playerMark = mySharedPreferences.getString("playerMark", "X");
                //if firstTurn==1 : player makes the move else if firstTurn == 0: com plays
                firstTurn = Integer.parseInt(mySharedPreferences.getString("firstTurn1", "1"));
                names[0] = mySharedPreferences.getString("playerName", "Player1");
            }else{
                playerMark = mySharedPreferences.getString("player1Mark", "X");
                //if firstTurn==1 : player1 makes the move else: player2
                firstTurn = Integer.parseInt(mySharedPreferences.getString("firstTurn2", "1"));
                names[0] = mySharedPreferences.getString("player1Name", "Player1");
                names[1] = mySharedPreferences.getString("player2Name", "Player2");
            }
            if(voiceMode){
                Toast.makeText(this, "Press volume down key" +
                        "Say \"Restart\" to restart the game" +
                        "Say \"Board\" to read out the board. " +
                        " Speak out positions from 1 to 9. "+"     Good Luck!!", Toast.LENGTH_LONG).show();
            }
            boardView = new BoardView(MainActivity.this);
            boardView.init(playAgainstCom, playerMark, firstTurn, names, speech);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(voiceMode)
                        speechInput();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

}
