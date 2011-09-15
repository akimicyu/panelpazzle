import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Board {
	int w, h;
	char[] b, e;
	int uf = 0, lf = 0;
	Set<Character> mask = new HashSet<Character>();	// 現時点で解空間の関心内となるパネル
	
	static final int MASK_MIN = 12;
	
	static char[] asc = new char[] {
		'1','2','3','4','5','6','7','8','9',
		'A','B','C','D','E','F','G','H','I',
		'J','K','L','M','N','O','P','Q','R',
		'S','T','U','V','W', 'X','Y','Z'
	};
	
	Board(int w, int h, char[] b) {
		this.w = w;
		this.h = h;
		this.b = b;
		maskUpdate();
	}
	
	public Board(Board board) {
		this( board.w, board.h, board.b.clone());
		this.e = board.e;
	}

	void maskUpdate() {
		mask.clear();
		char[] e = endStatus();
		for (int i=0; i<e.length; i++) { // 埋まっている行
			if ((i%w < lf) && (e[i]!='=')) {
				mask.add(Character.valueOf(e[i]));
			}
		}		
		for (int i=0; i<e.length; i++) { // 埋まっている列
			if ((i/w < uf) && (e[i]!='=')) {
				mask.add(Character.valueOf(e[i]));
			}
		}
		
		int w_len = w - lf;
		int h_len = h - uf;
		if (w_len >= h_len) { // 更に１行（列）
			for (int i=0; i<e.length; i++) {
				if ((i%w <= lf) && (e[i]!='=')) {
					mask.add(Character.valueOf(e[i]));
				}
			}
		} else {
			for (int i=0; i<e.length; i++) {
				if ((i/w <= uf) && (e[i]!='=')) {
					mask.add(Character.valueOf(e[i]));
				}
			}
		}
	}	
	
	String key() {
		StringBuilder sb = new StringBuilder();
		if (freeSpace() <= 9) { // 十分小さいからマスクを使わない
			for (int i=0; i<b.length; i++) {
				sb.append(b[i]);
			}
		} else {
			for (int i=0; i<b.length; i++) {
				if(b[i]=='0'||b[i]=='=') {
					sb.append(b[i]);
				} else if( mask.contains(Character.valueOf(b[i])) ) {
					sb.append(b[i]);
				} else { // 今の関心の対象外
					sb.append("_");
				}
			}
		}
		String key = sb.toString();
		return key;
	}
	
	char[] endStatus() {
		if (this.e != null) {
			return this.e;
		}
		char[] buff = new char[b.length];
		String b_str = new String(b);
		for (int i=0; i<buff.length-1; i++) {
			buff[i] = (b_str.indexOf(asc[i]) >= 0)
					? asc[i] : '=';
		}
		buff[buff.length-1] = '0';
		this.e = buff;
		return this.e;
	}
	
	int index(char[] b, char c) {
		for(int i=0; i<b.length; i++) {
			if(b[i]==c) return i;
		}
		return -1;
	}
	
	List<Character> movables() {
		List<Character> movables = new ArrayList<Character>();
		boolean[] im = isMovable();
		if( im[0] == true) { movables.add(Character.valueOf('l')); } 
		if( im[1] == true) { movables.add(Character.valueOf('r')); } 
		if( im[2] == true) { movables.add(Character.valueOf('u')); } 
		if( im[3] == true) { movables.add(Character.valueOf('d')); }
		return movables;
	}
	
	boolean[] isMovable() {
		int pos = index(b, '0');
		boolean l = (pos % w > lf)       && b[pos-1] != '=' ;
		boolean r = (pos % w != (w-1))   && b[pos+1] != '=' ;
		boolean u = (pos >= w*(1+uf))    && b[pos-w] != '=' ;
		boolean d = (pos+w < b.length)   && b[pos+w] != '=' ;
		boolean[] b = new boolean[] { l, r, u, d };
		return b;
	}
	
	void swap(int i, int j) {
		char tmp = b[i];
		b[i] = b[j];
		b[j] = tmp;
	}
	
	Board move(char dir) {
		int pos = index(b, '0');
		int moveTo = 0;
		switch (dir) {
		case 'l':
			moveTo = pos - 1;
			break;
		case 'r':
			moveTo = pos + 1;
			break;
		case 'u':
			moveTo = pos - w;
			break;
		case 'd':
			moveTo = pos + w;
			break;
		default:
			throw new RuntimeException();
		}
		swap(pos,moveTo);
		return this;
	}
	
	void print() {
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				System.out.print(b[w*i+j]);
				System.out.print(' ');
			}
			System.out.println();
		}
	}
	
	boolean[] check() {
	    boolean w_filled = true;
	    boolean h_filled = true;
	    for(int i=0; i<h; i++) {
	    	int pos = w * (i) + lf;
	    	if (b[pos] != '=' && b[pos] != asc[pos]) {
	    		w_filled = false;
	    	}
	    }
	    for(int j=0; j<w; j++) {
	    	int pos = w * uf + j;
	    	if (b[pos] != '=' && b[pos] != asc[pos]) {
	    		h_filled = false;
	    	}
	    }
	    if(w_filled||h_filled) {
			if (w_filled) { this.lf +=1; }
			if (h_filled) { this.uf +=1; }
			maskUpdate();
		}
	    return new boolean[]{ w_filled, h_filled};
	}
	
	public int freeSpace() {
		int w_len = w - lf;
		int h_len = h - uf;
		return w_len * h_len;
	}
	
}
