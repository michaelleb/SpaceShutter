package com.andrapp.spaceshutter.util;

import java.util.Iterator;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

public class MapCrayon extends Crayon {
	
	private final static String TAG = MapCrayon.class.getSimpleName();
	
	private Map map;
	private Path adnroidMapPath = new Path();
	private Path incisionPath	= new Path();
	
	private Paint incisionPain;
	



	public MapCrayon(Resources appRes) {
		super(appRes);
		
		//map path paint settings
		paint = new Paint();
		paint.setColor(Color.parseColor("#808080"));
		paint.setStyle(Style.STROKE);
		
		//player incision path paint settings
		incisionPain = new Paint();
		incisionPain.setColor(Color.parseColor("#EB1515"));
		incisionPain.setStyle(Style.STROKE);
		incisionPain.setStrokeWidth(4);
		
	}
	
	@Override
	protected void setHost(Map map) {
		this.map = map;
	}
	
	@Override
	public void draw(Canvas c) {
		
//		Log.d(TAG,map.getMapPath().toString() +" incisions: " + map.getPlayer1incision().toString() );
		
		
		scaleMovePahttoAndroidPath(map.getMapPath(), adnroidMapPath);
		c.drawPath(adnroidMapPath, paint);
//		Log.d(TAG,"map is drawn");

		
		Iterator<MovePath> incisionsIt =  map.getIncisionPaths().iterator();
		while(incisionsIt.hasNext()){
			scaleMovePahttoAndroidPath(incisionsIt.next(),incisionPath);
		}
		//draw incisions on canvas
		c.drawPath(incisionPath, incisionPain);
		
		//prepare for next round
		adnroidMapPath.rewind();
		incisionPath.rewind();
		
	}

	@Override
	public void init() {
		
		//load sprite of the map 
		//convert to bitmap
		//init frame counters etc.
	}

}
