package com.uofr.course.csc442.hw.hw1.tictactoe.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.uofr.course.csc442.hw.hw1.tictactoe.constants.TicTacToeConstants;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameInputException;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;
import com.uofr.course.csc442.hw.hw1.tictactoe.utils.BoardUtils;

/**
 * Default 2D board used to play TicTacToe
 * for 3*3 board.
 * @author tusharkumar
 *
 */
public class Default2DBoard implements Board {
	
	private CellValue[][] gameBoard;

	private int boardGridSize;
	private int emptyPos;
	
	public Default2DBoard(int boardGridSize) {
		this.gameBoard = new CellValue[boardGridSize][boardGridSize];
		this.boardGridSize = boardGridSize;
		initializeBoard();
	}
	
	public CellValue[][] getGameBoard() {
		return gameBoard;
	}
	
	public int getBoardGridSize() {
		return boardGridSize;
	}
	
	public boolean emptyPositionsPresent(Move lastMove) {
		return emptyPos > 0;
	}
	
	/**
	 * Given a move, this method
	 * a.) Validates the move
	 * b.) Makes the move
	 * c.) Decreases count of emptyPos
	 */
	@Override
	public void makeMove(Move move, Move lastMove) throws IllegalGameMoveException {
		Player playerOfMove = move.getPlayerOfMove();
		validateMove(move, lastMove);
		int rowIndex = move.getMoveIndexData(boardGridSize).getRowIndex();
		int colIndex = move.getMoveIndexData(boardGridSize).getColIndex();
		emptyPos--;
		gameBoard[rowIndex][colIndex] = CellValue.valFromPlayer(playerOfMove.getPlayerName());
		
	}	
	
	/**
	 * Helper method for Minimax to clear its 
	 * move and restoring board to the state it was 
	 * just before making the move.
	 */
	@Override
	public void clearMove(Move move) {	
		int rowIndex = move.getMoveIndexData(boardGridSize).getRowIndex();
		int colIndex = move.getMoveIndexData(boardGridSize).getColIndex();
		emptyPos++;
		gameBoard[rowIndex][colIndex] = CellValue.BLANK;
		
	}
		
	@Override
	public List<Move> getPossibleMoves(Move lastmove) {
		List<Move> moveList = new ArrayList<Move>();
		if(emptyPos == 0) {
			return moveList;
		}
		else{
			for(int boardGridIndexRow=0; boardGridIndexRow<boardGridSize; boardGridIndexRow++) {
				for(int boardGridIndexCol=0; boardGridIndexCol<boardGridSize; boardGridIndexCol++) {
					if(gameBoard[boardGridIndexRow][boardGridIndexCol] == CellValue.BLANK) {
						moveList.add(new Move((boardGridIndexRow*boardGridSize + boardGridIndexCol+1), 
								0, lastmove, lastmove.getPlayerOfMove().getOtherPlayer()));
					}
				}
			}
		}
		return moveList;
	}
	
	@Override
	public Player getWinner() {
		return getWinner(gameBoard, boardGridSize);
		
	}
	
	/**
	 * Method to request the user through 
	 * console for a input
	 */
	@Override
	public Move getMoveInputFromUser(Move lastMove) throws IllegalGameInputException {
		Scanner input = new Scanner(System.in);
		System.err.println(TicTacToeConstants.USER_MOVE_PROMPT);
		String choiceStr = input.next();
		try {			
			int choice = Integer.parseInt(choiceStr);
			Move move = new Move(choice, 0, lastMove, lastMove.getPlayerOfMove().getOtherPlayer());
			return move;
		}
		catch(NumberFormatException e) {
			throw new IllegalGameInputException(TicTacToeConstants.INTEGER_RANGE_BOARD, choiceStr);
		}
	}
	
	/**
	 * Method to evaluate the heuristic value 
	 * for the board.
	 * This value will be used by Mini max.
	 * It uses the heuristic function of finding the 
	 * difference in count of continuous two rows
	 * or continuous two columns.
	 */
	@Override
	public double evaluateHeuristicForMinMax(Move lastMove, double maxUtility, double minUtility) {
		Player player = this.getWinner();
		if(player != null) {
			if(player.isMax()) {
				return maxUtility;
			}
			else {
				return minUtility;
			}
		}
		else {			
			int gameWinPositionsForX = BoardUtils.getGameWinPositions(gameBoard, CellValue.X, boardGridSize);
			int gameWinPositionsForO = BoardUtils.getGameWinPositions(gameBoard, CellValue.O, boardGridSize);				
			double countDiff = BoardUtils.evaluateHeuristicForMinMax(this.gameBoard, boardGridSize);
			return (countDiff) + 2*(gameWinPositionsForX - gameWinPositionsForO);	
		}
	}
	
	@Override
	public void printBoard() {
		BoardUtils.printBoard(gameBoard, boardGridSize);
	}
	
	/**
	 * Method to setup the board for game play
	 */
	private void initializeBoard() {
		for(int boardGridIndexRow=0; boardGridIndexRow<boardGridSize; boardGridIndexRow++) {
			for(int boardGridIndexCol=0; boardGridIndexCol<boardGridSize; boardGridIndexCol++) {
				gameBoard[boardGridIndexRow][boardGridIndexCol] = CellValue.BLANK;	
				emptyPos++;
			}
		}		
	}	

	/**
	 * Validate move just ensures these two conditions are met:
	 * a.) Move is within the range
	 * b.) Move is not at a position that is already occupied
	 */
	public void validateMove(Move move, Move lastMove) throws IllegalGameMoveException {
		int choice = move.getMoveIndex();
		if(choice <= 0 || choice > boardGridSize*boardGridSize) {			
			throw new IllegalGameMoveException(choice + TicTacToeConstants.SPACE + TicTacToeConstants.INPUT_RANGE_PROMPT + 
					TicTacToeConstants.LEFT_BRACES + TicTacToeConstants.ONE + " - "+ boardGridSize*boardGridSize + TicTacToeConstants.RIGHT_BRACES);
		}		
		int rowIndex = move.getMoveIndexData(boardGridSize).getRowIndex();
		int colIndex = move.getMoveIndexData(boardGridSize).getColIndex();
		if(gameBoard[rowIndex][colIndex] != CellValue.BLANK) {
			throw new IllegalGameMoveException(TicTacToeConstants.BOARD_POSITION_OCCUPIED);
		}		
	}
	
	/**
	 * Method to check if the given move has made the
	 * board complete.
	 */
	public boolean isBoardComplete(Move move) {
		return BoardUtils.isBoardComplete(move.getMoveIndex(), this.gameBoard, boardGridSize);
	}
	
	/**
	 * Given a board, this method finds the player winning the board
	 * and returns null if nobody is.
	 * @param gameBoard
	 * @param boardGridSize
	 * @return
	 */
	protected Player getWinner(CellValue[][] gameBoard, int boardGridSize) {
		Player player = null;
		int winIndex = BoardUtils.isBoardComplete(gameBoard, boardGridSize);					
		if(winIndex != -1) {
			int winnerRowIndex = (winIndex-1) / boardGridSize;
			int winnerColIndex = (winIndex-1) - boardGridSize*winnerRowIndex;
			player =  Player.playerFromChoice(gameBoard[winnerRowIndex][winnerColIndex].getCellValue());
		}		
		return player;
	}	
}
