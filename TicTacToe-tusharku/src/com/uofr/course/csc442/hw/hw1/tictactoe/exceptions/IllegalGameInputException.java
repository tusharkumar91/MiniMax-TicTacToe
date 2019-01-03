package com.uofr.course.csc442.hw.hw1.tictactoe.exceptions;

import com.uofr.course.csc442.hw.hw1.tictactoe.constants.TicTacToeConstants;

/**
 * Exception class to handle the cases of bad input.
 * Scenarios include but not limited to 
 * a.) choice of X or O for starting player
 * b.) a move not between the required range of 1-10
 * c.) move not being a string.
 * 
 * These exceptions are not for handling 
 * game rules. 
 * Those are rather handled by {@link IllegalGameMoveException}
 * @author tusharkumar
 *
 */
public class IllegalGameInputException extends Exception {

	//Defaulting and not bothering about serialization for now
	private static final long serialVersionUID = 1L;
	private static final String INVALID_INPUT_PROMPT = "Invalid Input provided to the program";
	private static final String SUPPORTED_INPUT_TYPES = "Supported Input Types";
	private static final String ACTUAL_INPUT = "Actual Input";
	

	//Input types that the program actually expects
	private String supportedInputTypes;
	
	//Input types that the program actually got
	private String actualInput;
	
	@Override
	public String getMessage() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(INVALID_INPUT_PROMPT + TicTacToeConstants.NEW_LINE);
		strBuilder.append(SUPPORTED_INPUT_TYPES + " : " + supportedInputTypes + TicTacToeConstants.NEW_LINE);
		strBuilder.append(ACTUAL_INPUT + " : " + TicTacToeConstants.SPACE + actualInput + TicTacToeConstants.NEW_LINE);
		return strBuilder.toString();
	}
	
	public IllegalGameInputException(String supportedInputTypes, String actualInput) {
		super();
		this.supportedInputTypes = supportedInputTypes;
		this.actualInput = actualInput;
	}		
}
