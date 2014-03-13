package com.example.isola.ai;

import java.util.ArrayList;

import android.graphics.Point;

import com.example.isola.game.Board;

public class MinimaxStrategy extends Strategy {
	
	private int depth;
	
	private class TreeNode {
		private Board board;
		private boolean hasMoved, player1;
		private int x, y;
		
		private TreeNode(Board board, boolean hasMoved, boolean player1, int x, int y) {
			this.board = board;
			this.hasMoved = hasMoved;
			// the player that has made the move leading to this node
			this.player1 = player1;
			this.x = x;
			this.y = y;
		}
	}
	
	public MinimaxStrategy(Board board, boolean player1, int depth) {
		super(board, player1);
		
		// take into account both players having to do move AND destroy
		this.depth = depth * 2 + 1;
		
		WAITINGTIME = 800 - depth * 100;
		WAITINGTIME = (WAITINGTIME > 0)?WAITINGTIME:0;
	}

	private ArrayList<TreeNode> generateChildMoves(TreeNode node) {
		ArrayList<TreeNode> result = new ArrayList<TreeNode>();	
		
		if (node.hasMoved) {
			Point pos = node.board.getPlayerPosition(!node.player1);
			// look for fields to destroy
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
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
		int bestVal = Integer.MIN_VALUE;
		for (TreeNode node : generateChildMoves(root)) {
			int val = minimax(node, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
			if (val > bestVal) {
				bestVal = val;
				best = node;
			}
		}
		return best;
	}
	
	private int minimax(TreeNode node, int depth, int alpha, int beta) {
		if (depth == 0 || node.board.isOver()) {
			return evaluate(node);
		}
		ArrayList<TreeNode> moves = generateChildMoves(node);
		// are we evaluating our moves or the opponent's?
		if ((node.player1 == this.player1 && node.hasMoved) ||
			 node.player1 != this.player1 && !node.hasMoved) {
			for (TreeNode n : moves) {
				int val = minimax(n, depth - 1, alpha, beta);
				alpha = Math.max(val, alpha);
				if (beta <= alpha)
					break;
			}
			return alpha;
		} else {
			for (TreeNode n : moves) {
				int val = minimax(n, depth - 1, alpha, beta);
				beta = Math.min(val, beta);
				if (beta <= alpha)
					break;
			}
			return beta;
		}
	}
	
	private int evaluate(TreeNode node) {
		if (node.board.hasLost(!player1))
			return 1000;
		if (node.board.hasLost(player1))
			return -1000;
		
		if (node.hasMoved)
			return (3 * node.board.getPossibleMoveCount(player1) / 2) + (8 - node.board.getPossibleMoveCount(!node.player1)) / 2;
		else
			return (3 * (8 - node.board.getPossibleMoveCount(!node.player1)) / 2) + node.board.getPossibleMoveCount(player1) / 2;
	}
	
}