import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class NNplayer {

	private static RandomMove random = new RandomMove();
	private static Neural neural = new Neural();
	private static ttt_expert expert = new ttt_expert();
	static int gamesLost=0;
	static int indcount=0;
	static String[] lossTrack=new String[7000];
	public static void main(String[] args) {
		int counter=0;
		int gamesPlayed=10000;
		int gamesWon=0;
		int gamesDrawn=0;
		int[][] board=new int[3][3];
		String[] losses=new String[7000];
		
		for(int i =0;i<=2;i++){
			for(int j =0;j<=2;j++){
				board[i][j]=0;
			}
		}
		int placement=0;
		String taken="";
		Random rand=new Random();
		counter=0;
		int bestIndex=0;
		float bestProb=(float) -1.0;
		int index=0;
		String play="";
		String posLoss="";
		int idx = 0;
		String [] gameStatusEven = new String[5];
		
		for(int i=1;i<=gamesPlayed;i++){
			//THINGS TO RESET
			//board
			for(int k =0;k<=2;k++){
				for(int j =0;j<=2;j++){
					board[k][j]=0;
				}
			}
			//counter
			counter=0;
			//taken
			taken="";
			posLoss="";
			idx =0;
			while(true){

				//X RANDOM
				//placement=expert.ask_xttt_expert(boardToTernary(board));
				//placement=Neural.feedforward(boardToTernary(board));
				placement=RandomMove.getMove(boardToTernary(board));
				//placement=humanMove(board);
				board[placement/3][placement%3]=1;
				posLoss=posLoss+placement;
				play="x";
				if(!(legalCheck(boardToTernary(board), play))){
					System.out.println("placement failure, terminate");
					break;
				}

				counter++;
				//drawboard(board);
				if(boardcheck(board, counter).equals("x")){
					//lose case		
					losses[gamesLost]=posLoss;

					gamesLost++;
					System.out.println("---lose---"+i);				
					System.out.println(posLoss);

					break;
				}

				//DRAW CASE
				if(counter==9){
					//draw case
					gamesDrawn++;
					//System.out.println("---draw---");

					break;
				}

				//O
				
				//placement=expert.ask_ottt_expert(boardToTernary(board));
				placement=Neural.feedforward(boardToTernary(board));
				//placement=randomMove.getMove(boardToTernary(board));
				if(!(legalCheck(boardToTernary(board), play))){
					System.out.println("placement failure, terminate");
					break;
				}				
				board[placement/3][placement%3]=2;	
				posLoss=posLoss+placement;

				play="o";

				//drawboard(board);
				gameStatusEven[idx++] = boardToTernary(board);
				counter++;

				if(boardcheck(board, counter).equals("o")){
					//win case
					gamesWon++;
					//System.out.println("---win---");
					break;
				}
			}
		}
		
		System.out.println("Done");
		
		//int indcount=0;
		for(int i=0;i<=gamesLost-1;i++){
			printseq(losses, i, indcount);
			System.out.println("======");
		}

		System.out.println(gamesPlayed+" games played.");
		System.out.println(gamesWon+" games won.");
		System.out.println(gamesLost+" games lost.");
		System.out.println(gamesDrawn+" games drawn.");
	//	System.out.println(Neural.getIllegalMove()+" illegalMove.");

	}

	private static void printseq(String[] losses, int i, int indcount) {

		String temp=losses[i];
		String board[][]=new String[3][3];
		for(int k=0;k<=2;k++){
			for(int l=0;l<=2;l++){
				board[k][l]=" ";
			}
		}
		for(int j=0;j<=temp.length()-1;j++){
			if(j%2==0){
				board[Integer.parseInt(temp.substring(j, j+1))/3][Integer.parseInt(temp.substring(j, j+1))%3]="X";
			}
			else{
				board[Integer.parseInt(temp.substring(j, j+1))/3][Integer.parseInt(temp.substring(j, j+1))%3]="O";
				lossTrack[indcount]=stringBoardToTernary(board);
				indcount++;
			}
			for(int k=0;k<=2;k++){
				for(int l=0;l<=2;l++){
					System.out.print(board[k][l]);
				}
				System.out.println();
			}
			System.out.println("------");
		}


	}

	private static String stringBoardToTernary(String[][] board) {
		String ternary="";
		for(int i=0;i<=8;i++){
			if(board[i/3][i%3].equals("X")){
				ternary="1"+ternary;
			}
			else if(board[i/3][i%3].equals("O")){
				ternary="2"+ternary+"2";
			}
			else{
				ternary="0"+ternary;
			}
		}
		return ternary;
	}

	private static int humanMove(int[][] board) {
		drawboard(board);
		Scanner sc = new Scanner(System.in);
		int placement=sc.nextInt();
		return placement;
	}

	private static boolean legalCheck(String ternary, String play) {
		int xCount=0;
		int oCount=0;
		for(int i=0;i<=ternary.length()-1;i++){
			if(ternary.substring(8-i, 8-i+1).equals("1")){
				xCount++;
			}
			if(ternary.substring(8-i, 8-i+1).equals("2")){
				oCount++;
			}
		}

		if(play.equals("x")){
			if((xCount-1)==oCount){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if(xCount==oCount){
				return true;
			}
			else{
				return false;
			}
		}
	}

	private static void drawboard(int[][] board) {
		System.out.println("======");
		for(int i=0;i<=2;i++){
			for(int j=0;j<=2;j++){
				if(board[i][j]==1){
					System.out.print("X");
				}
				else if(board[i][j]==2){
					System.out.print("O");
				}
				else{
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println("======");
	}

	private static String boardToTernary(int[][] board) {
		String ternary="";
		for(int i=0;i<=8;i++){
			
			ternary=Integer.toString(board[i/3][i%3])+ternary;
		}
		return ternary;
	}

	private static String boardcheck(int[][] board, int count) {
		if(count>=5){
			//X
			if(board[0][0]==1 && board[0][1]==1 && board[0][2]==1 ||
					board[1][0]==1 && board[1][1]==1 && board[1][2]==1 ||
					board[2][0]==1 && board[2][1]==1 && board[2][2]==1 || 
					board[0][0]==1 && board[1][0]==1 && board[2][0]==1 ||
					board[0][1]==1 && board[1][1]==1 && board[2][1]==1 ||
					board[0][2]==1 && board[1][2]==1 && board[2][2]==1 ||
					board[0][0]==1 && board[1][1]==1 && board[2][2]==1 || 
					board[0][2]==1 && board[1][1]==1 && board[2][0]==1){

				return "x";
			}

			//O
			else if((board[0][0]==2 && board[0][1]==2 && board[0][2]==2) ||
					board[1][0]==2 && board[1][1]==2 && board[1][2]==2 ||
					board[2][0]==2 && board[2][1]==2 && board[2][2]==2 || 
					board[0][0]==2 && board[1][0]==2 && board[2][0]==2 ||
					board[0][1]==2 && board[1][1]==2 && board[2][1]==2 ||
					board[0][2]==2 && board[1][2]==2 && board[2][2]==2 ||
					board[0][0]==2 && board[1][1]==2 && board[2][2]==2 || 
					board[0][2]==2 && board[1][1]==2 && board[2][0]==2){

				return "o";
			}
		}
		return "";
	}
}