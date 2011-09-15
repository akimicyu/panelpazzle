import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardState implements Comparable<BoardState>{

	Board board;
	List<Character> minPath = new ArrayList<Character>();
	
	BoardState( Board board, List<Character> minPath ){
		this.board = board;
		this.minPath = minPath;
	}
	
	BoardState move(char dir) {
		BoardState newState = new BoardState( new Board(board).move(dir), nextPath(minPath, dir) );
	    return newState;
	}

	private List<Character> nextPath(List<Character> currentPath, char dir) {
		List<Character> nextPath = new ArrayList<Character>(currentPath.size());
		for(int i=0; i<currentPath.size(); i++) {
			nextPath.add(currentPath.get(i));
		}
		nextPath.add(Character.valueOf(dir));
		return nextPath;
	}
	
	boolean isEnd() {
		return Arrays.equals(board.b, board.endStatus());
	}
	
	int lv() {
		return board.lf + board.uf;
	}
	
	void printState(String message) {
		boolean[] lrud = board.isMovable();
	    System.out.println("------------------------------------------------");
	    System.out.println("-- " + message);
	    System.out.println("## path     ## ["+minPath+"]");
	    System.out.println("## movable? ## l="+lrud[0]+",r="+lrud[1]+",u="+lrud[2]+",d="+lrud[3]);
	    System.out.println("## level    ## " + lv());
	    board.print();
	}

	@Override
	public int compareTo(BoardState other) {
		if (this.lv() != other.lv()) {
			return other.lv() - this.lv(); 
		}
		return other.minPath.size() - this.minPath.size();
	}

	public String getResult() {
		StringBuilder sb = new StringBuilder();
		for (Character c : this.minPath) {
			sb.append(c);
		}
		return sb.toString();
	}

	public String key() {
		return board.key();
	}

}
