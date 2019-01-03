package com.uofr.course.csc442.hw.hw1.tictactoe.model;

import com.uofr.course.csc442.hw.hw1.tictactoe.constants.TicTacToeConstants;

/**
 * Enum created to represent the options
 * for running the program game
 * 
 * @author tusharkumar
 *
 */
public enum OptionType {

	BoardType("board"),
	AUTO("auto"),
	ALPHA_BETA("alphabeta"),
	FIRST_X("firstx"),
	ITER("iter"),
	DEPTH("depth");
	
	private String optionName;
	
	private OptionType(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionName() {
		return optionName;
	}
	
	public String toString() {
		return optionName;
	}	
	
	public static OptionType getOptionTypeFromName(String optionName) {
		for(OptionType optionType: OptionType.values()) {
			if(optionType.optionName.equalsIgnoreCase(optionName)) {
				return optionType;
			}
		}
		return null;
	}
	
	public static String getValidOptionNames() {
		StringBuilder str = new StringBuilder();
		for(OptionType optionType: OptionType.values()) {
			str.append(optionType.optionName);
			str.append(",");
			str.append(TicTacToeConstants.SPACE);
		}
		return str.toString();
	}
}
