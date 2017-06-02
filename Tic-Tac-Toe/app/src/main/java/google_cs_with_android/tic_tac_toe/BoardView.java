package google_cs_with_android.tic_tac_toe;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static google_cs_with_android.tic_tac_toe.minimax.isEmpty;
import static google_cs_with_android.tic_tac_toe.minimax.value;


public class BoardView {

    private Activity activity;
    private String player1Mark;
    private int currentTurn;    //0 for com, 1 for player1, 2 for player2
    private String[] marks;     //index 1 stores player1's marks, index 2 stores player2's mark
    private int[][] currentBoard;   //1 means X, 2 means O
    private Button[][] buttons;
    private minimax solver;         //used when playing against com
    private TextView textView;
    Toast gamedraw;
    Animation shake;

    public BoardView(Activity activity){
        this.activity = activity;
        gamedraw = Toast.makeText(activity, "It's a Draw! ", Toast.LENGTH_LONG);
        shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
    }

    public void init(boolean againstCom, String player1Mark, int firstTurn) {
        this.player1Mark = player1Mark;
        this.currentTurn = firstTurn;
        this.currentBoard = new int[3][3];
        this.buttons = new Button[3][3];
        this.textView = (TextView) activity.findViewById(R.id.textView);
        this.marks = new String[3];

        if (againstCom)
            playAgainstCom();
        else
            playerAgainstplayer();
    }

    private void setBoardAndStart(boolean vsCom) {
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
            if (currentTurn == 0) {
                int[] obtainMove = solver.nextmove(currentBoard);
                currentBoard[obtainMove[0]][obtainMove[1]] = solver.player;
                buttons[obtainMove[0]][obtainMove[1]].setText(solver.Splayer);
                buttons[obtainMove[0]][obtainMove[1]].setEnabled(false);
                currentTurn = 1;
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setOnClickListener(new onClickVsCom(i, j));
                    buttons[i][j].setText(" ");
                    buttons[i][j].setEnabled(true);
                }
            }
            textView.setText("You are " + player1Mark + "  Make your move");
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setOnClickListener(new onClick2player(i, j));
                    buttons[i][j].setText(" ");
                    buttons[i][j].setEnabled(true);
                }
            }
            textView.setText(" Player" + currentTurn + " make your move  You are " + marks[currentTurn]);
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
                currentBoard[i][j] = currentTurn;
                score = value(currentBoard, solver.opponent, solver.player);
                if (score == 10) {
                    animateVictory();
                    playerWon.show();
                    textView.setText("You Win! Click Restart");
                    disableAllButtons();
                    return;
                }
                if (!isEmpty(currentBoard)) {
                    animateDraw();
                    gamedraw.show();
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
                score = value(currentBoard, solver.player, solver.opponent);
                if (score == 10) {
                    animateVictory();
                    comWon.show();
                    textView.setText("You loose! Click Restart");
                    disableAllButtons();
                    return;
                }
                if (!isEmpty(currentBoard)) {
                    animateDraw();
                    gamedraw.show();
                    textView.setText("The game is Draw! Click Restart");
                    disableAllButtons();
                    return;
                }
                textView.setText("You are " + player1Mark + "   Make your move");
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
        Toast player1 = Toast.makeText(activity, "Player1 Won! Player2 Loose! ", Toast.LENGTH_LONG);
        Toast player2 = Toast.makeText(activity, "Player2 Won! Player1 Loose! ", Toast.LENGTH_LONG);

        public onClick2player(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public void onClick(View view) {
            if (buttons[i][j].isEnabled()) {
                opponent = currentTurn == 1 ? 2 : 1;
                buttons[i][j].setEnabled(false);
                buttons[i][j].setText(marks[currentTurn]);
                currentBoard[i][j] = currentTurn;
                score = value(currentBoard, currentTurn, opponent);
                if (score == 10) {
                    Log.i("score10","animate calling");
                    animateVictory();
                    Log.i("score10","animate done");
                    if (currentTurn == 1) {
                        player1.show();
                        textView.setText("Player1 won!  Click Restart");
                    } else {
                        player2.show();
                        textView.setText("Player2 won!  Click Restart");
                    }
                    disableAllButtons();
                    return;
                }
                if (!isEmpty(currentBoard)) {
                    animateDraw();
                    gamedraw.show();
                    textView.setText("The game is Draw!  Click Restart");
                    disableAllButtons();
                    return;
                }
                currentTurn = opponent;
                textView.setText(" Player" + currentTurn + " make your move    You are " + marks[currentTurn]);
            }
        }
    }

    private void animateDraw(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setAnimation(shake);
            }
        }
    }

    private void animateVictory(){
        Log.i("animate","in animate");
        for (int row = 2; row>-1; row--){
            if (currentBoard[row][0]==currentBoard[row][1] && currentBoard[row][1]==currentBoard[row][2]) {
                Log.i("animate","row"+row);
                buttons[row][0].setAnimation(shake);
                buttons[row][1].setAnimation(shake);
                buttons[row][2].setAnimation(shake);
                Log.i("animate","rowanimated"+row);
                return;
            }
        }
        // Checking for Columns for X or O victory.
        for (int col = 0; col<3; col++){
            if (currentBoard[0][col]==currentBoard[1][col] && currentBoard[1][col]==currentBoard[2][col]){
                Log.i("animate","col"+col);
                buttons[0][col].setAnimation(shake);
                buttons[1][col].setAnimation(shake);
                buttons[2][col].setAnimation(shake);
                Log.i("animate","col"+col);
                return;
            }
        }
        // Checking for Diagonals for X or O victory.
        if (currentBoard[0][0]==currentBoard[1][1] && currentBoard[1][1]==currentBoard[2][2]){
            buttons[0][0].setAnimation(shake);
            buttons[1][1].setAnimation(shake);
            buttons[2][2].setAnimation(shake);
            return;
        }
        buttons[0][2].setAnimation(shake);
        buttons[1][1].setAnimation(shake);
        buttons[2][0].setAnimation(shake);
    }

    private void disableAllButtons(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void playAgainstCom() {
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
}

