package com.uofr.course.csc442.hw.hw1.tictactoe.constants;

import com.uofr.course.csc442.hw.hw1.tictactoe.model.Move;

/**
 * Class to hold all the string constants used by TicTacToe 
 * game and also act as a common mechanism
 * for fetching all game related messages or prompts
 * @author tusharkumar
 *
 */
public class TicTacToeConstants {
	public static final String BOARD_POSITION_OCCUPIED = "Position is already occupied";
	public static final String INTEGER_RANGE_BOARD = "Integer between 1-9";
	public static final String USER_MOVE_PROMPT = "Enter move of User";
	public static final String USER_WON_PROMPT = "User Won";
	public static final String AGENT_WON_PROMPT = "Agent Won";
	public static final String DRAW_PROMPT = "Draw Happened";
	public static final String PIECE_CHOICE_PROMPT = "You want X or O";
	public static final String VALID_PIECE_CHOICE = "X, O, x, o";
	public static final String AGENT = "Agent";
	public static final String USER = "User";
	public static final String SPACE = " ";
	public static final String NEW_LINE = "\n";
	public static final String FIRST_PLAYER_PROMPT = "First Player is";
	public static final String INPUT_RANGE_PROMPT = "is not a number within the range";
	public static final String BOARD_POS_AND_MOVE_POS = "Board Position and Move Position";
	public static final String MOVE_LASTMOVE_MISMATCH_PROMPT = "Current move not in the board index according to last move";
	public static final String ONE = "1";
	public static final String LEFT_BRACES = "(";
	public static final String RIGHT_BRACES = ")";		
	
	/**
	 * Method to get the message explaining the move taken
	 * by a player.
	 * @param move
	 * @param isAgent
	 * @return
	 */
	public static final String getMoveMessage(Move move, boolean isAgent) {
		String player = isAgent ? AGENT : USER;
		String boardIndex = move.getBoardIndex() != 0 ? "board " + move.getBoardIndex() + " and" : "";
		String moveIndex = "position " + move.getMoveIndex();
		
		String message = player + SPACE + boardIndex + SPACE + moveIndex; 
		return message;
	}
	
	/**
	 * Method to get the prompt message for the first 
	 * player which is required only at the start of 
	 * a new manual game
	 * @param isAgent
	 * @return
	 */
	public static final String getFirstPlayerPrompt(boolean isAgent) {
		String player = isAgent ? AGENT : USER;
		return FIRST_PLAYER_PROMPT + SPACE + player;
	}
	
}
