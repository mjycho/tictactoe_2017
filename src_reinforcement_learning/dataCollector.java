import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class dataCollector {

	public static void main(String[] args) {
		int board[][]=new int[3][3];
		for(int i=0;i<=2;i++){
			for(int j=0;j<=2;j++){
				board[i][j]=0;
			}
		}
		Random rand = new Random();
		
		int dataCount=9999;
		//0=blank
		//1=X
		//2=O
		int placement=0;
		int count=0;
		String boardfull="";
		int drawIndex=0;
		int loserIndex=0;
		int winnerIndex=0;

		File win;
		FileOutputStream wino;
		boolean winfull=true;
		int winCount=0;
		
		File draw;
		FileOutputStream drawo;
		boolean drawfull=true;
		int drawCount=0;
		
		File lose;
		FileOutputStream loseo;
		boolean losefull=true;
		int loseCount=0;
		
		String tracker = "";

		while(losefull || winfull || drawfull){


			for(int j=0;j<=8;j++){
				//X TURN

				placement=rand.nextInt(9);
				while(true){
					if(boardfull.contains(Integer.toString(placement))){
						placement=rand.nextInt(9);
					}
					else{
						boardfull+=Integer.toString(placement);
						break;
					}
				}

				board[placement/3][placement%3]=1;
				tracker=tracker+""+Integer.toString(placement);
				count++;
				if(boardcheck(board, count).equals("x")){
					//write in x winner
					System.out.println("o WIN");
					drawboard(board);
					loseCount++;
					if(loserIndex<=dataCount){
						try {

							lose = new File("l/l"+loserIndex+".txt");
							loseo = new FileOutputStream(lose);

							// if file doesnt exists, then create it
							lose.createNewFile();


							// get the content in bytes
							byte[] contentInBytes = tracker.getBytes();

							loseo.write(contentInBytes);
							loseo.flush();
							loseo.close();
							loserIndex++;
							System.out.println("Done");

						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					else{
						losefull=false;
					}

					break;
				}
				//O TURN
				if(count==9){
					System.out.println("YOU DRAW"+tracker+" "+boardfull);
					drawboard(board);
					drawCount++;
					if(drawIndex<=dataCount){
						try {

							draw = new File("d/d"+drawIndex+".txt");
							drawo = new FileOutputStream(draw);

							// if file doesnt exists, then create it
							draw.createNewFile();


							// get the content in bytes
							byte[] contentInBytes = tracker.getBytes();

							drawo.write(contentInBytes);
							drawo.flush();
							drawo.close();
							drawIndex++;
							System.out.println("Done");

						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					else{
						drawfull=false;
					}

					//save draw
					break;
				}
				placement=rand.nextInt(9);
				while(true){
					if(boardfull.contains(Integer.toString(placement))){
						placement=rand.nextInt(9);
					}
					else{
						boardfull+=Integer.toString(placement);
						break;
					}
				}

				board[placement/3][placement%3]=2;
				tracker=tracker+""+Integer.toString(placement);

				count++;
				if(boardcheck(board, count).equals("o")){
					//write in o winner
					System.out.println("YOU LOSE"+tracker+" "+boardfull);
					drawboard(board);
					winCount++;
					if(winnerIndex<=dataCount){
						try {

							win = new File("w/w"+winnerIndex+".txt");
							wino = new FileOutputStream(win);

							// if file doesnt exists, then create it
							win.createNewFile();


							// get the content in bytes
							byte[] contentInBytes = tracker.getBytes();

							wino.write(contentInBytes);
							wino.flush();
							wino.close();
							winnerIndex++;
							System.out.println("Done");

						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					else{
						winfull=false;
					}

					//save x value
					break;
				}


			}
			boardfull="";
			count=0;
			tracker="";
			for(int k=0;k<=2;k++){
				for(int l=0;l<=2;l++){
					board[k][l]=0;
				}
			}
		}
		System.out.println(loseCount);
		System.out.println(winCount);
		System.out.println(drawCount);

	}

	private static void drawboard(int[][] board) {

		for(int i=0;i<=2;i++){
			for(int j=0;j<=2;j++){
				if(board[i][j]==0){
					System.out.print(" ");
				}
				if(board[i][j]==1){
					System.out.print("X");
				}
				if(board[i][j]==2){
					System.out.print("O");
				}
			}
			System.out.println();
		}

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
