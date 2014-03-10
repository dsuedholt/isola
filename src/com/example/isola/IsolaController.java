package com.example.isola;

public class IsolaController implements Runnable {
	
	private boolean humanP1, humanP2;
	private Strategy comp1, comp2;
	private boolean p1turn;
	private Board board;
	
	public IsolaController() {
		humanP1 = true;
		humanP2 = true;
		p1turn = true;
		board = Board.getBoard();
	}
	
	public boolean isHumanP1() {
		return humanP1;
	}

	public void setHumanP1() {
		this.humanP1 = true;
	}

	public boolean isHumanP2() {
		return humanP2;
	}

	public void setHumanP2() {
		this.humanP2 = true;
	}
	
	public void setComputerP1(Strategy comp) {
		humanP1 = false;
		comp1 = comp;
	}
	
	public void setComputerP2(Strategy comp) {
		humanP2 = false;
		comp2 = comp;
	}
	
	public void run() {
		board.init();
		while (!board.isOver()) {
			if (p1turn) {
				if (humanP1) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					comp1.play();
				}
			}
			else {
				if (humanP2) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					comp2.play();
				}
			}
			p1turn = !p1turn;
		}
	}
}
