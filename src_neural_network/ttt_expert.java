
import java.util.Random;

public class ttt_expert {	
	// O : Us = 2
	// X : Opponent = 1
	// X, first.
	
	public boolean isEmpty(String cb, int loc) {
		return(cb.substring(8-loc,8-loc+1).equals("0"));
	}
	
	public boolean isO(String cb, int loc) {
		return(cb.substring(8-loc,8-loc+1).equals("2"));
	}
	
	public boolean isX(String cb, int loc) {
		return(cb.substring(8-loc,8-loc+1).equals("1"));
	}
	
	private int countO (String cb) {
		int count = 0;
		for(int i=0;i<9;i++)
			if(isO(cb,i)) count++;
		return(count);
	}
	
	private int countX (String cb) {
		int count = 0;
		for(int i=0;i<9;i++) 
			if(isX(cb,i)) count++;
		return(count);
	}
	
	public boolean oWin(String cb) {
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
	public boolean xWin(String cb) {
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
	
	private int O0(String cb) {
		if(!isX(cb, 4)) return(4);
		else return(0);
	}

	private int O1(String cb) {
		String temp;
		
		// search possible opponent's three
		for(int i=0;i<9;i++) {
			if(isEmpty(cb,i)) {
				temp = cb.substring(0,8-i)+"1"+cb.substring(8-i+1,9);
				if(xWin(temp)) {
					return(i);
				}
			}
		}
		// no immediate three
		
		// mine is center?
		if(isO(cb, 4)) {
			if(isX(cb,0) && isX(cb,8)) return(7);
			else if(isX(cb,2) && isX(cb, 6)) return(7);
			
			// one of these should be true, because there are only two X's.
			if(     (isEmpty(cb,0)) && (isEmpty(cb,8))) return(0);
			else if((isEmpty(cb,2)) && (isEmpty(cb,6))) return(2);
			else if((isEmpty(cb,3)) && (isEmpty(cb,5))) return(3);
			else if((isEmpty(cb,1)) && (isEmpty(cb,7))) return(1);
			else {
				System.out.println("Error o1_0");
				System.exit(0);				
			}
		} else {	
			if(isX(cb, 0) || isX(cb,8)) { //left top, right bottom
				if(!isO(cb,2)) return(2);
				else if(!isO(cb,6)) return(6);
				else return(4);		// X's are in 2 & 6
			} else if(isX(cb,2) || isX(cb,6)) { // right top, left bottom
				if(!isO(cb,0)) return(0);
				else if(!isO(cb,8)) return(8);
				else return(4);		// X's are in 2 & 6	
			}
		}
		
		// There should be no case that mine is other places.
		System.out.println("Error o1_1 : "+cb);
		System.exit(0);
		return(0);
	}
	
	private int O2(String cb) {
		String temp;
		
		// search possible my and opponent's three
		for(int i=0;i<9;i++) {
			if(isEmpty(cb,i)) {
				temp = cb.substring(0,8-i)+"2"+cb.substring(8-i+1,9);

				if(oWin(temp)) {
					return(i); // win
				}
			}
		}
		for(int i=0;i<9;i++) {
			if(isEmpty(cb,i)) {		
				temp = cb.substring(0,8-i)+"1"+cb.substring(8-i+1,9);
				if(xWin(temp)) {
					return(i); // try to block
				}
			}
		}
		
		// Search two consecutive empty spots
		if(isEmpty(cb,0) && isEmpty(cb,1)) return(0);
		if(isEmpty(cb,1) && isEmpty(cb,2)) return(2);
		if(isEmpty(cb,2) && isEmpty(cb,5)) return(2);
		if(isEmpty(cb,5) && isEmpty(cb,8)) return(8);
		if(isEmpty(cb,7) && isEmpty(cb,8)) return(8);
		if(isEmpty(cb,6) && isEmpty(cb,7)) return(6);
		if(isEmpty(cb,3) && isEmpty(cb,6)) return(6);
		if(isEmpty(cb,0) && isEmpty(cb,3)) return(0);

		// else random
		do {
			Random r = new Random();
			int num = r.nextInt(9);
			if(isEmpty(cb, num)) return(num);
		} while(true);
	}
	
	private int O3(String cb) {
		String temp;
		
		// search possible my and opponent's three
		for(int i=0;i<9;i++) {
			if(isEmpty(cb,i)) {
				temp = cb.substring(0,8-i)+"2"+cb.substring(8-i+1,9);
				System.out.println("oWin Checking : "+temp);

				if(oWin(temp)) {
					return(i); // win
				}
			}
		}
		
		for(int i=0;i<9;i++) {
			if(isEmpty(cb,i)) {		
				temp = cb.substring(0,8-i)+"1"+cb.substring(8-i+1,9);
				if(xWin(temp)) {
					return(i); // try to block
				}
			}
		}
		
		// else random
		do {
			Random r = new Random();
			int num = r.nextInt(9);
			if(isEmpty(cb, num)) return(num);
		} while(true);
	}
	
	private int X0(String cb) {
		return(0);	// always top left
	}
	
	private int X1(String cb) {
		if(isO(cb,1)) return(6);
		else if(isO(cb,3)) return(2);
		else if(isEmpty(cb,4)) return(4);
		// find empty edge spot
		else if(isEmpty(cb,0)) return(0);
		else if(isEmpty(cb,2)) return (2);
		else { // not possible
			System.out.println("Error");
			System.exit(0);	
			return(0);
		}
	}
	
	private int X2(String cb) {
		String temp;
		// search possible my and opponent's three
		for(int i=0;i<9;i++) {
			if(isEmpty(cb,i)) {
				temp = cb.substring(0,8-i)+"1"+cb.substring(8-i+1,9);
				if(xWin(temp)) {
					return(i); // win
				}
				
				temp = cb.substring(0,8-i)+"2"+cb.substring(8-i+1,9);
				if(oWin(temp)) {
					return(i); // try to block
				}
			}
		}  // for i
		
		// fill center if that is available
		if(isEmpty(cb, 4)) return(4);
		
		if(isX(cb, 0) || isX(cb,8)) { //left top, right bottom
			if(isEmpty(cb,2)) return(2);
			else if(isEmpty(cb,6)) return(6);
			else if(isEmpty(cb,0)) return(0);
			else if(isEmpty(cb,8)) return(8);
			else {
				System.out.println("Error");
				System.exit(0);				
			}
		} else if(isX(cb,2) || isX(cb,6)) { // right top, left bottom
			if(isEmpty(cb,0)) return(0);
			else if(isEmpty(cb,8)) return(8);
			else if(isEmpty(cb,2)) return(2);
			else if(isEmpty(cb,6)) return(6);
			else {
				System.out.println("Error");
				System.exit(0);				
			}		
		}
		
		// else random
		do {
			Random r = new Random();
			int num = r.nextInt(9);
			if(isEmpty(cb, num)) return(num);
		} while(true);
	}
	
	private int X3(String cb) {
		String temp;
		// search possible my and opponent's three
		for(int i=0;i<9;i++) {
			if(isEmpty(cb,i)) {
				temp = cb.substring(0,8-i)+"1"+cb.substring(8-i+1,9);
				if(xWin(temp)) {
					return(i); // win
				}
				
				temp = cb.substring(0,8-i)+"2"+cb.substring(8-i+1,9);
				if(oWin(temp)) {
					return(i); // try to block
				}
			}
		}  // for i
		
		// Search two consecutive empty spots
		if(isEmpty(cb,0) && isEmpty(cb,1)) return(0);
		if(isEmpty(cb,1) && isEmpty(cb,2)) return(2);
		if(isEmpty(cb,2) && isEmpty(cb,5)) return(2);
		if(isEmpty(cb,5) && isEmpty(cb,8)) return(8);
		if(isEmpty(cb,7) && isEmpty(cb,8)) return(8);
		if(isEmpty(cb,6) && isEmpty(cb,7)) return(6);
		if(isEmpty(cb,3) && isEmpty(cb,6)) return(6);
		if(isEmpty(cb,0) && isEmpty(cb,3)) return(0);

		// else random
		do {
			Random r = new Random();
			int num = r.nextInt(9);
			if(isEmpty(cb, num)) return(num);
		} while(true);
	}
	
	private int X4(String cb) {
		// there is only one left. Find it.
		for(int i=0;i<9;i++) {
			if(isEmpty(cb,i)) return(i);
		}
		System.out.println("Error");
		System.exit(0);
		return(0);
	}
	
	// X is the first player
	public int ask_xttt_expert(String currentBoard) {		
		switch(countX(currentBoard)) {
			case 0 : 
				return(X0(currentBoard));
			case 1 :
				return(X1(currentBoard));
			case 2 :
				return(X2(currentBoard));
			case 3 :
				return(X3(currentBoard));
			case 4 :
				return(X4(currentBoard));			
			default :
				System.out.println("Error");
				System.exit(0);
		}
		return(1);
	}
	
	// O is the second player
	public int ask_ottt_expert(String currentBoard) {		
		//System.out.println("CB :"+currentBoard);
		switch(countO(currentBoard)) {
			case 0 : 
				return(O0(currentBoard));
			case 1 :
				return(O1(currentBoard));
			case 2 :
				return(O2(currentBoard));
			case 3 :
				return(O3(currentBoard));
			default :
				System.out.println("Error");
				System.exit(0);
		}
		return(1);
	}
}