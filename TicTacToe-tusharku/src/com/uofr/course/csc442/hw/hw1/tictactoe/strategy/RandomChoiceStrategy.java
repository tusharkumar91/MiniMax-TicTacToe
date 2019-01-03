package com.uofr.course.csc442.hw.hw1.tictactoe.strategy;

import java.util.List;

import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;
import com.uofr.course.csc442.hw.hw1.tictactoe.logger.TicTacToeMetric;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Board;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Move;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.State;
/**
 * Strategy implementation for random moves.
 * It just finds the possibleMoves from the board
 * and picks one move out of them randomly
 * and requests the board to make that move.
 * @author tusharkumar
 *
 */
public class RandomChoiceStrategy implements Strategy {

	@Override
	public Move getMove(State state, Move lastMove, TicTacToeMetric ticTacToeMetric) throws IllegalGameMoveException {
		return getRandomMove(state, lastMove, ticTacToeMetric);
	}

	/**
	 * Method to choose random move from available moves 
	 * @param state
	 * @param lastMove
	 * @param ticTacToeMetric
	 * @return
	 */
	private Move getRandomMove(State state, Move lastMove, TicTacToeMetric ticTacToeMetric) {
		Long startTime = System.currentTimeMillis();
		Board board = state.getBoard();
		List<Move> moveList = board.getPossibleMoves(lastMove);		
		long endTime = System.currentTimeMillis();
		ticTacToeMetric.addTime(endTime-startTime);
		ticTacToeMetric.addStateCount(1);
		return moveList.get((int) (System.currentTimeMillis() % moveList.size()));
	}
}
