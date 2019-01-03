package com.uofr.course.csc442.hw.hw1.tictactoe.strategy;

import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameInputException;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;
import com.uofr.course.csc442.hw.hw1.tictactoe.logger.TicTacToeMetric;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Move;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.State;

/**
 * Interface to assign a contract for all kinds of strategies
 * for picking a move. 
 * Follows simple Strategy pattern.
 * @author tusharkumar
 *
 */
public interface Strategy {

	public Move getMove(State state, Move lastMove, TicTacToeMetric ticTacToeMetric) 
			throws IllegalGameMoveException, IllegalGameInputException;
}
