package com.example.isola;

public enum Player {
	HUMAN(R.string.human),
	EASY(R.string.computer_easy),
	MEDIUM(R.string.computer_medium),
	HARD(R.string.computer_hard);
	
	private int id;
	
	private Player(int id){
        this.id = id;
    }
	
	public String toString(){
        return ChooseOpponentActivity.getContext().getString(id);
    }
}