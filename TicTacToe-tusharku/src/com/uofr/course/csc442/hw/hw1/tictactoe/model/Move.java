package com.uofr.course.csc442.hw.hw1.tictactoe.model;

/**
 * This class encapsulates the move of a game.
 * It contains the lastMove and boardIndex as that would be 
 * essential for some of the games/boards.
 * @author tusharkumar
 *
 */
public class Move {
	
	//Move index between 1-9
	private  final int moveIndex;
	
	//Board index between 1-9
	private  final int boardIndex;
	
	//Move made by last player
	private  final Move lastMove;
	
	//Current player who is making this move
	private  Player playerOfMove;
	
	public int getMoveIndex() {
		return moveIndex;
	}
	
	public int getBoardIndex() {
		return boardIndex;
	}
	
	public Move getLastMove() {
		return lastMove;
	}
	
	public Player getPlayerOfMove() {
		return playerOfMove;
	}
	
	public void setPlayerOfMove(Player playerOfMove) {
		this.playerOfMove = playerOfMove;
	}
	
	public Move(int moveIndex, int boardIndex, Move lastMove, Player playerOfMove) {
		this.moveIndex = moveIndex;
		this.boardIndex = boardIndex;
		this.lastMove = lastMove;
		this.playerOfMove = playerOfMove;
	}
	
	public MoveIndexData getMoveIndexData(int boardGridSize) {
		MoveIndexData moveIndexData = new MoveIndexData();
		int moveIndex = getMoveIndex();
		int rowIndex = (moveIndex-1) / boardGridSize;
		int colIndex = (moveIndex-1) - boardGridSize*rowIndex;
		moveIndexData.setColIndex(colIndex);
		moveIndexData.setRowIndex(rowIndex);
		return moveIndexData;
	}
	
	public class MoveIndexData {
		int rowIndex;
		int colIndex;
		
		public int getColIndex() {
			return colIndex;
		}
		public void setColIndex(int colIndex) {
			this.colIndex = colIndex;
		}
		
		public int getRowIndex() {
			return rowIndex;
		}
		public void setRowIndex(int rowIndex) {
			this.rowIndex = rowIndex;
		}
	}
}
