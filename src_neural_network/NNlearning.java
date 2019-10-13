import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class NNlearning {
  
  // Parameters
  static final int numOfLayer=3;    // 3 = 1 hidden, 4 = 2 hidden
  static final float learnRate =0.005f;
  static final int numOfHidden = 200; 
  static final float thres = 0.3f;
  static final int learn_iteration = 1500;

  static final int[] numOfNeurons = new int[numOfLayer];
  static float[][][] weights = new float[2][numOfHidden][numOfHidden];
  static float[][] layers=new float[numOfLayer][numOfHidden];

  static float perIterationErrorSum=0.0f;
  static float errorSum=0.0f;
  static float target=0.0f;
  static float weightChange=0;
  static float delta=0.0f;
  static float[] deltaHidden;
  static boolean posValidTP;
  static boolean negValidTP;
  
  public static void main(String[] args) {
    //864
    numOfNeurons[0]=18;
    numOfNeurons[1]=numOfHidden;
    numOfNeurons[2]=9;
    deltaHidden=new float[numOfNeurons[1]];
    float[] seqstate=new float[19683];
    String file="";
    String content="";
    int counter=0;
    int numTrainPattern = 0;
    String temp="";
    int correctCnt = 0;
    int correctRawCnt = 0;
    int better_place = -1;
    boolean lets_train = false;
    int targetIdx = 0;

    // reading winChance.txt
    file="winChance.txt";
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

    //make random weights
    Random rand=new Random();
    for(int i=0;i<=weights.length-1;i++){
      for(int j=0;j<=weights[0].length-1;j++){
        for(int k=0;k<=weights[0][0].length-1;k++){
          weights[i][j][k]=rand.nextFloat()*2-1;
        }
      }
    }
    
    for(int iter=0;iter<=learn_iteration;iter++){
      perIterationErrorSum = 0.0f;
      numTrainPattern = 0;

      for(int b=0;b<19682;b++){
        // Adding leading 0's
        temp=(Integer.toString(b,3)+"");
        int temp_len = temp.length();
        for(int g=0;g<numOfNeurons[2]-temp_len;g++){
          temp="0"+temp;
        }
        
        posValidTP = false;
        negValidTP = false;
        
        // Win TP
        if((seqstate[b] > thres) && sameNumOfOX(temp)) {
          posValidTP = true;
          negValidTP = false;
        }
        
        // Lose TP
        /* NOT USED
        if((seqstate[b]< -0.1) && sameNumOfOX(temp)) {
          posValidTP = false;
          negValidTP = true;
        }
        */
        if(negValidTP || posValidTP) {
          String origin=temp;
          
          for(int j=0;j<8;j++){    // 8 variations
            temp=stringChange(origin, j);
            for(int i=0;i<numOfNeurons[2];i++){  // searching "2" to replace
              if(isO(temp,i)){
                String temp2=temp.substring(0,8-i)+"0"+temp.substring(8-i+1, temp.length());
                lets_train = false;
                // positive case
                if(!oWin(temp2) && posValidTP) {
                 //  better_place = another_better_place(temp2);
                    better_place = -1;
                  targetIdx = (better_place != -1)? better_place : i; // there are better place. Tran this!!
                  if(better_place != -1) {
                  //  System.out.println(temp+ " --> "+ temp2 + " "+i + " "+targetIdx);
                  }
                  lets_train = true;
                  //if(b==163) System.out.println(temp+ " --> "+ temp2 + " "+i + " "+targetIdx);

                }
                
                /* NOT USED
                if(!xWin(temp2) && negValidTP) {
                  better_place = another_better_place(temp2);
                  if(better_place == -1) lets_train = false;
                  else {
                    targetIdx = better_place;
                    lets_train = true;
                  //  System.out.println(temp+ " --> "+ temp2 + " "+i + " "+targetIdx);
                  }  
                }
                */
                
                if(lets_train) {
                  numTrainPattern++;
                  feedforward(temp2);
                
                  if(iter == learn_iteration) { // last iteration
                    if(checkMax(temp2) == targetIdx) correctCnt++;
                    if(checkMaxRaw(temp2) == targetIdx) correctRawCnt++;
                  } else { // except last iteration
                    calcError(targetIdx);
                    if(iter != 0) {
                      backprop(targetIdx, learnRate);
                    }
                  }
                }  
              }
            }
          }
        }
      } 
      System.out.println("Total Error @ iteration "+iter+" ("+numTrainPattern+") : "+perIterationErrorSum);
    }  // for iter
    
    System.out.println("Correct Placement Ratio : " + correctRawCnt +" " +correctCnt+"/"+numTrainPattern);

  
    try {
      File prob;
      FileOutputStream probo;
      prob = new File("weights.txt");
      probo = new FileOutputStream(prob);

      // if file doesnt exists, then create it
      prob.createNewFile();
      for(int i=0;i<=1;i++){
        for(int j=0;j<=numOfNeurons[1]-1;j++){
          for(int k=0;k<=numOfNeurons[1]-1;k++){
            temp=Float.toString(weights[i][j][k]);
            byte[] contentInBytes = (temp+"\n").getBytes();
            probo.write(contentInBytes);
          }
        }
      }
      probo.flush();
      probo.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private static float numOfO(String bd) {
    int countO=0;
    
    for(int i=0;i<9;i++) {
      if(isO(bd, i)) countO++;
    }
    return((float)countO);
  }
  
  private static boolean sameNumOfOX(String bd) {
    int countO=0;
    int countX=0;
    
    for(int i=0;i<9;i++) {
      if(isO(bd, i)) countO++;
      if(isX(bd, i)) countX++;
    }
    if(countO == countX) return(true);
    else         return(false);
  }
  
  private static int another_better_place(String bd) {
    String temp;
    
    for(int i=0;i<numOfNeurons[2];i++) {
      if(bd.substring(i,i+1).equals("0")) {
        if(posValidTP) {
          temp = bd.substring(0,i) +"2" + bd.substring(i+1, bd.length());
          //  System.out.println("before checkOWin "+temp);
          if(oWin(temp)) return(i);
        }
        
        if(negValidTP) {
          temp = bd.substring(0,i) +"1" + bd.substring(i+1, bd.length());
          //System.out.println("before checkXWin "+temp);
          if(xWin(temp)) return(i);
        }
      }
    }
    
    if(posValidTP) {  
      for(int i=0;i<numOfNeurons[2];i++) {
        if(bd.substring(i,i+1).equals("0")) {
          temp = bd.substring(0,i) +"1" + bd.substring(i+1, bd.length());
          if(xWin(temp)) {
          //  System.out.println("after  checkOWin "+temp+ " "+i);
            return(i);
          }
        }
      }
    }
    return(-1);
  }
  
  
  private static boolean isO(String cb, int loc) {
    return(cb.substring(8-loc,8-loc+1).equals("2"));
  }  
  
  private static boolean isX(String cb, int loc) {
    return(cb.substring(8-loc,8-loc+1).equals("1"));
  }
  
  
  private static boolean isEmpty(String cb, int loc) {
    return(cb.substring(8-loc,8-loc+1).equals("0"));
  }
  
  private static boolean oWin(String cb) {
    if((isO(cb, 0) && isO(cb, 1) && isO(cb, 2)) ||
       (isO(cb, 3) && isO(cb, 4) && isO(cb, 5)) ||
       (isO(cb, 6) && isO(cb, 7) && isO(cb, 8)) ||
       (isO(cb, 0) && isO(cb, 3) && isO(cb, 6)) ||
       (isO(cb, 1) && isO(cb, 4) && isO(cb, 7)) ||
       (isO(cb, 2) && isO(cb, 5) && isO(cb, 8)) ||
       (isO(cb, 0) && isO(cb, 4) && isO(cb, 8)) ||
       (isO(cb, 2) && isO(cb, 4) && isO(cb, 6))
    ) return(true);
    else return(false);
  }

  
  private static boolean xWin(String cb) {
    if((isX(cb, 0) && isX(cb, 1) && isX(cb, 2)) ||
       (isX(cb, 3) && isX(cb, 4) && isX(cb, 5)) ||
       (isX(cb, 6) && isX(cb, 7) && isX(cb, 8)) ||
       (isX(cb, 0) && isX(cb, 3) && isX(cb, 6)) ||
       (isX(cb, 1) && isX(cb, 4) && isX(cb, 7)) ||
       (isX(cb, 2) && isX(cb, 5) && isX(cb, 8)) ||
       (isX(cb, 0) && isX(cb, 4) && isX(cb, 8)) ||
       (isX(cb, 2) && isX(cb, 4) && isX(cb, 6))
    ) return(true);
    else return(false);
  }
  
  private static int checkMax(String bd) {
    // check max output with considering no-duplicate
    float max = -100000f;
    int maxIdx = -1;
    
    for(int i=0;i<9;i++) {
      if(isEmpty(bd,i) && (layers[numOfLayer-1][i] > max)) {
        maxIdx = i;
        max = layers[numOfLayer-1][i];
      }
    }
    return(maxIdx);
  }
  
  private static int checkMaxRaw(String bd) {
    // check max output without considering no-duplicate
    float max = -100000f;
    int maxIdx = -1;
    
    for(int i=0;i<9;i++) {
      if(layers[numOfLayer-1][i] > max) {
        maxIdx = i;
        max = layers[numOfLayer-1][i];
      }
    }
    return(maxIdx);
  }
  
  private static String stringChange(String cb, int count) {
    switch(count) {
      case 0 :
        return cb;
      case 1 : 
        return(cb.substring(6,9)+cb.substring(3,6)+cb.substring(0,3));  
      case 2 : 
        return(    cb.substring(2,3) + cb.substring(1,2) + cb.substring(0,1) + 
              cb.substring(5,6) + cb.substring(4,5) + cb.substring(3,4) + 
              cb.substring(8,9) +  cb.substring(7,8) + cb.substring(6,7)
          );
      case 3 : 
        return(    cb.substring(6,7) + cb.substring(3,4) + cb.substring(0,1) +
              cb.substring(7,8) + cb.substring(4,5) + cb.substring(1,2) +
              cb.substring(8,9) +  cb.substring(5,6) + cb.substring(2,3)
          );
      case 4 :
        return(    cb.substring(8,9) + cb.substring(7,8) + cb.substring(6,7) +
              cb.substring(5,6) + cb.substring(4,5) + cb.substring(3,4) +
              cb.substring(2,3) +  cb.substring(1,2) + cb.substring(0,1)
          );
      case 5 :
        return(    cb.substring(2,3) + cb.substring(5,6) + cb.substring(8,9) +
              cb.substring(1,2) + cb.substring(4,5) + cb.substring(7,8) +
              cb.substring(0,1) +  cb.substring(3,4) + cb.substring(6,7)
          );
      case 6 :
        return(    cb.substring(0,1) + cb.substring(3,4) + cb.substring(6,7) +
              cb.substring(1,2) + cb.substring(4,5) + cb.substring(7,8) +
              cb.substring(2,3) +  cb.substring(5,6) + cb.substring(8,9)
          );
      case 7 :
        return(    cb.substring(8,9) + cb.substring(5,6) + cb.substring(2,3) +
              cb.substring(7,8) + cb.substring(4,5) + cb.substring(1,2) +
              cb.substring(6,7) +  cb.substring(3,4) + cb.substring(0,1)
          );
      default :
        System.out.println("Error");
        return("");
    } // switch
  }

  private static void feedforward(String temp) {

    // Set up input layer
    for(int i=0;i<=8;i++){
      if(isX(temp,i)){
        layers[0][i]   = 1.0f;
        layers[0][i+9] = 0.0f;
      }
      else if(isO(temp,i)){
        layers[0][i]   = 0.0f;
        layers[0][i+9] = 1.0f;
      } else {
        layers[0][i]   = 0.0f;
        layers[0][i+9] = 0.0f;
      }
    }
    
    // Feedforward
    for(int i=0;i<=numOfLayer-2;i++){
      for(int j=0;j<numOfNeurons[i+1];j++){
        //wipe layers
        layers[i+1][j]=0.0f;
        for(int k=0;k<numOfNeurons[i];k++){
          layers[i+1][j]+=layers[i][k]*weights[i][k][j];
        }
        layers[i+1][j]=(float) (1/(1+Math.exp(-1*layers[i+1][j])));
      }
    }
  }
  
  private static void backprop(int ans, float learnRate){
    
  //  if(negValidTP) learnRate = 0.0f * learnRate;
    
    
    for(int i=0;i<numOfNeurons[1];i++){  //hidden
      deltaHidden[i]=0.0f;
      for(int j=0;j<numOfNeurons[2];j++){  //output
        //
        target = (ans==j)? 1.0f : 0.0f;
      //  if(ans==4) target = target * 1.25f;
        delta=(layers[2][j]-target)*layers[2][j]*(1-layers[2][j]);
        weightChange=delta*layers[1][i];
        deltaHidden[i]+=delta*weights[1][i][j];
        weights[1][i][j]-=(learnRate*weightChange);
      }
    }

    //hidden layer
    for(int i=0;i<numOfNeurons[0];i++){  //input layers
      for(int j=0;j<numOfNeurons[1];j++){  // hidden layers
        delta=(deltaHidden[j])*layers[1][j]*(1-layers[1][j]);
        weightChange=delta*layers[0][i];
        weights[0][i][j]-=(learnRate*weightChange);
      }
    }
  } 
  
  private static void calcError(int ans) {
    //calculate total error
    errorSum = 0.0f;
    for(int i=0;i<numOfNeurons[2];i++){
      target = (ans==i)? 1.0f : 0.0f;
      errorSum+=0.5*Math.pow((target-layers[2][i]),2);
    }
    perIterationErrorSum += errorSum;

  }
}  // class
