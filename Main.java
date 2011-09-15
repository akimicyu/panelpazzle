import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {

	public static void main(String[] args) throws IOException{
		
		String loadFile   = args.length >= 1 ? args[0] : "problems.csv";
		String answerFile = args.length >= 2 ? args[1] : "answer.txt";
		String knownAnsFile = args.length >= 3 ? args[2] : "knownAnswer.txt";
		
		Problem problem = QuizReader.loadProblem(loadFile);
		
		File answer = new File(answerFile);
		if(answer.exists()) { answer.delete(); }
		PrintWriter pw = new PrintWriter(new FileWriter(answerFile));
		
		List<String> knownAnswer = QuizReader.loadAnswer(knownAnsFile);
		
		int turn_max = 3000000; // レベルアップ後、この回数超えたらあきらめる
		
		for (int b=0; b<problem.boardList.size(); b++) {
			Board board = problem.boardList.get(b);
			
			BoardState startState = new BoardState(board, new ArrayList<Character>());
			startState.board.check();
			startState.printState("startState:" + b);
			
			HashMap<String, BoardState> stateMap = new HashMap<String, BoardState>(1024*8);
			stateMap.put(startState.key(), startState);
			StateQueue stateQueue = new StateQueue();
			stateQueue.add(startState);
			
			if ( board.w * board.h > 16 || !knownAnswer.get(b).equals("")){
				System.out.printf("[%d][%d,%d]%s -> %s : PASS!\n",
						b, board.w, board.h, String.valueOf(board.b), String.valueOf(board.endStatus()));
				pw.printf("\n");
				continue;
			}

			boolean running = true;
			int turn = 0;
			String result = null;
			int current_lv = 0; // 現在のレベル
			BoardState last = startState;
			boolean levelUp = false;
			
			while (running && turn < turn_max && stateQueue.size()>0) {

				BoardState state = stateQueue.poll();
			    List<Character> movable = state.board.movables();
			    
			    for(Character dir : movable) {
			    	BoardState nextState = state.move(dir);
			    	if (!stateMap.containsKey(nextState.key())) {
			    		boolean[] filled_wh = nextState.board.check();
			    		if (filled_wh[0] || filled_wh[1]) {
			    			nextState.board.maskUpdate();
			    			if(current_lv < nextState.lv()) {
				    			current_lv = nextState.lv();
				    			levelUp = true;
				    			nextState.board.maskUpdate();
				    			stateMap.clear();
				    			stateQueue.clear(current_lv-1);
				    			turn = 0;
			    			} else {
			    				levelUp = false;
			    			}
			    		}
			    		if (nextState.isEnd()) {
			    			running = false;
			    			result  = nextState.getResult();
			    		}
				    	stateQueue.add(nextState);
				    	stateMap.put(nextState.key(), nextState);
				    	last = nextState;
			    	}
			    }
			    
			    turn++;
			    
			    if (turn % 100000 == 0 || levelUp) {
			    	System.out.printf("[Board]%d, [turn]%d, [qs]%s, [hs]%d [last]%s(lf=%d,uf=%d) [mask]%s [lv]%d\n",
			    			b, turn, stateQueue.toString(), stateMap.size(), last.key(), last.board.lf, last.board.uf, last.board.mask, current_lv);
			    }
			}
			
			if(result != null) {
				System.out.printf("[%d][%d,%d]%s -> %s : %s\n",
						b, board.w, board.h, String.valueOf(startState.board.b), String.valueOf(board.endStatus()), result);
				pw.printf("%s\n",result);
			} else {
				String message = (turn==turn_max) ? "turn_max" : "queue_end";
				System.out.printf("[%d][%d,%d]%s -> %s : not_found! (%s)\n",
						b, board.w, board.h, String.valueOf(startState.board.b), String.valueOf(board.endStatus()), message);
				pw.printf("%s\n",message);
			}
			
			pw.flush();
			
		}
		
		pw.close();
		System.out.println("END");

	}

}
