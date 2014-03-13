package com.example.isola.game;

public class GameOverEvent extends GameEvent {
	
	private boolean player1;
	
	public GameOverEvent(boolean player1) {
		super(EventType.GAMEOVER);
		this.player1 = player1;
	}
	
	public boolean isPlayer1() {
		return player1;
	}
}
