package com.uofr.course.csc442.hw.hw1.tictactoe.model;

import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;

/**
 * Method to maintain the state of the game specifically the below:
 * a.) Board status 
 * b.) Current player
 * @author tusharkumar
 *
 */
public class State {
	
	private Board board;
	private Player playerOfNextMove;
	
	public Player getCurrentPlayer() {
		return playerOfNextMove;
	}
	
	public Board getBoard() {
		return board;
	}	

	public boolean isGoalState(Move move) {
		return board.isBoardComplete(move);
	}
	
	public boolean isMovePossible(Move lastMove) {
		return board.emptyPositionsPresent(lastMove);
	}
	
	public void move(Move move, Move lastMove) throws IllegalGameMoveException {
		board.makeMove(move, lastMove);
		playerOfNextMove = playerOfNextMove.getOtherPlayer();
	}
	
	public State(Player player, Board board) {
		this.board = board;
		this.playerOfNextMove = player;
	}
}
