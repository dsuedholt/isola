package com.example.isola;

public class MinimaxStrategy extends Strategy {
	
	private SimpleStrategy dummy;
	
	public MinimaxStrategy(Board board, boolean player1, int depth) {
		super(board, player1);
		dummy = new SimpleStrategy(board, player1);
	}

	@Override
	protected void doMove() {
		dummy.doMove();
	}

	@Override
	protected void doDestroy() {
		dummy.doDestroy();
	}
	
	private double evaluate(Board board) {
		return 0;
	}
	
}
