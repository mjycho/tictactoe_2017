import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Neural {
	static final int numOfLayer=3;		// 3 = 1 hidden, 4 = 2 hidden
	static final int numOfHidden = 200;

	static final int[] numOfNeurons = new int[numOfLayer];
	static float[][] layers=new float[numOfLayer][numOfHidden];
	static float[][][] weights=new float[numOfLayer-1][numOfHidden][numOfHidden];
	static int getMove=0;
	static int illegalMove=0;
	
	public Neural() {
		numOfNeurons[0]=18;
		numOfNeurons[1]=numOfHidden;
		numOfNeurons[2]=9;
		int count=0;
		String file="weights.txt";
		String content="";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			for(int i=0;i<=1;i++){
				for(int j=0;j<=numOfNeurons[1]-1;j++){
					for(int k=0;k<=numOfNeurons[1]-1;k++){
						weights[i][j][k]=Float.parseFloat(br.readLine());
					}
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static int getIllegalMove()
	{
		return(illegalMove);
	}

	public float[] getProbs() {
		return layers[2];
	}
	
	public static int feedforward(String temp) {
		for(int i=0;i<=17;i++){
			layers[0][i]=0;
		}
		for(int i=0;i<=8;i++){
			if(temp.substring(8-i,8-i+1).equals("1")){
				layers[0][i]=1.0f;
			}
			else if(temp.substring(8-i,8-i+1).equals("2")){
				layers[0][9+i]=1.0f;
			}
		}
		for(int i=0;i<=numOfLayer-2;i++){
			for(int j=0;j<=numOfNeurons[i+1]-1;j++){
				layers[i+1][j]=0.0f;	// init
				for(int k=0;k<=numOfNeurons[i]-1;k++)
					layers[i+1][j]+=layers[i][k]*weights[i][k][j];
				layers[i+1][j]=(float) (1/(1+Math.exp(-1*layers[i+1][j])));
			}
		}
	
		int maxInd=-1;
		float maxProb=0.0f;
		for(int i=0;i<=8;i++){
			//if(i==4) layers[2][4]+=0.05;
//			if(temp.substring(i,i+1).equals("0")) illegalMove++;
			if(layers[2][i]>=maxProb && temp.substring(8-i,8-i+1).equals("0")){
				maxProb=layers[2][i];
				maxInd=i;
			}
		}
		return maxInd;
	}

}
