package com.uofr.course.csc442.hw.hw1.tictactoe.model;

/**
 * Enum created to represent the cell member of the board.
 * Can take values of 'X','O' and '-'
 * @author tusharkumar
 */
public enum CellValue {

	X("X"),
	O("O"),
	BLANK("-");
	
	private String cellValue;
	
	private CellValue(String cellValue) {
		this.cellValue = cellValue;
	}

	public String getCellValue() {
		return cellValue;
	}
	
	public String toString() {
		return cellValue;
	}
	
	/**
	 * Method to get the cellValue and the player who must have
	 * made the move to put this value. 
	 * Its useful in finding a winner from lastMove itself and 
	 * gets rid of unnecessary complex logic. 
	 * @param playerName
	 * @return
	 */
	public static CellValue valFromPlayer(String playerName) {
		CellValue cellValue = null;
		for(CellValue val : CellValue.values()) {
			if(val.cellValue.equalsIgnoreCase(playerName)) {
				cellValue = val;
				break;
			}
		}
		return cellValue;
	}	
}
