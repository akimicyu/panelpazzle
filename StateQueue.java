import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class StateQueue {
	
	ArrayList<LinkedList<BoardState>> list = new ArrayList<LinkedList<BoardState>>();
	
	public StateQueue() {
		list.add(new LinkedList<BoardState>());
	}

	public int size() {
		int size = 0;
		for (LinkedList<BoardState> q : list) {
			size += q.size();
		}
		return size;
	}

	public boolean add(BoardState state) {
		while (state.lv() >= list.size()) {
			list.add(new LinkedList<BoardState>());
		}
		return list.get(state.lv()).add(state);
	}

	public BoardState poll() {
		BoardState state = null;
		for (int i = list.size()-1; i >= 0; i--) {
			LinkedList<BoardState> queue = list.get(i);
			state = queue.poll();
			if(state!=null) return state;
		}
		return state;
	}

	@Override
	public String toString() {
		int[] a = new int[list.size()];
		for(int i=0; i<list.size(); i++) {
			a[i] = list.get(i).size();
		}
		return Arrays.toString(a);
	}
	
	public void clear(int lv) {
		for(int i=0; i<=lv; i++) {
			list.get(i).clear();
		}
	}

}
