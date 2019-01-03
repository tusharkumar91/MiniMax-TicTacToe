package com.uofr.course.csc442.hw.hw1.tictactoe.logger;

/**
 * Metric class to help log all the metric regarding
 * runs of board games of all types.
 * These are not crucla to running of game, and meant only
 * for understanding the progress, execution and performance
 * of the program running the game.
 * @author tusharkumar
 *
 */
public class TicTacToeMetric {
	
	//Count of different states being visited 
	private int stateCount = 0;
	
	//Time taken to look ahead and 'think' for a move
	private Long timeInMilliSeconds = 0L;
	
	//Max depth visited by program
	private int maxTreeDepth = 0;
	
	/**
	 * Border values of utility seen during MinMax
	 * (helps to get accurate heuristics)
	 */
	private int minUtility;
	private int maxUtility;
	
	/**
	 * Method to reset all metrics and 
	 * start things afresh
	 */
	public void resetMetrics() {
		stateCount = 0;
		timeInMilliSeconds = 0L;
		minUtility = 0;
		maxUtility = 0;
		maxTreeDepth = 0;
	}
	
	public int getMaxTreeDepth() {
		return maxTreeDepth;
	}

	public int getMinUtility() {
		return minUtility;
	}

	public int getMaxUtility() {
		return maxUtility;
	}
	
	public int getStateCount() {
		return stateCount;
	}

	public Long getTimeInMilliSeconds() {
		return timeInMilliSeconds;
	}

	
	public void addStateCount(int stepCount) {
		this.stateCount = this.stateCount + stepCount;
	}
	
	public void addTime(long stepTime) {
		timeInMilliSeconds = timeInMilliSeconds + stepTime;
	}
	
	public void setMaxDepth(int depth) {
		if(depth > maxTreeDepth) {
			maxTreeDepth = depth;
		}
	}
	
	public void setMaxUtility(int utility) {
		if(utility > maxUtility) {
			maxUtility = utility;
		}
	}
	
	public void setMinUtility(int utility) {
		if(utility < minUtility) {
			minUtility = utility;
		}
	}
	
}
