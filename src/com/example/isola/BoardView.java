package com.example.isola;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class BoardView extends View implements Observer {
	
	private Board board;
	
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		board = new Board();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
	}

	@Override
	public void update(Observable board, Object o) {
		board = (Board) board;
		invalidate();
	}
}
