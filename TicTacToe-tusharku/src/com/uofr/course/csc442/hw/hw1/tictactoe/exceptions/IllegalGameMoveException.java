package com.uofr.course.csc442.hw.hw1.tictactoe.exceptions;

import com.uofr.course.csc442.hw.hw1.tictactoe.constants.TicTacToeConstants;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.NineBoard;

/**
 * Exception class to handle the cases of bad move during game.
 * Scenarios include but not limited to 
 * a.) choice of move not according 
 * to the previous players move(Eg: {@link NineBoard })
 * b.) a move for a position that is already occupied
 * 
 * These exceptions are for handling 
 * game rules.
 * For exceptions handling of game inputs from user
 * please refer to  {@link IllegalGameInputException}
 * @author tusharkumar
 *
 */
public class IllegalGameMoveException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String INVALID_MOVE_PROMPT = "Invalid move given as input to the program";
	private static final String REASON = "Reason :";
	
	//Reason for move not being accepted by program
	private String reason;
		
	@Override
	public String getMessage() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(INVALID_MOVE_PROMPT + TicTacToeConstants.NEW_LINE);
		strBuilder.append(REASON + TicTacToeConstants.SPACE + reason + TicTacToeConstants.NEW_LINE);
		return strBuilder.toString();
	}

	public IllegalGameMoveException(String reason) {
		super();
		this.reason = reason;
	}	
}
