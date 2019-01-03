package com.uofr.course.csc442.hw.hw1.tictactoe.strategy;

import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameInputException;
import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;
import com.uofr.course.csc442.hw.hw1.tictactoe.logger.TicTacToeMetric;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Board;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Move;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.State;

/**
 * Strategy implementation for getting the moves 
 * as input from user
 * @author tusharkumar
 */
public class UserInputStrategy implements Strategy {

	@Override
	public Move getMove(State state, Move lastMove, TicTacToeMetric ticTacToeMetric) 
			throws IllegalGameMoveException, IllegalGameInputException {
		Board board = state.getBoard();
		Long startTime = System.currentTimeMillis();
		Move move = board.getMoveInputFromUser(lastMove);
		long endTime = System.currentTimeMillis();
		ticTacToeMetric.addTime(endTime-startTime);
		return move;
	}
}
