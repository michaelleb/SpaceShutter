package com.andrapp.spaceshutter.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.andrapp.spaceshutter.model.Monster;
import com.andrapp.spaceshutter.model.Player;

public class PlayerCrayon extends Crayon {

	private static final String TAG = PlayerCrayon.class.getSimpleName();
	
	private Paint paint;
	private Player player;
	private Bitmap bitmap;      // the animation sequence
	private Rect sourceRect;    // the rectangle to be drawn from the animation bitmap
	private int frameNr;        // number of frames in animation
	private int currentFrame;   // the current frame
	private long frameTicker;   // the time of the last frame update
	private int framePeriod;    // milliseconds between each frame (1000/fps)
	private int spriteWidth;    // the width of the sprite to calculate the cut out rectangle
	private int spriteHeight;   // the height of the sprite
	private int x;              // the X coordinate of the object (top left of the image)
	private int y;              // the Y coordinate of the object (top left of the image)
	private int playerRadius = 30;			// radius of the player image
	

	
	public PlayerCrayon(Resources appRes){
		super(appRes);
		
		paint = new Paint();
		paint.setColor(Color.parseColor("#321BA8"));
		paint.setStyle(Style.FILL_AND_STROKE);
	}
	
	@Override
	public void setHost(Player p){player = p;}
	
	
	public void draw(Canvas c) {
		this.getRealPoint(player.getLocationX(),player.getLocationY());
		c.drawCircle(realPoint.x , realPoint.y, playerRadius, paint) ;
	}


	public void init() {
		
		//load sprite of the player 
		//convert to bitmap
		//init frame counters etc.
	}


	

}
