package com.example.isola.game;

import com.example.isola.R;
import com.example.isola.android.ChooseOpponentActivity;

/**
 * This enum represens all the types of players currently implemented: Humans, Computer players 
 * and networked (bluetooth) players
 */
public enum Player {
	HUMAN(R.string.human),
	EASY(R.string.computer_easy),
	MEDIUM(R.string.computer_medium),
	HARD(R.string.computer_hard),
	BTPLAYER(R.string.bt_player);
	
	private int id;
	
	private Player(int id){
        this.id = id;
    }
	
	public String toString(){
        return ChooseOpponentActivity.getContext().getString(id);
    }
	
	/**
	 * @param p The specified player.
	 * @return True if the specified player is a computer player.
	 */
	public static boolean isComputer(Player p) {
		return p == EASY || p == MEDIUM || p == HARD;
	}
}