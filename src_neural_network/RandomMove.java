import java.util.Random;

public class RandomMove {
	static int getMove=0;

	public static int getMove(String ternary) {
		Random rand = new Random();
		int placement=0;
		while(true){
			placement=rand.nextInt(9);

			if(ternary.substring(8-placement, 8-placement+1).equals("2") || 
					ternary.substring(8-placement, 8-placement+1).equals("1")){
			}
			else{
				getMove=placement;
				break;
			}
		}
		return getMove;
	}
}

