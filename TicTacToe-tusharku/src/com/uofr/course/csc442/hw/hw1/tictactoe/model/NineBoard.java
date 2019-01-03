package com.uofr.course.csc442.hw.hw1.tictactoe.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.uofr.course.csc442.hw.hw1.tictactoe.constants.TicTacToeConstants;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameInputException;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;
import com.uofr.course.csc442.hw.hw1.tictactoe.utils.BoardUtils;

/**
 * Class to contain the implementation of rules 
 * and game play of NineBoard.
 * It uses some of the methods from its base class
 * {@link Default2DBoard} but mostly it needs its own
 * implementations as the rules of playing differ.
 * @author tusharkumar
 *
 */
public class NineBoard extends Default2DBoard {
	
	protected final static int BOARD_GRID_SIZE = 9;
	protected final static int BOARD_SUB_GRID_SIZE = 3;
	public Map<Integer, Set<Integer>> possibleMovesMapping;
	public Map<Integer, CellValue> winningBoardToPlayerMapping;
	
	public NineBoard() {
		super(BOARD_GRID_SIZE);		
		initializePossibleMoves();
		this.winningBoardToPlayerMapping = new HashMap<Integer, CellValue>();
	}

	private void initializePossibleMoves() {
		possibleMovesMapping = new HashMap<Integer, Set<Integer>>();
		for(int i=1; i<=BOARD_GRID_SIZE; i++) {
			Set<Integer> moveSet = new HashSet<Integer>();
			for(int j=1; j<=BOARD_SUB_GRID_SIZE*BOARD_SUB_GRID_SIZE; j++) {
				moveSet.add(j);
			}
			possibleMovesMapping.put(i, moveSet);
		}
	}
	
	/**
	 * Evaluation function for heuristic evaluation of minimax
	 * Each sub grid board is evaluated using the heuristics for 
	 * {@link Default2DBoard} and then summed together based
	 * on the weight given to current Board.
	 */
	@Override
	public double evaluateHeuristicForMinMax(Move lastMove, double maxUtility, double minUtility) {		
		Player winner = this.getWinner();
		if(winner != null) {
			return winner.isMax() ? maxUtility : minUtility;
		}
		else {						
			double currentBoardUtility = 0;
			for(int i=0; i<BOARD_GRID_SIZE; i=i+BOARD_SUB_GRID_SIZE) {
				for(int j=0; j<BOARD_GRID_SIZE; j=j+BOARD_SUB_GRID_SIZE) {
					CellValue[][] tempBoard = new CellValue[BOARD_SUB_GRID_SIZE][BOARD_SUB_GRID_SIZE];
					for(int row=i; row<=i+2;row++) {
						for(int col=j; col<= j+2; col++) {
							tempBoard[row-i][col-j] = this.getGameBoard()[row][col];
						}
					}		
					int winnerIndex = BoardUtils.isBoardComplete(tempBoard, BOARD_SUB_GRID_SIZE);
					if(winnerIndex != -1) {
						int rowWinnerIndex = (winnerIndex-1) / BOARD_SUB_GRID_SIZE;
						int colWinnerIndex = (winnerIndex-1) - BOARD_SUB_GRID_SIZE*rowWinnerIndex;
						if(tempBoard[rowWinnerIndex][colWinnerIndex] == CellValue.X) {
							return maxUtility;
						}
						else {
							return minUtility;
						}
					}
					int gameWinPositionsForX = BoardUtils.getGameWinPositions(tempBoard, CellValue.X, BOARD_SUB_GRID_SIZE);
					int gameWinPositionsForO = BoardUtils.getGameWinPositions(tempBoard, CellValue.O, BOARD_SUB_GRID_SIZE);
					currentBoardUtility += 2*(gameWinPositionsForX - gameWinPositionsForO) + 
								(BoardUtils.evaluateHeuristicForMinMax(tempBoard, BOARD_SUB_GRID_SIZE));
				}
			}
			return currentBoardUtility;			
		}
	}


	/**
	 * Find if there are empty positions present
	 * based on the last move.
	 */
	@Override
	public boolean emptyPositionsPresent(Move lastMove) {
		boolean val = false;
		if(lastMove.getBoardIndex() == -1) {
			//Must be first move
			val = true;
		}
		else {
			for(int i=1;i<=BOARD_GRID_SIZE; i++) {
				if(!possibleMovesMapping.get(i).isEmpty()) {
					val = true;
					break;
				}
			}
		}
		return val;
		
	}


	@Override
	public void makeMove(Move move, Move lastMove) throws IllegalGameMoveException {
		int moveIndex = move.getMoveIndex();
		Player playerOfMove = move.getPlayerOfMove();
		validateMove(move, lastMove);
		int startBoardRowIndex = ((move.getBoardIndex()-1)/BOARD_SUB_GRID_SIZE) * BOARD_SUB_GRID_SIZE;
		
		int startBoardColIndex = ((move.getBoardIndex()-1)%BOARD_SUB_GRID_SIZE) * BOARD_SUB_GRID_SIZE;
		
		int rowIndex = startBoardRowIndex + ((moveIndex-1) / BOARD_SUB_GRID_SIZE);
		int colIndex = startBoardColIndex + ((moveIndex-1) - ((BOARD_SUB_GRID_SIZE*(rowIndex - startBoardRowIndex))));		
		
		this.getGameBoard()[rowIndex][colIndex] = CellValue.valFromPlayer(playerOfMove.getPlayerName());
		possibleMovesMapping.get(move.getBoardIndex()).remove(moveIndex);

		CellValue[][] tempBoard = new CellValue[BOARD_SUB_GRID_SIZE][BOARD_SUB_GRID_SIZE];		
		for(int i=startBoardRowIndex; i<startBoardRowIndex+BOARD_SUB_GRID_SIZE;i++) {
			for(int j=startBoardColIndex; j<startBoardColIndex+BOARD_SUB_GRID_SIZE;j++) {
				tempBoard[i-startBoardRowIndex][j-startBoardColIndex] = this.getGameBoard()[i][j];
			}
		}
		int winIndex = BoardUtils.isBoardComplete(tempBoard, BOARD_SUB_GRID_SIZE);			
		if(winIndex != -1) {
			int winnerRowIndex = (winIndex-1) / BOARD_SUB_GRID_SIZE;
			int winnerColIndex = (winIndex-1) - BOARD_SUB_GRID_SIZE*winnerRowIndex;
			winningBoardToPlayerMapping.put(move.getBoardIndex(), tempBoard[winnerRowIndex][winnerColIndex]);
			possibleMovesMapping.get(move.getBoardIndex()).clear();
		}
	}


	/**
	 * Method to print the board on the console 
	 * beautifully with tabs and spaces
	 */
	@Override	
	public void printBoard() {
		System.err.println("------------------");
		for(int i=0;i<BOARD_GRID_SIZE;i++) {
			for(int j=0; j<BOARD_GRID_SIZE;j++) {
				System.err.print(this.getGameBoard()[i][j] + " ");
				if(((j+1) % BOARD_SUB_GRID_SIZE) == 0) {
					System.err.print("\t");
				}
			}
			System.err.println();
			if(((i+1) % BOARD_SUB_GRID_SIZE) == 0) {
				System.err.print("\n\n");
			}
		}
		System.err.println("------------------");
	}


	/**
	 * Validation of a move for {@link NineBoard} with following rules:
	 * a.) Move position between 1 and (BOARD_GRID_SIZE)
	 * b.) Board index between 1 and BOARD_GRID_SIZE
	 * c.) Move not accordance with lastMove
	 */
	@Override
	public void validateMove(Move move, Move lastMove) throws IllegalGameMoveException {
		int lastMoveIndex = lastMove.getMoveIndex();
		int moveIndex = move.getMoveIndex();
		if(moveIndex <= 0 || moveIndex > BOARD_GRID_SIZE) {			
			throw new IllegalGameMoveException(moveIndex + TicTacToeConstants.SPACE + TicTacToeConstants.INPUT_RANGE_PROMPT + 
					TicTacToeConstants.LEFT_BRACES + TicTacToeConstants.ONE + " - "+ BOARD_SUB_GRID_SIZE*BOARD_SUB_GRID_SIZE + TicTacToeConstants.RIGHT_BRACES);
		}
		
		if(move.getBoardIndex() <= 0 || move.getBoardIndex() > BOARD_GRID_SIZE) {			
			throw new IllegalGameMoveException(move.getBoardIndex()  + TicTacToeConstants.SPACE + 
					TicTacToeConstants.INPUT_RANGE_PROMPT + TicTacToeConstants.LEFT_BRACES + 
					TicTacToeConstants.ONE + " - "+ BOARD_GRID_SIZE + TicTacToeConstants.RIGHT_BRACES);
		}
		
		if(lastMoveIndex != -1) {
			boolean isMyBoardFull = isMyBoardFull(lastMoveIndex);
			boolean isNewBoardAlsoFull = isMyBoardFull(move.getBoardIndex());
			if(isMyBoardFull) {
				//Board Full since completely occupied or being won/lost
				if(move.getBoardIndex() == lastMoveIndex || isNewBoardAlsoFull) {
					throw new IllegalGameMoveException("Board Already Captured");
				}
				else {
					if(getCellValueFromMove(move) != CellValue.BLANK) {
						throw new IllegalGameMoveException(TicTacToeConstants.BOARD_POSITION_OCCUPIED);
					}
				}
			}
			else {
				//Board is not full
				
				if(move.getBoardIndex() != lastMoveIndex) {
					throw new IllegalGameMoveException(TicTacToeConstants.MOVE_LASTMOVE_MISMATCH_PROMPT);
				}
				else {
					if(getCellValueFromMove(move) != CellValue.BLANK) {
						throw new IllegalGameMoveException(TicTacToeConstants.BOARD_POSITION_OCCUPIED);
					}
				}
			}			
		}
		else {						
			if(getCellValueFromMove(move) != CellValue.BLANK) {
				throw new IllegalGameMoveException(TicTacToeConstants.BOARD_POSITION_OCCUPIED);
			}
		}
	}

	private CellValue getCellValueFromMove(Move move) {
		int startBoardRowIndex = ((move.getBoardIndex()-1)/BOARD_SUB_GRID_SIZE) * BOARD_SUB_GRID_SIZE;
		
		int startBoardColIndex = ((move.getBoardIndex()-1)%BOARD_SUB_GRID_SIZE) * BOARD_SUB_GRID_SIZE;
		
		int rowIndex = startBoardRowIndex + ((move.getMoveIndex()-1) / BOARD_SUB_GRID_SIZE);
		int colIndex = startBoardColIndex + ((move.getMoveIndex()-1) - ((BOARD_SUB_GRID_SIZE*(rowIndex - startBoardRowIndex))));
		return this.getGameBoard()[rowIndex][colIndex];
	}
	
	protected boolean isMyBoardFull(int lastMoveIndex) {
		boolean isMyBoardFull = possibleMovesMapping.get(lastMoveIndex).isEmpty();
		return isMyBoardFull;
	}

	/**
	 * Helper method for helping minimax
	 * to clear the moves it made in order
	 * to look ahead
	 */
	@Override
	public void clearMove(Move move) {
		int moveIndex = move.getMoveIndex();
		int startBoardRowIndex = ((move.getBoardIndex()-1)/BOARD_SUB_GRID_SIZE) * BOARD_SUB_GRID_SIZE;
		
		int startBoardColIndex = ((move.getBoardIndex()-1)%BOARD_SUB_GRID_SIZE) * BOARD_SUB_GRID_SIZE;
		
		int rowIndex = startBoardRowIndex + ((moveIndex-1) / BOARD_SUB_GRID_SIZE);
		int colIndex = startBoardColIndex + ((moveIndex-1) - ((BOARD_SUB_GRID_SIZE*(rowIndex - startBoardRowIndex))));
		this.getGameBoard()[rowIndex][colIndex] = CellValue.BLANK;
		possibleMovesMapping.get(move.getBoardIndex()).add(moveIndex);
		if(winningBoardToPlayerMapping.containsKey(move.getBoardIndex())) {		
			CellValue[][] tempBoard = new CellValue[BOARD_SUB_GRID_SIZE][BOARD_SUB_GRID_SIZE];
			Set<Integer> blankCells = new HashSet<Integer>();
			int indexOfMove = 1;
			for(int i=startBoardRowIndex; i<startBoardRowIndex+BOARD_SUB_GRID_SIZE;i++) {
				for(int j=startBoardColIndex; j<startBoardColIndex+BOARD_SUB_GRID_SIZE;j++) {
					tempBoard[i-startBoardRowIndex][j-startBoardColIndex] = this.getGameBoard()[i][j];
					if(this.getGameBoard()[i][j] == CellValue.BLANK) {
						blankCells.add(indexOfMove);
					}
					indexOfMove++;
				}
			}
			int winIndex = BoardUtils.isBoardComplete(tempBoard, BOARD_SUB_GRID_SIZE);			
			if(winIndex == -1) {
				winningBoardToPlayerMapping.remove(move.getBoardIndex());
				possibleMovesMapping.put(move.getBoardIndex(), blankCells);
			}
		}
	}

	/**
	 * Method to fetch the NineBoard input from user
	 */
	@Override
	public Move getMoveInputFromUser(Move lastMove) throws IllegalGameInputException {
		Scanner input = new Scanner(System.in);
		System.err.println(TicTacToeConstants.USER_MOVE_PROMPT);			
		String boardIndexStr = input.next();
		String moveIndexStr = input.next();
		try {
			int boardIndex = Integer.parseInt(boardIndexStr); 
			int moveIndex = Integer.parseInt(moveIndexStr);
			
			Move move = new Move(moveIndex, boardIndex, lastMove, lastMove.getPlayerOfMove().getOtherPlayer());
			return move;
		}
		catch(NumberFormatException e) {
			throw new IllegalGameInputException(TicTacToeConstants.BOARD_POS_AND_MOVE_POS + " should be integer between 1-9", 
					"board position given was " + boardIndexStr + " and move position given was " + moveIndexStr);
		}
	}
	
	@Override
	public boolean isBoardComplete(Move move) {	
		return (winningBoardToPlayerMapping.get(move.getBoardIndex()) != null);
	}


	/**
	 * Method to get all possible moves
	 * given only a lastMove 
	 */
	@Override
	public List<Move> getPossibleMoves(Move lastMove) {
		List<Move> moveList = new ArrayList<Move>();
		int lastMoveIndex = lastMove.getMoveIndex();
		if(lastMoveIndex == -1) {			
			for(int boardIndex = 1; boardIndex <= BOARD_GRID_SIZE; boardIndex++) {
				Set<Integer> possibleMoveIndexes = possibleMovesMapping.get(boardIndex);
				if(possibleMoveIndexes != null && !possibleMoveIndexes.isEmpty()) {
					for(int possibleMoveIndex : possibleMoveIndexes) {
						moveList.add(new Move(possibleMoveIndex, boardIndex,
							lastMove, lastMove.getPlayerOfMove().getOtherPlayer()));
					}
				}
			}
		}
		else {
			Set<Integer> possibleMoveIndexes = possibleMovesMapping.get(lastMove.getMoveIndex());
			if(possibleMoveIndexes != null && !possibleMoveIndexes.isEmpty())
			{
				for(int possibleMoveIndex : possibleMoveIndexes) {
					moveList.add(new Move(possibleMoveIndex, lastMove.getMoveIndex(),
						lastMove, lastMove.getPlayerOfMove().getOtherPlayer()));
				}
			}
			else {
				for(int boardIndex = 1; boardIndex <= BOARD_GRID_SIZE; boardIndex++) {
					Set<Integer> allPossibleMoveIndexes = possibleMovesMapping.get(boardIndex);
					if(allPossibleMoveIndexes != null && !allPossibleMoveIndexes.isEmpty()) {
						for(int possibleMoveIndex : allPossibleMoveIndexes) {
							moveList.add(new Move(possibleMoveIndex, boardIndex,
								lastMove, lastMove.getPlayerOfMove().getOtherPlayer()));
						}
					}
				}				
			}
		}
		return moveList;
	}

	@Override
	public Player getWinner() {
		Player winner = null;
		if(!winningBoardToPlayerMapping.keySet().isEmpty()) {
			for(Map.Entry<Integer, CellValue> entry : winningBoardToPlayerMapping.entrySet()) {
				winner = Player.playerFromChoice(entry.getValue().getCellValue());
				break;
			}
		}
		return winner;
	}	
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		
		str.append("\n------------------\n");
		for(int i=0;i<BOARD_GRID_SIZE;i++) {
			for(int j=0; j<BOARD_GRID_SIZE;j++) {
				str.append(this.getGameBoard()[i][j] + " ");
				if(((j+1) % BOARD_SUB_GRID_SIZE) == 0) {
					str.append("\t");
				}
			}
			str.append("\n");
			if(((i+1) % BOARD_SUB_GRID_SIZE) == 0) {
				str.append("\n");
			}
		}
		str.append("\n------------------\n");
		return str.toString();
	}
}
