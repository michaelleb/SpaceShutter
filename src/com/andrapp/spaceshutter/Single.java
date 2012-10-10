package com.andrapp.spaceshutter;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Single extends View {
	private Paint paint = new Paint();
	private Path path = new Path();

	public Single(Context context, AttributeSet attrs) {
		super(context, attrs);
		System.out.println("in Single C-tor");
		paint.setAntiAlias(true);
		paint.setStrokeWidth(6f);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		System.out.println("in onDraw");
		canvas.drawPath(path, paint);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("in onTouchEvent");
		float eventX = event.getX();
		float eventY = event.getY();
		
		if(BuildConfig.DEBUG){
			Log.i(Constants.SINGLE, "coordinates: (" + eventX + ", "+ eventY +")");
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(eventX, eventY);
			System.out.println("touch event:action doun");
			return true;
		case MotionEvent.ACTION_MOVE:
			path.lineTo(eventX, eventY);
			System.out.println("touch event:action move");
			break;
		case MotionEvent.ACTION_UP: // nothing to do break; default:
			System.out.println("touch event:action up");
			return false;
		} 
		
		// Schedules a repaint.
		invalidate();
		return true;
	}
	

}