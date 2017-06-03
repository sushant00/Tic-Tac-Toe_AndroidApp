package google_cs_with_android.tic_tac_toe;

//board[i][j] = 0 means i,j th board position is empty
public class minimax {
    int player;             // 1=="X"
    int opponent;           // 2=="O"
    String Splayer;
    String Sopponent;

    public minimax(int player, int opponent){
        this.player = player;
        this.opponent = opponent;
        this.Splayer = player == 1 ? "X" : "0";
        this.Sopponent = opponent == 1 ? "X" : "0";
    };

    public static boolean isEmpty(int[][] board){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j]==0){
                    return true;
                }
            }
        }
        return false;
    }

    public static int value(int[][] board, int player, int opponent){
        // Checking for Rows for X or O victory.
        for (int row = 0; row<3; row++)
        {
            if (board[row][0]==board[row][1] && board[row][1]==board[row][2])
            {
                if (board[row][0]==player)
                    return +10;
                else if (board[row][0]==opponent)
                    return -10;
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col<3; col++)
        {
            if (board[0][col]==board[1][col] &&
                    board[1][col]==board[2][col])
            {
                if (board[0][col]==player)
                    return +10;

                else if (board[0][col]==opponent)
                    return -10;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (board[0][0]==board[1][1] && board[1][1]==board[2][2])
        {
            if (board[0][0]==player)
                return +10;
            else if (board[0][0]==opponent)
                return -10;
        }

        if (board[0][2]==board[1][1] && board[1][1]==board[2][0])
        {
            if (board[0][2]==player)
                return +10;
            else if (board[0][2]==opponent)
                return -10;
        }

        // Else if none of them have won then return 0
        return 0;
    }

    public int[] nextmove(int[][] board){
        int p=-1;
        int q=-1;
        int[] moveCoordinates = new int[2];
        int bestvalue=-10000;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j]==0){
                    board[i][j]=player;
                    int move=MM(board,0,false);
                    board[i][j]=0;
                    if(move>bestvalue){
                        bestvalue=move;
                        p=i;
                        q=j;
                    }
                }
            }
        }
        moveCoordinates[0] = p;moveCoordinates[1] = q;
        return moveCoordinates;
    }
    public int MM(int[][] board, int depth, boolean max) {
        int score=0;
        int value=value(board,this.player,this.opponent);
        if(value==10){
            return value-depth;
        }
        else if(value==-10){
            return value+depth;
        }
        if(!isEmpty(board)){
            return 0;
        }
        if(max){
            score=-100;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(board[i][j]==0){
                        board[i][j]=player;
                        score=Math.max(score, MM(board, depth+1,!max));
                        board[i][j]=0;
                    }
                }
            }
        }
        else{
            score=100;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(board[i][j]==0){
                        board[i][j]=opponent;
                        score=Math.min(score,MM(board, depth+1,!max));
                        board[i][j]=0;
                    }
                }
            }
        }
        return score;
    }


}
// to check if game is won calculate the value of current board and if equal to 10 or -10
//then game is over