package com.uofr.course.csc442.hw.hw1.tictactoe.model;

/**
 * Enum created to represent the boardType
 * @author tusharkumar
 *
 */
public enum BoardType {

	ThreeBoard("3 Board TicTacToe"),
	NineBoard("9 Board TicTacToe"),
	Ultimate("Ultimate TicTacToe");
	
	private String boardType;
	
	private BoardType(String boardType) {
		this.boardType = boardType;
	}

	public String getBoardType() {
		return boardType;
	}
	
	public String toString() {
		return boardType;
	}	
}
