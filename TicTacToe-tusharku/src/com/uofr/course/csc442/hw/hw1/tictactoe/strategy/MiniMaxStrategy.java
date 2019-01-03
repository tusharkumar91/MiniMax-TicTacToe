package com.uofr.course.csc442.hw.hw1.tictactoe.strategy;

import java.util.List;

import com.uofr.course.csc442.hw.hw1.tictactoe.exceptions.IllegalGameMoveException;
import com.uofr.course.csc442.hw.hw1.tictactoe.logger.TicTacToeMetric;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Board;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Move;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.Player;
import com.uofr.course.csc442.hw.hw1.tictactoe.model.State;

/**
 * Class encapsulating the implementation of MiniMax Strategy
 * @author tusharkumar
 *
 */
public class MiniMaxStrategy implements Strategy {

	private double MAX_UTILITY = 1000;
	private double MIN_UTILITY = -1000;
	private int MAX_DEPTH = 6;
	
	private boolean isAlphaBetaPruningEnabled;
	
	public MiniMaxStrategy(boolean isAlphaBetaPruningEnabled) {
		super();
		this.isAlphaBetaPruningEnabled = isAlphaBetaPruningEnabled;	
	}
	
	public MiniMaxStrategy(boolean isAlphaBetaPruningEnabled, int depth) {
		super();
		this.isAlphaBetaPruningEnabled = isAlphaBetaPruningEnabled;	
		this.MAX_DEPTH = depth;
	}
	
	public Move getMove(State state, Move lastMove, TicTacToeMetric ticTacToeMetric) throws IllegalGameMoveException {		
		return(getMoveUsingMiniMax(state.getBoard(), lastMove, ticTacToeMetric));
	}

	/**
	 * Method to find the value of the board.
	 * This is only used when depth is less than MaxDepth
	 * @param board
	 * @return
	 */
	private double getUtilityValueFromBoard(Board board) {
		Player player = board.getWinner();
		
		if(player != null) {
			if(player.isMax()) {
				return MAX_UTILITY;
			}
			else {
				return MIN_UTILITY;
			}
		}
		return 0;
	}
	
	/**
	 * Method to find the value of the board.
	 * This is only used when depth is greater than maxDepth
	 * @param board
	 * @return
	 */
	private double evaluateHeuristicForMinMax(Board board, Move lastMove) {
		return board.evaluateHeuristicForMinMax(lastMove, MAX_UTILITY, MIN_UTILITY);
	}
	
	/**
	 * Main method for minimax which finds the possible moves
	 * and calls the recursive method
	 * @param board
	 * @param player
	 * @param boardGridSize
	 * @param ticTacToeMetric
	 * @return
	 * @throws IllegalGameMoveException 
	 */
	private Move getMoveUsingMiniMax(Board board, Move lastMove, TicTacToeMetric ticTacToeMetric) throws IllegalGameMoveException {
		Move bestMove = null;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int depth = 0;
		long startTime = System.currentTimeMillis();
		int stateCount = 0;
		if(lastMove.getPlayerOfMove().getOtherPlayer().isMax()) {			
			double maxUtility = Integer.MIN_VALUE;			
			List<Move> moveList = board.getPossibleMoves(lastMove);	
			for(Move possibleMove : moveList) {
				stateCount++;
				board.makeMove(possibleMove, lastMove);				
				double utility = getUtilityUsingMiniMaxRecurive(board, possibleMove, alpha, beta, ticTacToeMetric, depth+1);
				//System.err.println("utility of move " + possibleMove.getMoveIndex() + " is " + utility);
				ticTacToeMetric.setMaxUtility((int)utility);
				if(utility > maxUtility) {
					bestMove = possibleMove;
					maxUtility = utility;
				}
				board.clearMove(possibleMove);
			}						
		}
		else {
			double minUtility = Integer.MAX_VALUE;			
			List<Move> moveList = board.getPossibleMoves(lastMove);	
			for(Move possibleMove : moveList) {
				stateCount++;
				board.makeMove(possibleMove, lastMove);
				double utility = getUtilityUsingMiniMaxRecurive(board, possibleMove, alpha, beta, ticTacToeMetric, depth+1);
				//System.err.println("utility of move " + possibleMove.getMoveIndex() + " is " + utility);
				ticTacToeMetric.setMinUtility((int)utility);
				if(utility < minUtility) {
					bestMove = possibleMove;
					minUtility = utility;
				}
				board.clearMove(possibleMove);				
			}			
		}		
		long endTime = System.currentTimeMillis();
		ticTacToeMetric.addTime(endTime-startTime);	
		ticTacToeMetric.addStateCount(stateCount);
		return bestMove;
	}
	
	/**
	 * Helper recursive method for Minimax which finds the possible 
	 * moves and recursively calls itself.
	 * @param board
	 * @param lastMove
	 * @param alpha
	 * @param beta
	 * @param ticTacToeMetric
	 * @param depth
	 * @return
	 * @throws IllegalGameMoveException
	 */
	private double getUtilityUsingMiniMaxRecurive(Board board, Move lastMove, double alpha, double beta, TicTacToeMetric ticTacToeMetric, int depth) throws IllegalGameMoveException {
		ticTacToeMetric.setMaxDepth(depth);
		double utility = 0;
		
		utility = getUtilityValueFromBoard(board);
		if(utility != 0 || !board.emptyPositionsPresent(lastMove)) {
			return utility;
		}
		if(depth >= MAX_DEPTH) {
			utility = evaluateHeuristicForMinMax(board, lastMove);
			return utility;
		}
		if(lastMove.getPlayerOfMove().getOtherPlayer().isMax()) {			
			double maxUtility = Integer.MIN_VALUE;		
			List<Move> moveList = board.getPossibleMoves(lastMove);
			for(Move possibleMove : moveList) {
				board.makeMove(possibleMove, lastMove);
				maxUtility = Math.max(maxUtility, getUtilityUsingMiniMaxRecurive(board, possibleMove, alpha, beta, ticTacToeMetric, depth+1));				
				ticTacToeMetric.setMaxUtility((int) maxUtility);
				if(isAlphaBetaPruningEnabled) {
					if(maxUtility >= beta) {
						board.clearMove(possibleMove);
						return maxUtility;
					}
					alpha = Math.max(alpha, maxUtility);
				}
				board.clearMove(possibleMove);
			}
			utility = maxUtility;
		}
		else {
			double minUtility = Integer.MAX_VALUE;	
			List<Move> moveList = board.getPossibleMoves(lastMove);		
			for(Move possibleMove : moveList) {				
				board.makeMove(possibleMove, lastMove);
				minUtility = Math.min(minUtility, getUtilityUsingMiniMaxRecurive(board, possibleMove, alpha, beta, ticTacToeMetric, depth+1));
				ticTacToeMetric.setMinUtility((int) minUtility);
				if(isAlphaBetaPruningEnabled) {

					if(minUtility <= alpha) {
						board.clearMove(possibleMove);
						return minUtility;
					}
					beta = Math.min(beta, minUtility);
				}
				board.clearMove(possibleMove);
			}
			utility = minUtility;
		}
		ticTacToeMetric.addStateCount(1);
		return utility;
	}
}
