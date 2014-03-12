package com.example.isola;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class BoardView extends View implements Observer {
	
	private Board board;
	private Paint paint;
	private Rect rect;
	
	private int tilewidth, tileheight;
	
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BoardView(Context context, Board board) {
		super(context);
		this.board = board;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(3);
		invalidate();
		rect = new Rect();
	}
	
	public Point getTileCoords(int x, int y) {
		tilewidth = getWidth() / Board.WIDTH;
		tileheight = tilewidth;
		Point toReturn = new Point(x / tilewidth, y / tileheight);
		if(toReturn.x >= Board.WIDTH || toReturn.y >= Board.HEIGHT) {
			return null;
		}
		return toReturn;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		tilewidth = getWidth() / Board.WIDTH;
		tileheight = tilewidth;
		canvas.drawColor(Color.WHITE);
		for (int i = 0; i < Board.WIDTH; i++) {
			for (int j = 0; j < Board.HEIGHT; j++) {

				rect.set(i * tilewidth, j * tileheight, (i + 1) * tilewidth, (j + 1) * tileheight);
				switch(board.getTile(i, j)) {
				case PLAYER1:
					paint.setColor(Color.RED); break;
				case PLAYER2:
					paint.setColor(Color.BLUE); break;
				case FREE:
					paint.setColor(Color.BLACK); break;
				case DESTROYED:
					paint.setColor(Color.BLACK); break;
				}
				if (board.getTile(i, j) == Tile.FREE) {
					paint.setStyle(Style.STROKE);
				} else {
					paint.setStyle(Style.FILL);
				}
				canvas.drawRect(rect, paint);
			}
		}
	}

	@Override
	public void update(Observable board, Object o) {
		postInvalidate();
	}
}
