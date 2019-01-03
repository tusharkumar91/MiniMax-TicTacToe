package com.uofr.course.csc442.hw.hw1.tictactoe.utils;

import com.uofr.course.csc442.hw.hw1.tictactoe.model.CellValue;

/**
 * Utility class built to help the board classes
 * with basic functionalities and take off load 
 * from them.
 * @author tusharkumar
 *
 */
public final class BoardUtils {
	private BoardUtils() {
		/**
		 * Private constructor so that
		 * this isn't initialized ever.
		 */
	}
	
	/**
	 * Helper method to get count of two continuous
	 * values of input cellvalue in a row of the given board
	 * @param cellValue
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static int getTwoInRowCount(CellValue cellValue, CellValue[][] board, int boardGridSize) {
		int twoCountRow = 0;
		for(int row = 0; row<boardGridSize; row++) {
			if((board[row][0] == board[row][1] && board[row][1] == cellValue && board[row][2] == CellValue.BLANK) || 
					((board[row][1] == board[row][2] && board[row][2] == cellValue && board[row][0] == CellValue.BLANK))){
				twoCountRow += 1;

			}
		}
		return twoCountRow;
	}
	
	/**
	 * Helper method to get count of two continuous
	 * values of input cellvalue in a column of the given board
	 * @param cellValue
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static int getTwoInColCount(CellValue cellValue, CellValue[][] board, int boardGridSize) {
		int twoCountCol = 0;
		for(int col = 0; col<boardGridSize; col++) {
			if((board[0][col] == board[1][col] && board[1][col] == cellValue && board[2][col] == CellValue.BLANK) || 
					((board[1][col] == board[2][col] && board[2][col] == cellValue && board[0][col] == CellValue.BLANK))){
				twoCountCol += 1;

			}
		}
		return twoCountCol;
	}
	
	/**
	 * Helper method to get count of two continuous
	 * values of input cellvalue in left diagonal of the given board
	 * @param cellValue
	 * @param board
	 * @return
	 */
	private static int getTwoCountLD(CellValue cellValue, CellValue[][] board) {
		int twoCountLD = 0;
		if((board[0][0] == board[1][1] && board[1][1] == cellValue&& board[2][2] == CellValue.BLANK) || 
				((board[1][1] == board[2][2] && board[2][2] == cellValue && board[0][0] == CellValue.BLANK))){
			twoCountLD += 1;
		}
		return twoCountLD;
	}
	
	/**
	 * Helper method to get count of two continuous
	 * values of input cellvalue in the right diagonal the given board
	 * @param cellValue
	 * @param board
	 * @return
	 */
	private static int getTwoCountRD(CellValue cellValue, CellValue[][] board) {
		int twoCountRD = 0;
		if((board[0][2] == board[1][1] && board[1][1] == cellValue && board[2][0] == CellValue.BLANK) || 
				((board[1][1] == board[2][0] && board[2][0] == cellValue && board[0][2] == CellValue.BLANK))){
			twoCountRD += 1;
		}
		return twoCountRD;
	}
	
	/**
	 * Utility method to evaluate the heuristic for Minmax given a board of 3*3
	 * Gets the count of 2 continuous values and finally returns the difference
	 * between (sums of the counts for rows/cols/diagonals of X) and 
	 * ((sums of the counts for rows/cols/diagonals of O) ) 
	 * @param board
	 * @param maxUtility
	 * @param minUtility
	 * @param boardGridSize
	 * @return
	 */
	public static double evaluateHeuristicForMinMax(CellValue[][] board, int boardGridSize) {	

		int twoCountRowForX = getTwoInRowCount(CellValue.X, board, boardGridSize);
		int twoCountRowForO = getTwoInRowCount(CellValue.O, board, boardGridSize);
		
		int twoCountColForX = getTwoInColCount(CellValue.X, board, boardGridSize);
		int twoCountColForO = getTwoInColCount(CellValue.O, board, boardGridSize);
	
		int twoCountLDForX = getTwoCountLD(CellValue.X, board);
		int twoCountLDForO = getTwoCountLD(CellValue.O, board);
		
		int twoCountRDForX = getTwoCountRD(CellValue.X, board);
		int twoCountRDForO = getTwoCountRD(CellValue.O, board);

		int countForX = twoCountRowForX + twoCountColForX + twoCountLDForX + twoCountRDForX;
		int countFor0 = twoCountRowForO + twoCountColForO + twoCountLDForO + twoCountRDForO;
		return countForX - countFor0;
			
	}	
	
	/**
	 * Utility method to calculate game winning positions 
	 * of input cellvalue in the given board
	 * @param board
	 * @param x
	 * @param boardGridSize
	 * @return
	 */
	public static int getGameWinPositions(CellValue[][] board, CellValue x, int boardGridSize) {
		int winningPositionsOfPlayer = 0;
		for(int i=0; i< boardGridSize; i++) {
			for(int j=0; j< boardGridSize; j++) {
				if(board[i][j] == CellValue.BLANK) {
					board[i][j] = x;
					if(isBoardComplete(board, boardGridSize) > 0) {
						winningPositionsOfPlayer++;
					}
					board[i][j] = CellValue.BLANK;
				}
			}
		}		
		return winningPositionsOfPlayer;
	}
	
	/**
	 * Helper method to print a 3*3 board
	 * @param board
	 * @param boardGridSize
	 */
	public static void printBoard(CellValue[][] board, int boardGridSize) {
		for(int boardGridIndexRow=0; boardGridIndexRow<boardGridSize; boardGridIndexRow++) {
			for(int boardGridIndexCol=0; boardGridIndexCol<boardGridSize; boardGridIndexCol++) {
				System.err.print(board[boardGridIndexRow][boardGridIndexCol] + " ");				
			}
			System.err.println();
		}
		System.err.println("===========================================");
		System.err.println();
	}

	public static int isBoardComplete(CellValue[][] board, int boardGridSize) {
		return Math.max(isRDWin(board, boardGridSize), Math.max(isLDWin(board, boardGridSize), Math.max(isHWin(board, boardGridSize), isVWin(board, boardGridSize))));
	}
	
	public static boolean isBoardComplete(int moveIndex, CellValue[][] board, int boardGridSize) {
		int rowIndex = (moveIndex-1) / boardGridSize;
		int colIndex = (moveIndex-1) - boardGridSize*rowIndex;
		CellValue cellValue = board[rowIndex][colIndex];
		return isHWin(rowIndex, cellValue, board, boardGridSize) 
				|| isVWin(colIndex, cellValue, board, boardGridSize) 
				|| isLDWin(rowIndex, colIndex, cellValue, board, boardGridSize)
				|| isRDWin(rowIndex, colIndex, cellValue, board, boardGridSize);
	}
	
	/**
	 * Utility method to check if there a horizontal
	 * row of all 3 similar cellvalues in board
	 * containing given indexes.
	 * @param rowIndex
	 * @param cellValue
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static boolean isHWin(int rowIndex, CellValue cellValue, CellValue[][] board, int boardGridSize) {
		for(int i=0; i<boardGridSize;i++) {
			if(board[rowIndex][i] != cellValue) {
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * Utility method to check if there a vertical
	 * column of all 3 similar cellvalues in board
	 * containing given indexes.
	 * @param colIndex
	 * @param cellValue
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static boolean isVWin(int colIndex,CellValue cellValue, CellValue[][] board, int boardGridSize) {
		for(int i=0; i<boardGridSize;i++) {
			if(board[i][colIndex] != cellValue) {
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * Utility method to check if row of all 
	 * 3 similar cellvalues in left diagonal of board
	 * containing given indexes.
	 * @param rowIndex
	 * @param colIndex
	 * @param cellValue
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static boolean isLDWin(int rowIndex, int colIndex,CellValue cellValue,CellValue[][] board, int boardGridSize) {
		for(int i=0; i<boardGridSize;i++) {
			if(board[i][i] != cellValue) {
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * Utility method to check if row of all 
	 * 3 similar cellvalues in right diagonal of board
	 * containing given indexes.
	 * @param rowIndex
	 * @param colIndex
	 * @param cellValue
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static boolean isRDWin(int rowIndex, int colIndex,CellValue cellValue, CellValue[][] board, int boardGridSize) {
		for(int i=boardGridSize-1; i>=0;i--) {
			if(board[boardGridSize-1-i][i] != cellValue) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Is there a vertical Win in the board
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static int isVWin(CellValue[][] board, int boardGridSize) {
		if(board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[2][0]!= CellValue.BLANK) {
			return 1;
		}		
		if(board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[2][1]!= CellValue.BLANK) {
			return 2;
		}		
		if(board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[2][2]!= CellValue.BLANK) {
			return 3;
		}
		return -1;

	}
	
	/**
	 * Is there a horizontal win in the board
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static int isHWin(CellValue[][] board, int boardGridSize) {
		if(board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][2]!= CellValue.BLANK) {
			return 1;
		}		
		if(board[1][0] == board[1][1] && board[1][1] == board[1][2] && board[1][2]!= CellValue.BLANK) {
			return 4;
		}		
		if(board[2][0] == board[2][1] && board[2][1] == board[2][2] && board[2][2]!= CellValue.BLANK) {
			return 7;
		}
		return -1;

	}
	
	/**
	 * Is left diagonal complete on the board
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static int isLDWin(CellValue[][] board, int boardGridSize) {
		if(board[0][0] == board[1][1] && board[1][1] == board[boardGridSize-1][boardGridSize-1] && board[boardGridSize-1][boardGridSize-1]!= CellValue.BLANK) {
			return 1;
		}
		else{
			return -1;
		}
	}
	
	/**
	 * Is right diagonal complete on the board
	 * @param board
	 * @param boardGridSize
	 * @return
	 */
	private static int isRDWin(CellValue[][] board, int boardGridSize) {
		if(board[boardGridSize-1][0] == board[1][1] && board[1][1] == board[0][boardGridSize-1] && board[0][boardGridSize-1]!= CellValue.BLANK) {
			return 3;
		}
		else{
			return -1;
		}
	}	
}
