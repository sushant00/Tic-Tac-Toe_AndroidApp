package google_cs_with_android.tic_tac_toe;

import android.app.Activity;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static google_cs_with_android.tic_tac_toe.minimax.isEmpty;
import static google_cs_with_android.tic_tac_toe.minimax.value;


public class BoardView extends MainActivity{

    public Activity activity;
    public String player1Mark;
    public int currentTurn;    //0 for com, 1 for player1, 2 for player2
    public String[] currentTurnName; // index 1 stores name for player1, 2 for player2
    public String[] marks;     //index 1 stores player1's marks, index 2 stores player2's mark
    public int[][] currentBoard;   //1 means X, 2 means O
    public Button[][] buttons;
    public minimax solver;         //used when playing against com
    public TextView textView;
    public boolean speech;
    Toast gamedraw;
    Animation shake;
    private TextToSpeech textSpeech;

    public BoardView(Activity activity){
        this.activity = activity;
        gamedraw = Toast.makeText(activity, "It's a Draw! ", Toast.LENGTH_LONG);
        shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
    }

    public void init(boolean againstCom, String player1Mark, int firstTurn, String[] names, boolean speech) {
        this.player1Mark = player1Mark;
        this.currentTurn = firstTurn;
        this.currentTurnName = new String[]{"",names[0],names[1]};
        this.currentBoard = new int[3][3];
        this.buttons = new Button[3][3];
        this.textView = (TextView) activity.findViewById(R.id.textView);
        this.marks = new String[3];
        this.speech = speech;

        //setup text to speech
        textSpeech=new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textSpeech.setLanguage(Locale.UK);
                }
            }
        });

        if (againstCom)
            playAgainstCom();
        else
            playerAgainstplayer();
    }

    public void setBoardAndStart(boolean vsCom) {
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j < 3; j++)
                currentBoard[i][j] = 0;
        }
        buttons[0][0] = (Button) activity.findViewById(R.id.button1);
        buttons[0][1] = (Button) activity.findViewById(R.id.button2);
        buttons[0][2] = (Button) activity.findViewById(R.id.button3);

        buttons[1][0] = (Button) activity.findViewById(R.id.button4);
        buttons[1][1] = (Button) activity.findViewById(R.id.button5);
        buttons[1][2] = (Button) activity.findViewById(R.id.button6);

        buttons[2][0] = (Button) activity.findViewById(R.id.button7);
        buttons[2][1] = (Button) activity.findViewById(R.id.button8);
        buttons[2][2] = (Button) activity.findViewById(R.id.button9);

        if (vsCom) {
            //handle the firstMove
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setOnClickListener(new onClickVsCom(i, j));
                    buttons[i][j].setText(" ");
                    buttons[i][j].setEnabled(true);
                }
            }
            if (currentTurn == 0) {
                int[] obtainMove = solver.nextmove(currentBoard);
                currentBoard[obtainMove[0]][obtainMove[1]] = solver.player;
                buttons[obtainMove[0]][obtainMove[1]].setText(solver.Splayer);
                buttons[obtainMove[0]][obtainMove[1]].setEnabled(false);
                if (speech){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textSpeech.speak("Com placed " + solver.Splayer + " at position " + (obtainMove[0] * 3 + obtainMove[1] + 1), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textSpeech.speak("Com placed " + solver.Splayer + " at position " + (obtainMove[0] * 3 + obtainMove[1] + 1), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    while (textSpeech.isSpeaking()) {
                        continue;
                    }
                }
                currentTurn = 1;
            }
            textView.setText( currentTurnName[1]+"  make your move. You are " + player1Mark);
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setOnClickListener(new onClick2player(i, j));
                    buttons[i][j].setText(" ");
                    buttons[i][j].setEnabled(true);
                }
            }
            if (speech) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textSpeech.speak(currentTurnName[currentTurn] + "  make your move. You are " + marks[currentTurn], TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    textSpeech.speak(currentTurnName[currentTurn] + "  make your move. You are " + marks[currentTurn], TextToSpeech.QUEUE_FLUSH, null);
                }
                while (textSpeech.isSpeaking()) {
                    continue;
                }
            }
            textView.setText( currentTurnName[currentTurn]+"  make your move. You are " + marks[currentTurn]);
        }
    }

    class onClickVsCom implements View.OnClickListener {
        int i;
        int j;
        int[] obtainMove;
        int score;
        Toast comWon = Toast.makeText(activity, "Com Won! You Loose! ", Toast.LENGTH_LONG);
        Toast playerWon = Toast.makeText(activity, "You Won! You beat Com ", Toast.LENGTH_LONG);

        public onClickVsCom(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public void onClick(View view) {
            if (buttons[i][j].isEnabled()) {
                buttons[i][j].setEnabled(false);
                buttons[i][j].setText(marks[currentTurn]);
                currentBoard[i][j] = solver.opponent;
                if (speech) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textSpeech.speak("You placed" + solver.Sopponent + " at position " + (i * 3 + j + 1), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textSpeech.speak("You placed" + solver.Sopponent + " at position " + (i * 3 + j + 1), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    while (textSpeech.isSpeaking()) {
                        continue;
                    }
                }
                score = value(currentBoard, solver.opponent, solver.player);
                if (score == 10) {
                    animateVictory();
                    playerWon.show();
                    if (speech) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textSpeech.speak("You Win!", TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            textSpeech.speak("You Win!", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    textView.setText("You Win! Click Restart");
                    disableAllButtons();
                    return;
                }
                if (!isEmpty(currentBoard)) {
                    animateDraw();
                    gamedraw.show();
                    if (speech) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textSpeech.speak("It's a Draw! ", TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            textSpeech.speak("It's a Draw! ", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    textView.setText("The game is Draw! Click Restart");
                    disableAllButtons();
                    return;
                }
                currentTurn = 0;

                //now com moves
                obtainMove = solver.nextmove(currentBoard);
                currentBoard[obtainMove[0]][obtainMove[1]] = solver.player;
                buttons[obtainMove[0]][obtainMove[1]].setEnabled(false);
                buttons[obtainMove[0]][obtainMove[1]].setText(marks[currentTurn]);
                if (speech) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textSpeech.speak("Com placed " + solver.Splayer + " at position " + (obtainMove[0] * 3 + obtainMove[1] + 1), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textSpeech.speak("Com placed " + solver.Splayer + " at position " + (obtainMove[0] * 3 + obtainMove[1] + 1), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    while (textSpeech.isSpeaking()) {
                        continue;
                    }
                }
                score = value(currentBoard, solver.player, solver.opponent);
                if (score == 10) {
                    animateVictory();
                    comWon.show();
                    if (speech) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textSpeech.speak("You loose! Com Won", TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            textSpeech.speak("You loose! Com Won", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    textView.setText("You loose! Click Restart");
                    disableAllButtons();
                    return;
                }
                if (!isEmpty(currentBoard)) {
                    animateDraw();
                    gamedraw.show();
                    if (speech) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textSpeech.speak("It's a Draw", TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            textSpeech.speak("It's a Draw", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    textView.setText("The game is Draw! Click Restart");
                    disableAllButtons();
                    return;
                }
                textView.setText( currentTurnName[1]+"  make your move. You are " + marks[currentTurn]);
                currentTurn = 1;
            }
        }
    }



    //listener of key when playing in two player mode
    class onClick2player implements View.OnClickListener {
        int i;
        int j;
        int opponent;
        int score;
        Toast player1 = Toast.makeText(activity, currentTurnName[1]+" Won! "+currentTurnName[2]+" Loose! ", Toast.LENGTH_LONG);
        Toast player2 = Toast.makeText(activity, currentTurnName[2]+" Won! "+currentTurnName[1]+" Loose! ", Toast.LENGTH_LONG);

        public onClick2player(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public void onClick(View view) {
            if (buttons[i][j].isEnabled()) {
                if (speech) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textSpeech.speak(currentTurnName[currentTurn] + " placed " + marks[currentTurn] + " at position" + (i * 3 + j + 1), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textSpeech.speak(currentTurnName[currentTurn] + " placed " + marks[currentTurn] + " at position" + (i * 3 + j + 1), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    while (textSpeech.isSpeaking()) {
                        continue;
                    }
                }
                opponent = currentTurn == 1 ? 2 : 1;
                buttons[i][j].setEnabled(false);
                buttons[i][j].setText(marks[currentTurn]);
                currentBoard[i][j] = currentTurn;
                score = value(currentBoard, currentTurn, opponent);
                if (score == 10) {
                    animateVictory();
                    if (speech) {
                        if (currentTurn == 1) {
                            player1.show();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                textSpeech.speak(currentTurnName[1] + " won!", TextToSpeech.QUEUE_FLUSH, null, null);
                            } else {
                                textSpeech.speak(currentTurnName[1] + " won!", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                        textView.setText(currentTurnName[1]+" won!  Click Restart");
                    } else {
                        player2.show();
                        if (speech) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                textSpeech.speak(currentTurnName[2] + " won!", TextToSpeech.QUEUE_FLUSH, null, null);
                            } else {
                                textSpeech.speak(currentTurnName[2] + " won!", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                        textView.setText(currentTurnName[2]+" won!  Click Restart");
                    }
                    disableAllButtons();
                    return;
                }
                if (!isEmpty(currentBoard)) {
                    animateDraw();
                    gamedraw.show();
                    if (speech) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textSpeech.speak("It's a Draw! ", TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            textSpeech.speak("It's a Draw! ", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    textView.setText("The game is Draw!  Click Restart");
                    disableAllButtons();
                    return;
                }
                currentTurn = opponent;
                textView.setText(currentTurnName[currentTurn] + " make your move. You are " + marks[currentTurn]);
            }
        }
    }

    public void animateDraw(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setAnimation(shake);
            }
        }
    }

    public void animateVictory(){
        AnimationSet as = new AnimationSet(true);
        // Checking for Rows for X or O victory.
        for (int row = 0; row<3; row++){
            if (currentBoard[row][0]==currentBoard[row][1] && currentBoard[row][1]==currentBoard[row][2]) {
                if(currentBoard[row][0] > 0) {
                    buttons[row][0].setAnimation(shake);
                    buttons[row][1].setAnimation(shake);
                    buttons[row][2].setAnimation(shake);
                    return;
                }
            }
        }
        // Checking for Columns for X or O victory.
        for (int col = 0; col<3; col++){
            if (currentBoard[0][col]==currentBoard[1][col] && currentBoard[1][col]==currentBoard[2][col]){
                if(currentBoard[0][col] > 0) {
                    buttons[0][col].setAnimation(shake);
                    buttons[1][col].setAnimation(shake);
                    buttons[2][col].setAnimation(shake);
                    return;
                }
            }
        }
        // Checking for Diagonals for X or O victory.
        if (currentBoard[0][0]==currentBoard[1][1] && currentBoard[1][1]==currentBoard[2][2] && currentBoard[0][0]>0){
            buttons[0][0].setAnimation(shake);
            buttons[1][1].setAnimation(shake);
            buttons[2][2].setAnimation(shake);
            return;
        }
        buttons[0][2].setAnimation(shake);
        buttons[1][1].setAnimation(shake);
        buttons[2][0].setAnimation(shake);
    }


    public void disableAllButtons(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public void speakBoard(){
        String[] symbols = {"empty","X","0"};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textSpeech.speak( currentTurnName[currentTurn]+" you are "+marks[currentTurn], TextToSpeech.QUEUE_FLUSH, null,null);
        } else {
            textSpeech.speak(currentTurnName[currentTurn]+" you are "+marks[currentTurn], TextToSpeech.QUEUE_FLUSH, null);
        }
        while(textSpeech.isSpeaking()){
            continue;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textSpeech.speak("position "+(i*3+j+1)+" "+symbols[currentBoard[i][j]], TextToSpeech.QUEUE_FLUSH, null,null);
                } else {
                    textSpeech.speak("position "+(i*3+j+1)+" "+symbols[currentBoard[i][j]], TextToSpeech.QUEUE_FLUSH, null);
                }
                while(textSpeech.isSpeaking()){
                    continue;
                }
            }
        }
    }


    public void playAgainstCom() {
        if (player1Mark.equals("X")) {
            solver = new minimax(2, 1);
            marks[0] = "O";
            marks[1] = "X";
        } else {
            solver = new minimax(1, 2);
            marks[0] = "X";
            marks[1] = "O";
        }

        setBoardAndStart(true);
    }

    private void playerAgainstplayer() {
        if (player1Mark.equals("X")) {
            marks[1] = "X";
            marks[2] = "O";
        } else {
            marks[1] = "O";
            marks[2] = "X";
        }
        setBoardAndStart(false);
    }


    // on pause , for textSpeech
    public void onPause(){
        if(textSpeech !=null){
            textSpeech.stop();
            textSpeech.shutdown();
        }
        super.onPause();
    }


}

