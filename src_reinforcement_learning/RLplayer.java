import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class RLplayer {

	public static void main(String[] args) {
		float[] seqstate=new float[19683];
		String file="";
		file="winChance.txt";
		String content="";
		int counter=0;

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				content=sCurrentLine;
				seqstate[counter]=Float.parseFloat(content);
				counter++;

				//System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		
				 
		
		int gamesPlayed=10000;
		int gamesLost=0;
		int gamesWon=0;
		int gamesDrawn=0;
		int[][] board=new int[3][3];
		for(int i =0;i<=2;i++){
			for(int j =0;j<=2;j++){
				board[i][j]=0;
			}
		}
		int placement=0;
		String taken="";
		Random rand=new Random();
		counter=0;
		String ternary="";
		int bestIndex=0;
		float bestProb=(float) -1.0;
		int index=0;

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
			
			
			while(true){
				
				//X RANDOM
				placement=rand.nextInt(9);
				while(true){
					if(taken.contains(Integer.toString(placement))){
						placement=rand.nextInt(9);
					}
					else{
						taken+=Integer.toString(placement);
						break;
					}
				}

				board[placement/3][placement%3]=1;
				counter++;
				drawboard(board);
				if(boardcheck(board, counter).equals("x")){
					//lose case
					gamesLost++;
					System.out.println("----------lose--------------------------------------------------------------------");

					break;
				}

				//DRAW CASE
				if(counter==9){
					//draw case
					gamesDrawn++;
					System.out.println("-------draw------------------------");

					break;
				}

				//O


				
				//COMPARE POSSIBLES
			
				bestIndex=0;
				bestProb=(float) -1.0;
				for(int j=0;j<=8;j++){
					if((board[j/3][j%3]==1) || (board[j/3][j%3]==2)){
						//SET PROBS LOW
						
					}
					else{
						board[j/3][j%3]=2;
						//BOARD TO TERNARY
						ternary=boardToTernary(board);//WORKING
						//TAKE IN BOARD AS DECIMAL
						index=ternaryToDecimal(ternary);//WORKING
						if(seqstate[index]>=bestProb){
							bestProb=seqstate[index];
							bestIndex=j;
						}
						board[j/3][j%3]=0;
					}
				}
				board[bestIndex/3][bestIndex%3]=2;

				System.out.println("The chance: "+bestProb);
				drawboard(board);

				taken+=bestIndex;
				counter++;

				if(boardcheck(board, counter).equals("o")){
					//win case
					gamesWon++;
					System.out.println("----------win-------------------------");
					break;
				}
			}
			

		}
		System.out.println(gamesPlayed+" games played.");
		System.out.println(gamesWon+" games won.");
		System.out.println(gamesLost+" games lost.");
		System.out.println(gamesDrawn+" games drawn.");
		
		/*
		int NumOfOne=0;
		int NumOfHalf=0;
		int NumOfElse=0;
		//FILE READER
		for(int i=0;i<=seqstate.length-1;i++){
			if(seqstate[i]==0.5){
				NumOfHalf++;
			}
			else if(seqstate[i]==1.0){
				NumOfOne++;
			}
			else{
				NumOfElse++;
			}
		}
		
		System.out.println("ones "+NumOfOne);
		System.out.println("half "+NumOfHalf);
		System.out.println("aregf "+NumOfElse);
		*/
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
	
	
	private static int ternaryToDecimal(String ternary) {
		int answer=0;
		String temp=new StringBuilder(ternary).reverse().toString();

		for(int i=0;i<=8;i++){
			answer+=Integer.parseInt(temp.substring(i,i+1))* Math.pow(3, i);
			//System.out.println(temp.substring(i, i+1));
			//System.out.println(Integer.parseInt(temp.substring(i,i+1))* Math.pow(3, i));
		}
		return answer;
	}

	private static String boardToTernary(int[][] board) {
		String ternary="";
		for(int i=0;i<=8;i++){
			ternary=ternary+Integer.toString(board[i/3][i%3]);
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
