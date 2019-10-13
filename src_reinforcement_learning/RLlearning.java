import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class RLlearning {
	static float[] seqstate =new float[19683];

	public static void main(String[] args) {

		for(int i=0;i<=19682;i++){
			seqstate[i]=(float) 0.0;
		}
		int dataCount=9999;
		File prob;
		FileOutputStream probo;
		String temp="";
		learn("w/w", 1.0, (float) 0.1, dataCount);
		learn("l/l", -1.0, (float) 0.1, dataCount);
		learn("d/d", 0.1, (float) 0.1, dataCount);
		
		/*
		for(int i=0;i<=19682;i++){
			if(seqstate[i]>=0.51 && seqstate[i]<=0.99){
				drawboard(i);
				System.out.println();
				System.out.println();
				//System.out.println(i);
				
			}
		}
		*/
		try {
			
			prob = new File("winChance.txt");
			probo = new FileOutputStream(prob);

			// if file doesnt exists, then create it
			prob.createNewFile();
			
			int winNum=0;
			int loseNum=0;
			int noEffectNum=0;
			int positiveNum=0;
			int negativeNum=0;
			
			// get the content in bytes
			for(int i=0;i<=19682;i++){
				if(seqstate[i]==1.0){
					winNum++;
				}
				else if(seqstate[i]==0.0){
					noEffectNum++;
				}
				else if(seqstate[i]==-1.0){
					loseNum++;
				}
				else{
					if(seqstate[i]>0.0){
						positiveNum++;
					}
					else{
						negativeNum++;
					}
				}
				
				temp=Float.toString(seqstate[i]);
				byte[] contentInBytes = (temp+"\n").getBytes();
				probo.write(contentInBytes);
			}
			
			probo.flush();
			probo.close();
			System.out.println("Done");
			System.out.println("Win num: "+winNum);
			System.out.println("Lose num: "+loseNum);
			System.out.println("no effect num: "+noEffectNum);
			System.out.println("positive num: "+positiveNum);
			System.out.println("negative num: "+negativeNum);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void learn(String filename, double d,float learnRate, int dataCount) {
		String ternary="";
		String file="";
		String content="";
		int decimal=0;
		float prevState=(float) 0.0;
		for(int a=0;a<=dataCount;a++){


			file=filename+a+".txt";

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					content=sCurrentLine;
			//		System.out.println(sCurrentLine);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}


			ternary=seqToternary(content);
			//System.out.println(ternary);
			decimal=ternaryToDecimal(ternary);
		//	System.out.println(decimal);

			seqstate[decimal]=(float) d;
			prevState=(float) d;
			int content_len = content.length();
		//	for(int j=0;j<=content_len-2;j++){
		    for(int j=0;j<=content.length()-2;j++){

				int content_len_int = content.length();
				content=content.substring(0, content_len_int-1);
				String ter = seqToternary(content);
				decimal=ternaryToDecimal(ter);
				float temp = seqstate[decimal];
				seqstate[decimal]=seqstate[decimal]+learnRate*(prevState-seqstate[decimal]);
				System.out.println("content : "+content + " ternary : " + ter +" decimal : "+decimal + " " + prevState + ":"+temp+ "->" + seqstate[decimal]);
				prevState=seqstate[decimal];
			}
			ternary="";
			content="";
		}		
	}

	private static void drawboard(int i) {
		String ternary=Integer.toString(i, 3);
		//System.out.println(ternary);
		int counter=0;
		for(int j=0;j<=ternary.length()-1;j++){
			if(counter==3){
				System.out.println();
				counter=0;
			}
			counter++;
			if(ternary.substring(8-j, 8-j+1).equals("1")){
				System.out.print("X");
			}
			else if(ternary.substring(8-j, 8-j+1).equals("0")){
				System.out.print(" ");
			}
			else{
				System.out.print("O");
			}
		}
	}


	private static int ternaryToDecimal(String ternary) {
		int answer=0;
		for(int i=0;i<=8;i++){
			answer+=Integer.parseInt(ternary.substring(8-i,8-i+1))* Math.pow(3, i);
			//System.out.println(ternary.substring(i, i+1));
		}
		return answer;
	}

	private static String seqToternary(String content) {
		String[] temp=new String[9];
		String ternary="";
		String[] holder=content.split("");
		for(int i=0;i<=8;i++){
			temp[i]="0";
		}

		for(int i=0;i<=content.length()-1;i++){
			//even x
			//odd o
			if(i%2==0){
				temp[Integer.parseInt(holder[i])]="1";
			}
			else{
				temp[Integer.parseInt(holder[i])]="2";
			}
		}

		for(int i=0;i<=8;i++){
			ternary=temp[i]+ternary;
		}
		return ternary;
	}

}
