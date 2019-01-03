package com.uofr.course.csc442.hw.hw1.tictactoe.model;

import com.uofr.course.csc442.hw.hw1.tictactoe.utils.BoardUtils;

/**
 * Class to contain the implementation of rules 
 * and game play of Ultimate Tic Tac Toe.
 * It uses the methods from its base class
 * {@link NineBoard} .
 * @author tusharkumar
 *
 */
public class UltimateBoard extends NineBoard {	
	/**
	 * Heuristic method to evaluate the board by using the 
	 * heuristic of counting continuous X's and continuous O's.
	 * This is done for each of the rows and columns and diagonals
	 * with all three values being added separately for X and O.
	 * The sum outputs for X and O are then differentiated 
	 */
	@Override
	public double evaluateHeuristicForMinMax(Move lastMove, double maxUtility, double minUtility) {
		Player winner = this.getWinner();
		if(winner != null) {
			return winner.isMax() ? maxUtility : minUtility;
		}
		else {			
			double utility =  (getUtilityOfRows() + getUtilityOfCols() + getUtilityOfLD() + getUtilityOfRD());
			return utility;	
		}
	}
	
	/**
	 * Method to get utility of all the boards
	 * on right diagonal.
	 * @return
	 */
	private double getUtilityOfRD() {
		double diagUtility = 0;
		int boardIndex = 7;
		int j = 0;
		for(int i=BOARD_GRID_SIZE-BOARD_SUB_GRID_SIZE; i>=0; i=i-BOARD_SUB_GRID_SIZE) {
			CellValue[][] tempBoard = new CellValue[BOARD_SUB_GRID_SIZE][BOARD_SUB_GRID_SIZE];
			for(int row=i; row<=i+2;row++) {
				for(int col=j; col<= j+2; col++) {
					tempBoard[row-i][col-j] = this.getGameBoard()[row][col];
				}
			}
			CellValue winner = winningBoardToPlayerMapping.get(boardIndex);	
			if(winner != null) {
				if(winner == CellValue.X) {
					diagUtility = diagUtility + 100;
				}
				else {
					diagUtility = diagUtility - 100;
				}
			}
			else{
				int gameWinPositionsForX = BoardUtils.getGameWinPositions(tempBoard, CellValue.X, BOARD_SUB_GRID_SIZE);
				int gameWinPositionsForO = BoardUtils.getGameWinPositions(tempBoard, CellValue.O, BOARD_SUB_GRID_SIZE);
				diagUtility = diagUtility + 2*(gameWinPositionsForX - gameWinPositionsForO) +
						BoardUtils.evaluateHeuristicForMinMax(tempBoard, BOARD_SUB_GRID_SIZE);				
			}
			j = j+3;
			boardIndex -= 2;
		}

		return diagUtility;
	}
	
	/**
	 * Method to get utility of all the boards
	 * on left diagonal.
	 * @return
	 */
	private double getUtilityOfLD() {
		double diagUtility = 0;
		int boardIndex = 1;
		for(int i=0; i<BOARD_GRID_SIZE; i=i+BOARD_SUB_GRID_SIZE) {
			CellValue[][] tempBoard = new CellValue[BOARD_SUB_GRID_SIZE][BOARD_SUB_GRID_SIZE];
			for(int row=i; row<=i+2;row++) {
				for(int col=i; col<= i+2; col++) {
					tempBoard[row-i][col-i] = this.getGameBoard()[row][col];
				}
			}
			CellValue winner = winningBoardToPlayerMapping.get(boardIndex);	
			if(winner != null) {
				if(winner == CellValue.X) {
					diagUtility = diagUtility + 100;
				}
				else {
					diagUtility = diagUtility - 100;
				}
			}
			else{
				int gameWinPositionsForX = BoardUtils.getGameWinPositions(tempBoard, CellValue.X, BOARD_SUB_GRID_SIZE);
				int gameWinPositionsForO = BoardUtils.getGameWinPositions(tempBoard, CellValue.O, BOARD_SUB_GRID_SIZE);
				diagUtility = diagUtility + 2*(gameWinPositionsForX - gameWinPositionsForO) +
						BoardUtils.evaluateHeuristicForMinMax(tempBoard, BOARD_SUB_GRID_SIZE);				
			}
			boardIndex += 4;
		}

		return diagUtility;
	}
	
	/**
	 * Method to get utility of board in 
	 * horizontal manner. 
	 * This is where we will the benefit of 
	 * having 3 boards in a row with good utility.
	 * board.
	 * @return
	 */
	private double getUtilityOfRows() {
		double maxUtility = 0;	
		double minUtility = 0;	
		int boardIndex = 1;
		for(int i=0; i<BOARD_GRID_SIZE; i=i+BOARD_SUB_GRID_SIZE) {
			double rowUtility = 0;
			for(int j=0; j<BOARD_GRID_SIZE; j=j+BOARD_SUB_GRID_SIZE) {
				CellValue[][] tempBoard = new CellValue[BOARD_SUB_GRID_SIZE][BOARD_SUB_GRID_SIZE];
				for(int row=i; row<=i+2;row++) {
					for(int col=j; col<= j+2; col++) {
						tempBoard[row-i][col-j] = this.getGameBoard()[row][col];
					}
				}
				CellValue winner = winningBoardToPlayerMapping.get(boardIndex);				
				if(winner != null) {
					if(winner == CellValue.X) {
						rowUtility = rowUtility + 100;
					}
					else {
						rowUtility = rowUtility - 100;
					}
				}
				else{
					int gameWinPositionsForX = BoardUtils.getGameWinPositions(tempBoard, CellValue.X, BOARD_SUB_GRID_SIZE);
					int gameWinPositionsForO = BoardUtils.getGameWinPositions(tempBoard, CellValue.O, BOARD_SUB_GRID_SIZE);
					rowUtility = rowUtility + 2*(gameWinPositionsForX - gameWinPositionsForO) +
							BoardUtils.evaluateHeuristicForMinMax(tempBoard, BOARD_SUB_GRID_SIZE);				
				}
				boardIndex++;
			}
			maxUtility = Math.max(maxUtility, rowUtility);
			minUtility = Math.min(minUtility, rowUtility);
		}
		return Math.abs(maxUtility) - Math.abs(minUtility);
	}
	
	/**
	 * Method to get utility of boards
	 * in vertical manner.
	 * This is where we will the benefit of 
	 * having 3 boards in a col with good utility.
	 * @return
	 */
	private double getUtilityOfCols() {
		double maxUtility = 0;	
		double minUtility = 0;
		int oldBoardIndex = 1;
		for(int j=0; j<BOARD_GRID_SIZE; j=j+BOARD_SUB_GRID_SIZE) {
			double colUtility = 0;
			int newBoardIndex = oldBoardIndex;
			for(int i=0; i<BOARD_GRID_SIZE; i=i+BOARD_SUB_GRID_SIZE) {
				CellValue[][] tempBoard = new CellValue[BOARD_SUB_GRID_SIZE][BOARD_SUB_GRID_SIZE];
				for(int row=i; row<=i+2;row++) {
					for(int col=j; col<= j+2; col++) {
						tempBoard[row-i][col-j] = this.getGameBoard()[row][col];
					}
				}
				CellValue winner = winningBoardToPlayerMapping.get(newBoardIndex);	
				if(winner != null) {
					if(winner == CellValue.X) {
						colUtility = colUtility + 100;
					}
					else {
						colUtility = colUtility - 100;
					}
				}
				else{
					int gameWinPositionsForX = BoardUtils.getGameWinPositions(tempBoard, CellValue.X, BOARD_SUB_GRID_SIZE);
					int gameWinPositionsForO = BoardUtils.getGameWinPositions(tempBoard, CellValue.O, BOARD_SUB_GRID_SIZE);
					colUtility = colUtility + 2*(gameWinPositionsForX - gameWinPositionsForO) +
							BoardUtils.evaluateHeuristicForMinMax(tempBoard, BOARD_SUB_GRID_SIZE);				
				}
				newBoardIndex += 3;
			}
			oldBoardIndex = oldBoardIndex + 1;
			maxUtility = Math.max(maxUtility, colUtility);
			minUtility = Math.min(minUtility, colUtility);
		}
		
		return Math.abs(maxUtility) - Math.abs(minUtility);
	}
	
	
	/**
	 * Overridden method to find out if the board is complete
	 */
	@Override
	public boolean isBoardComplete(Move move) {
		if(getWinner() != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Overridden method to find the winner of the board
	 */
	@Override
	public Player getWinner() {
		
		CellValue cellValue = getWinnerOfRow();
		if(cellValue != null) {
			return Player.playerFromChoice(cellValue.name());
		}
		
		cellValue = getWinnerOfCol();
		if(cellValue != null) {
			return Player.playerFromChoice(cellValue.name());
		}
		
		cellValue = getWinnerOfLD();
		if(cellValue != null) {
			return Player.playerFromChoice(cellValue.name());
		}
		
		cellValue = getWinnerOfRD();
		if(cellValue != null) {
			return Player.playerFromChoice(cellValue.name());
		}
		
		return null;		
	}
	
	/**
	 * Method to get the winner of 3 boards
	 * continuously in a row.
	 * @return
	 */
	private CellValue getWinnerOfRow() {
		CellValue winner = null;	
		int boardIndex = 0;
		for(int i=0; i<BOARD_GRID_SIZE; i=i+BOARD_SUB_GRID_SIZE) {
			int gridsComplete = 0;
			CellValue winnerOfGrid = null;
			for(int j=0; j<BOARD_GRID_SIZE; j=j+BOARD_SUB_GRID_SIZE) {	
				CellValue winnerOfBoard = winningBoardToPlayerMapping.get(boardIndex);
				if(winnerOfBoard != null) {
					if(winnerOfGrid != null) {
						if(winnerOfBoard == winnerOfGrid) {
							gridsComplete++;
						}
						else {
							break;
						}
					}
					else {
						winnerOfGrid = winnerOfBoard;
						gridsComplete++;
					}
				}
				else {
					break;
				}
				boardIndex++;
			}			
			if(gridsComplete == BOARD_SUB_GRID_SIZE) {
				winner = winnerOfGrid;
				break;
				
			}					
		}
		return winner;
	}
	
	/**
	 * Method to get the winner of 3 boards
	 * continuously in a column.
	 * @return
	 */
	private CellValue getWinnerOfCol() {
		CellValue winner = null;
		int oldBoardIndex = 1;
		for(int i=0; i<BOARD_GRID_SIZE;i=i+BOARD_SUB_GRID_SIZE) {
			int gridsComplete = 0;
			int newBoardIndex = oldBoardIndex;
			CellValue winnerOfGrid = null;
			for(int j=0; j<BOARD_GRID_SIZE; j=j+BOARD_SUB_GRID_SIZE) {
				CellValue winnerOfBoard = winningBoardToPlayerMapping.get(newBoardIndex);
				if(winnerOfBoard != null) {
					if(winnerOfGrid != null) {
						if(winnerOfBoard == winnerOfGrid) {
							gridsComplete++;
						}
						else {
							break;
						}
					}
					else {
						winnerOfGrid = winnerOfBoard;
						gridsComplete++;
					}
				}
				else {
					break;
				}
				newBoardIndex += 3;
			}
			if(gridsComplete == BOARD_SUB_GRID_SIZE) {
				winner = winnerOfGrid;
				break;				
			}	
			oldBoardIndex += 1;
		}
		return winner;
	}
	
	/**
	 * Method to get the winner of 3 boards
	 * continuously in left diagonal.
	 * @return
	 */
	private CellValue getWinnerOfLD() {
		CellValue winner = null;	
		CellValue winnerOfGrid = null;
		int boardIndex = 1;
		int gridsComplete = 0;
		for(int i=0; i<BOARD_GRID_SIZE; i=i+BOARD_SUB_GRID_SIZE) {			
			CellValue winnerOfBoard = winningBoardToPlayerMapping.get(boardIndex);
			if(winnerOfBoard != null) {
				if(winnerOfGrid != null) {
					if(winnerOfBoard == winnerOfGrid) {
						gridsComplete++;
					}
					else {
						break;
					}
				}
				else {
					winnerOfGrid = winnerOfBoard;
					gridsComplete++;
				}
			}
			else {
				break;
			}
			if(gridsComplete == BOARD_SUB_GRID_SIZE) {
				winner = winnerOfGrid;
				break;				
			}
			boardIndex += 4;
		}
		return winner;
	}
	
	
	/**
	 * Method to get the winner of 3 boards
	 * continuously in right doagonal.
	 * @return
	 */
	private CellValue getWinnerOfRD() {
		CellValue winner = null;	
		CellValue winnerOfGrid = null;
		int boardIndex = 7;
		int gridsComplete = 0;
		for(int i=BOARD_GRID_SIZE-BOARD_SUB_GRID_SIZE-1; i>=BOARD_GRID_SIZE; i=i-BOARD_SUB_GRID_SIZE) {
			CellValue winnerOfBoard = winningBoardToPlayerMapping.get(boardIndex);
			if(winnerOfBoard != null) {
				if(winnerOfGrid != null) {
					if(winnerOfBoard == winnerOfGrid) {
						gridsComplete++;
					}
					else {
						break;
					}
				}
				else {
					winnerOfGrid = winnerOfBoard;
					gridsComplete++;
				}
			}
			else {
				break;
			}
			if(gridsComplete == BOARD_SUB_GRID_SIZE) {
				winner = winnerOfGrid;
				break;				
			}	
			boardIndex -= 2;
		}
		return winner;
	}
}
