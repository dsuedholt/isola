package com.example.isola;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class BoardView extends View implements Observer {
	
	private Board board;
	
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BoardView(Context context, Board board) {
		super(context);
		this.board = board;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.MAGENTA);
	}

	@Override
	public void update(Observable board, Object o) {
		invalidate();
	}
	
	protected Point getTileCoords(int x, int y) {
		Point placeholder = new Point(1, 1);
		return placeholder;
	}
}
