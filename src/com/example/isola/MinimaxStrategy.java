package com.example.isola;

import java.util.ArrayList;

import android.util.Log;

public class MinimaxStrategy extends Strategy {
	
	private int depth;
	
	private class TreeNode {
		private Board board;
		private boolean hasMoved, player1;
		private int x, y;
		
		private TreeNode(Board board, boolean hasMoved, boolean player1, int x, int y) {
			this.board = board;
			this.hasMoved = hasMoved;
			this.player1 = player1;
			this.x = x;
			this.y = y;
		}
	}
	
	public MinimaxStrategy(Board board, boolean player1, int depth) {
		super(board, player1);
		
		// take into account both players having to do move AND destroy
		this.depth = depth * 2;
		
		WAITINGTIME = 0;
	}

	private ArrayList<TreeNode> generateChildMoves(TreeNode node) {
		Log.i("minimax", "entered generateChildMoves()");
		ArrayList<TreeNode> result = new ArrayList<TreeNode>();

		if (node.hasMoved) {
			// look for fields to destroy
			for (int i = 0; i < Board.WIDTH; i++) {
				for (int j = 0; j < Board.HEIGHT; j++) {
					if (node.board.canDestroy(i, j)) {
						Board board = new Board(node.board);
						board.destroy(i, j);
						TreeNode tmp = new TreeNode(board, false, node.player1, i, j);
						result.add(tmp);
					}
				}
			}

		} else {
			
			findPosition(node.board, !node.player1);
			
			// look for fields to move to
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (node.board.canMove(player1, x + i, y + j)) {
						Board board = new Board(node.board);
						board.move(player1, x + i, j + y);
						TreeNode tmp = new TreeNode(board, true, !node.player1, x + i, y + j);
						result.add(tmp);
					}
				}
			}
		}
		return result;
	}

	@Override
	protected void doMove() {
		Log.i("minimax", "entered doMove()");
		TreeNode root = new TreeNode(board, false, !player1, 0, 0);
		TreeNode best = bestMove(root);
		board.move(player1, best.x, best.y);
	}

	@Override
	protected void doDestroy() {
		Log.i("minimax", "entered doDestroy()");
		TreeNode root = new TreeNode(board, true, player1, 0, 0);
		TreeNode best = bestMove(root);
		board.destroy(best.x, best.y);
	}
	
	private TreeNode bestMove(TreeNode root) {
		String logstring = "entered bestMove() for ";
		logstring += (root.hasMoved)?"moving":"destroying";
		Log.i("minimax", logstring);
		TreeNode best = null;
		int bestVal = -10;
		for (TreeNode node : generateChildMoves(root)) {
			int val = minimax(node, depth);
			if (val > bestVal) {
				bestVal = val;
				best = node;
			}
		}
		return best;
	}
	
	private int minimax(TreeNode node, int depth) {
		Log.i("minimax", "entered minimax with depth " + depth);
		if (depth == 0 || node.board.isOver()) {
			return evaluate(node);
		}
		int bestVal;
		ArrayList<TreeNode> moves = generateChildMoves(node);
		// are we evaluating our moves or the opponent's?
		if ((node.player1 == this.player1 && node.hasMoved) ||
			 node.player1 != this.player1 && !node.hasMoved) {
			bestVal = -10;
			for (TreeNode n : moves) {
				int val = minimax(n, depth - 1);
				bestVal = Math.max(val, bestVal);
			}
		} else {
			bestVal = 10;
			for (TreeNode n : moves) {
				int val = minimax(n, depth - 1);
				bestVal = Math.min(val, bestVal);
			}
		}
		return bestVal;
	}
	
	private void findPosition(Board board, boolean player1) {
		Board tmp = this.board;
		this.board = board;
		findPosition(player1);
		this.board = tmp;
	}
	
	private int evaluate(TreeNode node) {
		int myMoves = node.board.getPossibleMoveCount(node.player1);
		int theirMoves = node.board.getPossibleMoveCount(!node.player1);
		if (myMoves == 0)
			return -9;
		if (theirMoves == 0)
			return 9;
		return myMoves - theirMoves;
	}
	
}