package com.uofr.course.csc442.hw.hw1.tictactoe.gameplay;

import java.util.Scanner;

import com.uofr.course.csc442.hw.hw1.tictactoe.constants.TicTacToeConstants;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameInputException;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;
import com.uofr.course.csc442.hw.hw1.tictactoe.logger.TicTacToeMetric;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Board;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.BoardType;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.CellValue;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Default2DBoard;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Move;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.NineBoard;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.OptionType;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Player;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.State;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.UltimateBoard;
import com.uofr.course.csc442.hw.hw1.tictactoe.strategy.MiniMaxStrategy;
import com.uofr.course.csc442.hw.hw1.tictactoe.strategy.RandomChoiceStrategy;
import com.uofr.course.csc442.hw.hw1.tictactoe.strategy.Strategy;
import com.uofr.course.csc442.hw.hw1.tictactoe.strategy.UserInputStrategy;

/**
 * Class to play TicTacToe Game
 * with multiple board types
 * @author tusharkumar
 */
public class Game {
	
	private State state;
	Player user;
	Player agent;
	private int scoreAgent;
	private int scoreUser;
	private int draw;
	private int maxIter;
	private int depth;
	public int moves = 0;
	//should the game be automated or not
	private boolean autoMode;
	private boolean shouldUseXAsFirstPlayer;
	private boolean shouldUseAlphaBetaPruning;
	private BoardType boardType;
	
	public TicTacToeMetric getTicTacToeMetricUser() {
		return ticTacToeMetricUser;
	}

	public TicTacToeMetric getTicTacToeMetricAgent() {
		return ticTacToeMetricAgent;
	}

	private TicTacToeMetric ticTacToeMetricUser;
	private TicTacToeMetric ticTacToeMetricAgent;	
		
	public Game(boolean autoMode, boolean shouldUseXAsFirstPlayer, 
			boolean shouldUseAlphaBetaPruning, BoardType boardType,
			int maxIter, int depth) {
		this.autoMode = autoMode;
		this.shouldUseXAsFirstPlayer = shouldUseXAsFirstPlayer;
		this.shouldUseAlphaBetaPruning = shouldUseAlphaBetaPruning;
		this.boardType = boardType;
		this.maxIter = maxIter;
		this.depth = depth;
		this.ticTacToeMetricAgent = new TicTacToeMetric();
		this.ticTacToeMetricUser = new TicTacToeMetric();
	}
	
	public void resetGameMetrics() {
		ticTacToeMetricAgent.resetMetrics();
		ticTacToeMetricUser.resetMetrics();
	}
	
	/**
	 * Method to initialize board and setup game
	 * 1. It sets up the board
	 * 2. It sets up the initial state
	 * 3. It sets up the first player
	 *     a.) If shouldUseXAsFirstPlayer then it asks for choice from user
	 *     and then picks randomly the first player cellvalue. 
	 *     If cellValue matches with user choice then user is first player
	 *     else agent is first player
	 *     b.) If not shouldUseXAsFirstPlayer then X is always the first player
	 * @throws IllegalGameInputException
	 */
	public void setUpGame() throws IllegalGameInputException {		
		user = getPlayerChoice();
		agent = user.getOtherPlayer();
		
		Board board = null;
		switch(this.boardType) {
		case NineBoard : 
			board = new NineBoard();
			break;
		case Ultimate :
			board = new UltimateBoard();
			break;
		default:
			board = new Default2DBoard(3);
			break;
		}

		Player firstPlayer;
		if(this.shouldUseXAsFirstPlayer) {			
			firstPlayer = user.getPlayerName().equalsIgnoreCase(CellValue.X.getCellValue()) ? user : agent;
		}
		else{
			if(System.nanoTime() % 2 == 0) {
				firstPlayer = user;
				System.err.println(TicTacToeConstants.getFirstPlayerPrompt(false));
			}
			else {
				firstPlayer = agent;
				System.err.println(TicTacToeConstants.getFirstPlayerPrompt(true));
			}
		}
		state = new State(firstPlayer, board);		
	}
	
	/**
	 * Method to get users choice of his board value.
	 * @return
	 * @throws IllegalGameInputException
	 */
	public Player getPlayerChoice() throws IllegalGameInputException {
		String choice;
		Player player;
		if(autoMode) {
			if(System.nanoTime() % 2 == 0) {
				choice = CellValue.X.getCellValue();
			}
			else {
				choice = CellValue.O.getCellValue();
			}
			player = Player.playerFromChoice(choice);
		}
		else{
			Scanner input = new Scanner(System.in);
			System.err.println(TicTacToeConstants.PIECE_CHOICE_PROMPT);
			choice = input.next();
			player = Player.playerFromChoice(choice);
			if(player == null) {
				throw new IllegalGameInputException(TicTacToeConstants.VALID_PIECE_CHOICE, choice);
			}
		}
		return player;
	}
	
	/**
	 * Main method to play game.
	 * Iterates over players starting from first player
	 * and asks them to make moves
	 * till either game is drawn or one player wins
	 */
	public void playGame() {
		boolean gameOver = false;
		Move lastMove = new Move(-1, -1, null, state.getCurrentPlayer().getOtherPlayer());
		state.getBoard().printBoard();
		
		Strategy userStrategy = this.autoMode ? new RandomChoiceStrategy() : new UserInputStrategy();
		Strategy agentStrategy = new MiniMaxStrategy(this.shouldUseAlphaBetaPruning, this.depth);
		while(!gameOver) {
			//Is there a move possible ?
			if(state.isMovePossible(lastMove)) {
				moves++;
				try {
					Move newlastMove = null;
					if(state.getCurrentPlayer().getPlayerName().equalsIgnoreCase(user.getPlayerName())) {
						newlastMove = makePlayersMove(lastMove, userStrategy, false);
					}
					else {
						newlastMove = makePlayersMove(lastMove, agentStrategy, true);
					}
					lastMove = newlastMove;
				} catch (IllegalGameMoveException | IllegalGameInputException e) {
					System.err.println(e.getMessage());
					continue;
				}
				//Is it a goal state ?
				if(state.isGoalState(lastMove)) {
					if(state.getCurrentPlayer().equals(agent)) {
						System.err.println(TicTacToeConstants.USER_WON_PROMPT);
						scoreUser++;						
					}
					else {
						System.err.println(TicTacToeConstants.AGENT_WON_PROMPT);
						scoreAgent++;
					}
					gameOver = true;
				}
			}
			else {
				System.err.println(TicTacToeConstants.DRAW_PROMPT);
				draw++;
				gameOver = true;
			}
		}
	}
	
	/**
	 * Helper method to make the players move.
	 * Picks the strategy for user depending
	 * on auto mode being set as true or not 
	 * @param lastMove
	 * @return
	 * @throws IllegalGameMoveException
	 * @throws IllegalGameInputException
	 */
	private Move makePlayersMove(Move lastMove, Strategy strategy, boolean isAgent) throws IllegalGameMoveException, IllegalGameInputException {
		TicTacToeMetric metric = (isAgent) ? ticTacToeMetricAgent : ticTacToeMetricUser;
		Move move = strategy.getMove(state, lastMove, metric);
		state.move(move, lastMove);			
		state.getBoard().printBoard();
		if(isAgent) {
			if(this.boardType.equals(BoardType.ThreeBoard)) {
				System.out.println(move.getMoveIndex());
			}
			else {
				System.out.println(move.getBoardIndex() + " " + move.getMoveIndex());
			}
		}
		System.err.println(TicTacToeConstants.getMoveMessage(move, isAgent));
		return move;	
	}

	/**
	 * Helper method to play the game manually 
	 * with moves being taken from console.
	 */
	public static void playManualGame(Game game) {
		int i = 1;		
		int MAX_ITER = 1;
		while(i<=MAX_ITER) {
			try {
				System.err.println("===========================================");
				game.setUpGame();
			} catch (IllegalGameInputException e) {
				System.err.println(e.getMessage());
				continue;
			}
			game.playGame();
			System.err.println("===========================================");
			System.err.println("Finished iteration - " + i);
			i++;
		}
	}
	
	/**
	 * 
	 * Helper method to play the game autoMode 
	 * with moves being taken from the strategies 
	 * itself without requiring any input from console.
	 * MaxIterations are always 1000
	 */
	public static void playGame(Game game) {
		int i = 1;				 
		while(i<=game.maxIter) {
			try {
				System.err.println("===========================================");
				game.setUpGame();
			} catch (IllegalGameInputException e) {
				System.err.println(e.getMessage());
				continue;
			}
			game.playGame();
			System.err.println("===========================================");
			System.err.println("Finished iteration - " + i);
			i++;
		}

		System.err.println("Time for User = " + game.getTicTacToeMetricUser().getTimeInMilliSeconds());
		System.err.println("StateCounts for User = " + game.getTicTacToeMetricUser().getStateCount());				
		System.err.println("Time for Agent = " + game.getTicTacToeMetricAgent().getTimeInMilliSeconds());
		System.err.println("StateCounts for Agent = " + game.getTicTacToeMetricAgent().getStateCount());
		System.err.println("Score of Agent = " + game.scoreAgent);
		System.err.println("Draw instances = " + game.draw);
		System.err.println("Score of User = " + game.scoreUser);		
		System.err.println("Max tree depth = " + game.ticTacToeMetricAgent.getMaxTreeDepth());
		//System.err.println("Max Utility = " + game.ticTacToeMetricAgent.getMaxUtility());
		//System.err.println("Min Utility = " + game.ticTacToeMetricAgent.getMinUtility());	
		
	}
	
	/**
	 * Main method to play the game. 
	 * Example run arguments - "-board three -auto true -alphabeta true -maxIter 1 -firstx true"
	 * -board : The board Type you want to play on {three, nine, ultimate}
	 * -auto : Whether you want the game to be automated with agent's opponent taking random moves {true, false}
	 * -alphabeta : If you want minimax to do alphabeta pruning {true, false}
	 * -maxIter : How many iterations of game you want to play {Integer}
	 * -firstx : Do you want to always have X as the first player ? {true, false}
	 * -depth : What depth you want minimax to be restricted to ? {Integer}
	 * Default runType - "-board three -auto false -alphabeta true -maxIter 1 -firstx true -depth 6"
	 * @param args
	 */
	public static void main(String[] args) {
		BoardType boardType = BoardType.ThreeBoard;
		boolean autoMode = false;
		boolean alphaBetaPruning = true;
		boolean firstPlayerDefaultToX = true;
		int maxIter = 1;
		int depth = 6;
		boolean isDepthProvided = false;
		if(args.length > 0) {
			for(int i=0; i<args.length; i+=2) {
				if(args[i].charAt(0) != '-') {
					throw new IllegalArgumentException("argument name not prefixed with -");
				}
				if(args[i].length() <= 1) {
					throw new IllegalArgumentException("argument name must be of more than 1 character");
				}
				OptionType argumentName = OptionType.getOptionTypeFromName(args[i].substring(1));
				if(argumentName == null) {
					throw new IllegalArgumentException("argument name must be amongst the following : " + OptionType.getValidOptionNames() + 
					" but was " + args[i].substring(1));
				}
				
				if(i+1 >= args.length) {
					throw new IllegalArgumentException("No value given for option " + argumentName.getOptionName());
				}
				String argumentValue = args[i+1];
				switch(argumentName) {
				case BoardType:
					if("nine".equalsIgnoreCase(argumentValue.toLowerCase())) {
						boardType = BoardType.NineBoard;
					}
					if("ultimate".equalsIgnoreCase(argumentValue.toLowerCase())) {
						boardType = BoardType.Ultimate;
					}
					break;
				case ITER:
					try{
						maxIter = Integer.parseInt(argumentValue);
					}
					catch(NumberFormatException e) {
						throw new IllegalArgumentException("Unable to get the integral value for iterations");
					}
					break;
				case ALPHA_BETA:
					alphaBetaPruning = Boolean.parseBoolean(argumentValue);
					break;
				case AUTO :
					autoMode = Boolean.parseBoolean(argumentValue);
					break;
				case FIRST_X:
					firstPlayerDefaultToX = Boolean.parseBoolean(argumentValue);
					break;
				case DEPTH:
					isDepthProvided = true;
					try{
						depth = Integer.parseInt(argumentValue);
					}
					catch(NumberFormatException e) {
						throw new IllegalArgumentException("Unable to get the integral value for depth");
					}
					break;
				}								
			}
			
		}
		
		if(!isDepthProvided && boardType.equals(BoardType.ThreeBoard)) {
			depth = 10;
		}
		Game game = new Game(autoMode, firstPlayerDefaultToX, 
				alphaBetaPruning, boardType, maxIter, depth);
		playGame(game);
	}

}
