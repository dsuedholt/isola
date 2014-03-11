package com.example.isola;

public class MinimaxStrategy implements Strategy {
	
	private SimpleStrategy dummy;
	
	public MinimaxStrategy(int depth) {
		dummy = new SimpleStrategy();
	}
	
	@Override
	public void play(Board board, boolean player1) {
		dummy.play(board, player1);
	}

}
