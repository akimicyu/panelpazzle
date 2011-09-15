import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class QuizReader {

	static List<String> loadAnswer(String knownAnsFile) {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(knownAnsFile));
			List<String> rtn = new ArrayList<String>();
			String line;
			while ((line = bfr.readLine()) != null) {
				rtn.add(line);
			}			
			return rtn;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static Problem loadProblem(String fileString) {
		Problem p = new Problem();
		p.boardList = new ArrayList<Board>();
		
		try {
			FileReader inFile = new FileReader(fileString);
			BufferedReader inBuffer = new BufferedReader(inFile);
			String line;
			int i = 0;
			while ((line = inBuffer.readLine()) != null) {
				if(i==0) {
					String[] a = line.split(" ");
					p.lx = Integer.parseInt(a[0]);
					p.rx = Integer.parseInt(a[1]);
					p.ux = Integer.parseInt(a[2]);
					p.dx = Integer.parseInt(a[3]);
				} else if(i==1) {
					p.n  = Integer.parseInt(line);
				} else {
					String[] a = line.split(",");
					int w = Integer.parseInt(a[0]);
					int h = Integer.parseInt(a[1]);
					char[] b = a[2].toCharArray();
					p.boardList.add(new Board(w, h, b));
				}
				i++;
			}
			return p;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
