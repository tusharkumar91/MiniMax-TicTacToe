package com.uofr.course.csc442.hw.hw1.tictactoe.model;

import java.util.List;

import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameInputException;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;

/**
 * Interface which captures the specific game moves/rules
 * for each of the different board.
 * Each core implementation provides it own 
 * manner of looking for a move, choosing whats best
 * and then making a move.
 * @author tusharkumar
 *
 */
public interface Board {
	
	
	public boolean emptyPositionsPresent(Move move);
			
	/**
	 * Method to print the board on the console
	 */
	public void printBoard();
	
	/**
	 * Method to validate if the current move that was asked 
	 * to the board to be made
	 * @param move - the current move being requested to be made
	 * @param lastMove - the last move that was made in the board
	 * @throws IllegalGameMoveException
	 */
	public void validateMove(Move move, Move lastMove) throws IllegalGameMoveException; 
	
	/**
	 * Method to find if the board is already filled 
	 * or there are no possible moves whilst adhering
	 * to the rules of the board
	 * @param move
	 * @return
	 */
	public boolean isBoardComplete(Move move);
	
	/**
	 * Method to get possible moves for the program
	 * @param lastmove
	 * @return
	 */
	public List<Move> getPossibleMoves(Move lastmove);

	/**
	 * Method to help minimax clear a move it made
	 * in order to 'think' for whats the best move
	 * @param move
	 */
	public void clearMove(Move move);
	
	public Player getWinner();

	public Move getMoveInputFromUser(Move lastMove) throws IllegalGameInputException;

	/**
	 * Method to actually make the move. It actually calls
	 * validateMove function before making the move. Hence
	 * the exception being thrown
	 * @param move
	 * @param lastMove
	 * @throws IllegalGameMoveException
	 */
	public void makeMove(Move move, Move lastMove) throws IllegalGameMoveException;

	/**
	 * Helper method for minimax to evaluate the heuristic utility
	 * value of board in case we havent found winner and there are
	 * still moves possible.
	 * @param lastMove
	 * @param maxUtility
	 * @param minUtility
	 * @return
	 */
	public double evaluateHeuristicForMinMax(Move lastMove, double maxUtility, double minUtility);
	
}
