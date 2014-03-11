package com.example.isola;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class BoardView extends View implements Observer {
	private boolean initialized = false;
	private Board board;
	private int height_display;
	private int width_display;
	private int side_length_square;
	
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BoardView(Context context, Board board) {
		super(context);
		this.board = board;
		invalidate();
		
		//getting the screen resolution
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		//the new solution would require at least API level 13 (current min is 8)
		height_display = display.getHeight();
		width_display = display.getWidth();
	}
	
	
	private void initiate_drawing(Canvas canvas) {
		Bitmap tyle_free = BitmapFactory.decodeResource(getResources(), R.drawable.tyle_free);
		side_length_square = (int) width_display / 8;
		Bitmap tyle_free_resized = Bitmap.createScaledBitmap(tyle_free, side_length_square, side_length_square, true);
		int pos_x = side_length_square;
		int pos_y = (int) (height_display - width_display) / 2; //width_display = 8 * side_length_square
		for(int i = 1; i <= 8; i++) {
			pos_x = side_length_square;
			for(int k = 1; k <= 6; k++) {
				canvas.drawBitmap(tyle_free_resized, pos_x, pos_y, null);
				pos_x += side_length_square;
			}
			pos_y += side_length_square;
		}
		
		initialized = true;
	}
	
	protected void onDraw(Canvas canvas) {
		if(!initialized) {
			initiate_drawing(canvas);
		} else {
			// TODO: destroy tyle or move player
		}
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
