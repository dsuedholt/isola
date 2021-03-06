package com.example.isola.ai;

import java.util.ArrayList;

import android.graphics.Point;

import com.example.isola.game.Board;

/**
 * This class represents a computer player that implements the Alpha-Beta-Pruning variant of the 
 * Minimax-Algorithm to find the best move.
 */
public class MinimaxStrategy extends Strategy {
	
	private int depth;
	
	private class TreeNode {
		private Board board;
		private boolean hasMoved, player1;
		private int x, y;
		
		private TreeNode(Board board, boolean hasMoved, boolean player1, int x, int y) {
			this.board = board;
			
			// was the turn leading to this node a player move or destruction of a field?
			this.hasMoved = hasMoved;
			
			// the player that has made the move leading to this node
			this.player1 = player1;
			
			this.x = x;
			this.y = y;
		}
	}
	
	/**
	 * Construct the MinimaxStrategy.
	 * @param board   The game board.
	 * @param player1 True if the computer player is player1, false otherwise.
	 * @param depth   Integer representing how far down the game tree the computer should look
	 * for the best move. Higher depth means better moves but significantly longer calculation periods.
	 */
	public MinimaxStrategy(Board board, boolean player1, int depth) {
		super(board, player1);
		this.depth = depth;
		
		WAITINGTIME = 800 - depth * 100;
		WAITINGTIME = (WAITINGTIME > 0)?WAITINGTIME:0;
	}

	private ArrayList<TreeNode> generateChildMoves(TreeNode node) {
		ArrayList<TreeNode> result = new ArrayList<TreeNode>();	
		
		if (node.hasMoved) {
			Point pos = node.board.getPlayerPosition(!node.player1);
			// look for fields to destroy
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if (node.board.canDestroy(pos.x + i, pos.y + j)) {
						Board board = new Board(node.board);
						board.destroy(pos.x + i, pos.y + j);
						TreeNode tmp = new TreeNode(board, false, node.player1, pos.x + i, pos.y + j);
						result.add(tmp);
					}
				}
			}

		} else {
			Point pos = node.board.getPlayerPosition(!node.player1);
			// look for fields to move to
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (node.board.canMove(!node.player1, pos.x + i, pos.y + j)) {
						Board board = new Board(node.board);
						board.move(!node.player1, pos.x + i, pos.y + j);
						TreeNode tmp = new TreeNode(board, true, !node.player1, pos.x + i, pos.y + j);
						result.add(tmp);
					}
				}
			}
		}
		return result;
	}

	@Override
	protected void doMove() {
		TreeNode root = new TreeNode(board, false, !player1, 0, 0);
		TreeNode best = bestMove(root);
		board.move(player1, best.x, best.y);
	}

	@Override
	protected void doDestroy() {
		TreeNode root = new TreeNode(board, true, player1, 0, 0);
		TreeNode best = bestMove(root);
		board.destroy(best.x, best.y);
	}
	
	private TreeNode bestMove(TreeNode root) {
		TreeNode best = null;
		double bestVal = Double.NEGATIVE_INFINITY;
		for (TreeNode node : generateChildMoves(root)) {
			double val = minimax(node, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (val > bestVal) {
				bestVal = val;
				best = node;
			}
		}
		return best;
	}
	
	private double minimax(TreeNode node, int depth, double alpha, double beta) {
		if (depth == 0 || node.board.isOver()) {
			return evaluate(node);
		}
		ArrayList<TreeNode> moves = generateChildMoves(node);
		// are we evaluating our moves or the opponent's?
		if ((node.player1 == this.player1 && node.hasMoved) ||
			 node.player1 != this.player1 && !node.hasMoved) {
			for (TreeNode n : moves) {
				double val = minimax(n, depth - 1, alpha, beta);
				alpha = Math.max(val, alpha);
				if (beta <= alpha)
					break;
			}
			return alpha;
		} else {
			for (TreeNode n : moves) {
				double val = minimax(n, depth - 1, alpha, beta);
				beta = Math.min(val, beta);
				if (beta <= alpha)
					break;
			}
			return beta;
		}
	}
	
	private double evaluate(TreeNode node) {
		if (node.board.hasLost(!player1))
			return Double.POSITIVE_INFINITY;
		if (node.board.hasLost(player1))
			return Double.NEGATIVE_INFINITY;
		
		double myPosition = node.board.getPossibleMoveCount(player1)
				- proximityToCenter(node.board, player1);
		double theirPosition = node.board.getPossibleMoveCount(!player1)
			    - proximityToCenter(node.board, !player1);
		
		// favor the a good position of ours over a bad one of theirs
		// when moving, the other way round when destroying
		if (node.hasMoved)
			return (3 * myPosition / 2) - theirPosition / 2;
		else
			return  myPosition / 2 - (3 * theirPosition / 2);
	}
	
	private double proximityToCenter(Board board, boolean player1) {
		Point pos = board.getPlayerPosition(player1);
		
		double centerx = (Board.WIDTH - 1) / 2.;
		double centery = (Board.HEIGHT - 1) / 2.;
		
		return Math.sqrt(Math.pow(centerx - pos.x, 2) 
			           + Math.pow(centery - pos.y, 2));
	}
}