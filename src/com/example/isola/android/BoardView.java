package com.example.isola.android;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.isola.R;
import com.example.isola.game.Board;
import com.example.isola.game.GameEvent;
import com.example.isola.game.GameOverEvent;
import com.example.isola.game.MoveEvent;

/**
 * This class is responsible for drawing the Board to the screen.
 */
public class BoardView extends View implements Observer {
	
	private Board board;
	private Paint paint;
	private Paint paint_text;
	private Bitmap tile_free;
	private Bitmap tile_destroyed;
	private Bitmap tile_player1;
	private Bitmap tile_player2;
	private Bitmap tile_free_resized;
	private Bitmap tile_destroyed_resized;
	private Bitmap tile_player1_resized;
	private Bitmap tile_player2_resized;
	private GameEvent received_event;
	private int text_size;
	private Rect rect;
	private boolean initialized;
	private boolean player2_has_destroyed;
	
	private int tilewidth;
	private String infotext;
	
	/**
	 * Constructor with default parameters. Should not be called by the app.
	 */
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Construct the BoardView.
	 * @param context The application context.
	 * @param board   The game board to display.
	 */
	public BoardView(Context context, Board board) {
		super(context);
		this.board = board;
		paint = new Paint();
		paint.setColor(Color.rgb(54, 181, 255));
		paint.setStrokeWidth(3);
		paint.setStyle(Style.STROKE);
		paint_text = new Paint();
		paint_text.setColor(Color.WHITE);
		paint_text.setTextAlign(Align.CENTER);
		initialized = false;
		init();
		invalidate();
	}
	
	/**
	 * Convert coordinates from the view's system into tile coordinates on the board.
	 * @param x The x-coordinate of the point on the view.
	 * @param y The y-coordinate of the point on the view.
	 * @return A Point object containing the coordinates of a tile on the board.
	 */
	public Point getTileCoords(int x, int y) {
		Point toReturn = new Point(x / tilewidth, y / tilewidth);
		if(toReturn.x >= Board.WIDTH || toReturn.y >= Board.HEIGHT) {
			return null;
		}
		return toReturn;
	}
	
	private void init() {
		player2_has_destroyed = false;
		infotext = getResources().getString(R.string.player1_move);
	}
	
	private void init_bitmaps() {
		tile_free = BitmapFactory.decodeResource(getResources(), R.drawable.tile_free);
		tile_destroyed = BitmapFactory.decodeResource(getResources(), R.drawable.tile_destroyed);
		tile_player1 = BitmapFactory.decodeResource(getResources(), R.drawable.tile_player1);
		tile_player2 = BitmapFactory.decodeResource(getResources(), R.drawable.tile_player2);
		
		tilewidth = getWidth() / Board.WIDTH;
		text_size = getHeight() / 32;
		paint_text.setTextSize(text_size);
		rect = new Rect(5, 8 * tilewidth + 10, getWidth() - 5, getHeight() - 5);
		
		tile_free_resized = Bitmap.createScaledBitmap(tile_free, tilewidth, tilewidth, false);
		tile_destroyed_resized = Bitmap.createScaledBitmap(tile_destroyed, tilewidth, tilewidth, true);
		tile_player1_resized = Bitmap.createScaledBitmap(tile_player1, tilewidth, tilewidth, true);
		tile_player2_resized = Bitmap.createScaledBitmap(tile_player2, tilewidth, tilewidth, true);
		
		initialized = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.rgb(35, 35, 35));
		if(!initialized) {
			init_bitmaps();
		}
		for (int i = 0; i < Board.WIDTH; i++) {
			for (int j = 0; j < Board.HEIGHT; j++) {

				switch(board.getTile(i, j)) {
				case PLAYER1:
					canvas.drawBitmap(tile_player1_resized, i * tilewidth, j * tilewidth, null);
					break;
				case PLAYER2:
					canvas.drawBitmap(tile_player2_resized, i * tilewidth, j * tilewidth, null);
					break;
				case FREE:
					canvas.drawBitmap(tile_free_resized, i * tilewidth, j * tilewidth, null);
					break;
				case DESTROYED:
					canvas.drawBitmap(tile_destroyed_resized, i * tilewidth, j * tilewidth, null);
					break;
				}
			}
		}
		canvas.drawRect(rect, paint);
		
		if (received_event == null)
			return;
		
		switch (received_event.getType()) {
		case MOVE:
			if(((MoveEvent)received_event).isPlayer1()) {
				infotext = getResources().getString(R.string.player1_destroy);
			} else {
				infotext = getResources().getString(R.string.player2_destroy);
			}
			break;
		case DESTROY:
			if(player2_has_destroyed) {
				infotext = getResources().getString(R.string.player1_move);
				player2_has_destroyed = false;
			} else {
				infotext = getResources().getString(R.string.player2_move);
				player2_has_destroyed = true;
			}
			break;
		case GAMEOVER:
			if(((GameOverEvent)received_event).isPlayer1()) {
				infotext = getResources().getString(R.string.player1_win);
			} else {
				infotext = getResources().getString(R.string.player2_win);
			}
			break;
		case GAMESTART:
			init();
			break;
		}
		// centralize and draw the text
		canvas.drawText(infotext, getWidth() / 2, rect.centerY() + text_size / 2, paint_text);
	}

	@Override
	public void update(Observable board, Object o) {
		received_event = (GameEvent) o;
		postInvalidate();
	}
}
